/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tokopedia.translator.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.translator.manager.TranslatorManager
import com.tokopedia.translator.manager.TranslatorManagerFragment
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.lang.ref.WeakReference


class ActivityTranslatorCallbacks : Application.ActivityLifecycleCallbacks {

    private val translatorManager = TranslatorManager.getInstance()

    private val fragmentTranslatorCallbacks: FragmentManager.FragmentLifecycleCallbacks = FragmentTranslatorCallbacks()

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentTranslatorCallbacks, true)
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (SharedPrefsUtils.getBooleanPreference(activity, TranslatorSettingView.IS_ENABLE, false)) {
            Log.i(TAG, "onActivityResumed() invoked of :" + activity.localClassName)
            val weakActivity = WeakReference<Activity>(activity)
            translatorManager?.clearSelectors()
            translatorManager?.prepareSelectors(activity)
            TranslatorManager.setCurrentActivity(weakActivity)

            val rootView = activity.window.decorView
            setAddonGlobalLayoutListener(rootView, translatorManager)
            setScrollChangedLayoutListener(rootView, translatorManager)
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        val rootView = activity.window.decorView
        TranslatorManager.clearCurrentActivity()
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(translatorManagerOnGlobalLayoutListener(translatorManager))
        rootView.viewTreeObserver.removeOnScrollChangedListener(translatorScrollChangedLayoutListener(translatorManager))

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentTranslatorCallbacks)
        }
    }

    private fun setAddonGlobalLayoutListener(rootView: View, translatorManager: TranslatorManager?) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(translatorManagerOnGlobalLayoutListener(translatorManager))
    }

    private fun setScrollChangedLayoutListener(rootView: View, translatorManager: TranslatorManager?) {
        rootView.viewTreeObserver.addOnScrollChangedListener(translatorScrollChangedLayoutListener(translatorManager))
    }

    private fun translatorManagerOnGlobalLayoutListener(translatorManager: TranslatorManager?) = ViewTreeObserver.OnGlobalLayoutListener {
        translatorManager?.startTranslate()
    }

    private fun translatorScrollChangedLayoutListener(translatorManager: TranslatorManager?) = ViewTreeObserver.OnScrollChangedListener {
        Handler(Looper.getMainLooper()).postDelayed({
            translatorManager?.startTranslate()
        }, DELAY_SCROLLING_TO_IDLE)
    }

    companion object {
        private val TAG = ActivityTranslatorCallbacks::class.java.simpleName

        private const val DELAY_SCROLLING_TO_IDLE = 300L
    }

    internal inner class FragmentTranslatorCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

        private val translatorManagerFragment = TranslatorManagerFragment.getInstance()

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            setTranslatorBottomSheet(f)
        }

        override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
            super.onFragmentDetached(fm, f)
            if (f is BottomSheetUnify) {
                val rootView = f.view
                rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(translatorManagerOnGlobalLayoutListener(rootView, translatorManagerFragment))
                rootView?.viewTreeObserver?.removeOnScrollChangedListener(translatorScrollChangedLayoutListener(translatorManagerFragment))

                TranslatorManagerFragment.clearCurrentFragment()
            }
        }

        private fun setTranslatorBottomSheet(f: Fragment) {
            val mContext = f.context
            if (f is BottomSheetUnify) {
                if (mContext?.let { SharedPrefsUtils.getBooleanPreference(it, TranslatorSettingView.IS_ENABLE, false) } == true) {
                    Log.i(TAG, "onFragmentResumed() invoked of :" + f::class.java.simpleName)

                    val weakFragment = WeakReference<Fragment>(f)

                    translatorManagerFragment?.clearSelectors()
                    translatorManagerFragment?.prepareSelectors(f)
                    TranslatorManagerFragment.setCurrentFragment(weakFragment)

                    val rootView = f.view
                    if (rootView != null) {
                        setAddonGlobalLayoutListener(rootView, translatorManagerFragment)
                        setScrollChangedLayoutListener(rootView, translatorManagerFragment)
                    }
                }
            }
        }

        private fun setAddonGlobalLayoutListener(rootView: View, translatorManagerFragment: TranslatorManagerFragment?) {
            rootView.viewTreeObserver.addOnGlobalLayoutListener(translatorManagerOnGlobalLayoutListener(rootView, translatorManagerFragment))
        }

        private fun setScrollChangedLayoutListener(rootView: View, translatorManagerFragment: TranslatorManagerFragment?) {
            rootView.viewTreeObserver.addOnScrollChangedListener(translatorScrollChangedLayoutListener(translatorManagerFragment))
        }

        private fun translatorManagerOnGlobalLayoutListener(rootView: View, translatorManagerFragment: TranslatorManagerFragment?): ViewTreeObserver.OnGlobalLayoutListener {
            return object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    translatorManagerFragment?.startTranslate()
                }
            }
        }

        private fun translatorScrollChangedLayoutListener(translatorManagerFragment: TranslatorManagerFragment?) = ViewTreeObserver.OnScrollChangedListener {
            Handler(Looper.getMainLooper()).postDelayed({
                translatorManagerFragment?.startTranslate()
            }, DELAY_SCROLLING_TO_IDLE)
        }
    }
}
