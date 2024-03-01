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
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.translator.manager.TranslatorManager
import com.tokopedia.translator.manager.TranslatorManagerFragment
import com.tokopedia.translator.ui.SharedPrefsUtils
import com.tokopedia.translator.ui.TranslatorSettingView
import com.tokopedia.translator.ui.TranslatorSettingView.DELIM_LANG_CODE
import com.tokopedia.translator.ui.TranslatorSettingView.DESTINATION_LANGUAGE
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext


class ActivityTranslatorCallbacks : Application.ActivityLifecycleCallbacks, CoroutineScope {

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
            TranslatorManager.setCurrentActivity(weakActivity)
            translatorManager?.clearSelectors()

            val rootView: View = activity.window.decorView.findViewById(android.R.id.content)
            setAddonGlobalLayoutListener(rootView)

            launch {

                rootView.viewTreeObserver
                    .onScrollChangedAsFlow()
                    .collect {
                        translatorManager?.startTranslate()
                    }

                onDestLanguageChangedAsFlow(activity)
                    .collect {
                        translatorManager?.startTranslate()
                    }
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentTranslatorCallbacks)
        }
    }

    private fun setAddonGlobalLayoutListener(rootView: View) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {

                launch {
                    translatorManager?.startTranslate()
                }

                rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    @OptIn(FlowPreview::class)
    fun ViewTreeObserver.onScrollChangedAsFlow(): Flow<Unit> {

        return callbackFlow<Unit> {

            val onScrollChangedListener = ViewTreeObserver.OnScrollChangedListener {
                trySend(Unit)
            }

            addOnScrollChangedListener(onScrollChangedListener)

            awaitClose {
                removeOnScrollChangedListener(onScrollChangedListener)
            }
        }.debounce(DELAYING_SCROLL_TO_IDLE)
    }

    private fun onDestLanguageChangedAsFlow(activity: Activity) = callbackFlow<String> {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == DESTINATION_LANGUAGE) {
                trySend(getDestinationLang(sharedPreferences))
            }
        }

        sharedPref.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPref.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    private fun getDestinationLang(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString(DESTINATION_LANGUAGE, "")
            ?.split(DELIM_LANG_CODE.toRegex())
            ?.dropLastWhile { it.isEmpty() }
            ?.toTypedArray()?.getOrNull(1) ?: ""
    }

    companion object {
        private val TAG = ActivityTranslatorCallbacks::class.java.simpleName

        private const val DELAYING_SCROLL_TO_IDLE = 500L
    }

    internal inner class FragmentTranslatorCallbacks : FragmentManager.FragmentLifecycleCallbacks() {

        private val translatorManagerFragment = TranslatorManagerFragment.getInstance()

        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
            super.onFragmentResumed(fm, f)
            setTranslatorFragment(f)
        }

        private fun setTranslatorFragment(f: Fragment) {
            if (f.activity != null) {
                val weakActivity = WeakReference<Activity>(f.activity)
                translatorManager?.clearSelectors()
                TranslatorManager.setCurrentActivity(weakActivity)
            }
            if (f is BottomSheetUnify) {
                val mContext = f.context
                if (mContext?.let { SharedPrefsUtils.getBooleanPreference(it, TranslatorSettingView.IS_ENABLE, false) } == true) {
                    Log.i(TAG, "onFragmentResumed() invoked of :" + f::class.java.simpleName)

                    val weakFragment = WeakReference<Fragment>(f)
                    TranslatorManagerFragment.setCurrentFragment(weakFragment)
                    translatorManagerFragment?.clearSelectors()

                    f.view?.let {
                        setAddonGlobalLayoutListener(it)
                        launch {

                            it.viewTreeObserver.onScrollChangedAsFlow().collect {
                                translatorManagerFragment?.startTranslate()
                            }
                        }
                    }
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + CoroutineExceptionHandler { _, _ -> }
}
