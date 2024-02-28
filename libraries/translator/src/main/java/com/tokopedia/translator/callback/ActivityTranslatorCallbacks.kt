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

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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

            launch {
                translatorManager?.prepareSelectors(activity)
                setAddonGlobalLayoutListener(rootView)

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

    private fun setRecyclerViewScrollListener(currentRecyclerView: RecyclerView): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            var lastScrollY = -1

            private val handler = Handler()

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                handler.removeCallbacksAndMessages(null)

                handler.postDelayed({
                    val currentScrollY = currentRecyclerView.computeVerticalScrollOffset()

                    if (currentScrollY != lastScrollY) {
                        startTranslate(currentRecyclerView)
                        lastScrollY = currentScrollY
                    }
                }, DELAYING_SCROLL_TO_IDLE)
            }
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setGestureDetector(rootView: View, activity: Activity) {
        val gestureDetector = GestureDetector(activity, object : GestureDetector.SimpleOnGestureListener() {

            private var initialScrollY = 0

            override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                if (Math.abs(distanceY) > 0) {
                    val currentScrollY = rootView.scrollY

                    if (currentScrollY != initialScrollY) {
                        startTranslate(rootView)

                        // Update the initial scroll position
                        initialScrollY = currentScrollY
                    }
                }

                return super.onScroll(e1, e2, distanceX, distanceY)
            }
        })

        rootView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun setScrollChangedLayoutListener(rootView: View) {
        rootView.viewTreeObserver.addOnScrollChangedListener(translatorScrollChangedLayoutListener(rootView))
    }

    private fun translatorManagerOnGlobalLayoutListener(rootView: View) = ViewTreeObserver.OnGlobalLayoutListener {
        startTranslate(rootView)
    }

    fun ViewTreeObserver.onScrollChangedAsFlow() = callbackFlow<Unit> {
        var job: Job? = null

        val listener = ViewTreeObserver.OnScrollChangedListener {
            job?.cancel()
            job = launch(coroutineContext) {
                delay(DELAYING_SCROLL_TO_IDLE)
                trySend(Unit)
            }
        }

        addOnScrollChangedListener(listener)

        awaitClose {
            removeOnScrollChangedListener(listener)
            job?.cancel()
        }
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

    private fun translatorScrollChangedLayoutListener(rootView: View): ViewTreeObserver.OnScrollChangedListener {
        return object : ViewTreeObserver.OnScrollChangedListener {

            override fun onScrollChanged() {

                launch(coroutineContext) {

                }
            }
        }
    }

    private fun startTranslate(rootView: View) {
        rootView.postDelayed({
//            translatorManager?.startTranslate()
        }, DELAYING_SCROLL_TO_IDLE)
    }

    companion object {
        private val TAG = ActivityTranslatorCallbacks::class.java.simpleName

        private const val DELAYING_SCROLL_TO_IDLE = 300L
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
                rootView?.viewTreeObserver?.removeOnGlobalLayoutListener(translatorManagerOnGlobalLayoutListener(rootView))
            }
        }

        private fun setTranslatorBottomSheet(f: Fragment) {
            if (f is BottomSheetUnify) {
                val mContext = f.context
                if (mContext?.let { SharedPrefsUtils.getBooleanPreference(it, TranslatorSettingView.IS_ENABLE, false) } == true) {
                    Log.i(TAG, "onFragmentResumed() invoked of :" + f::class.java.simpleName)

                    val weakFragment = WeakReference<Fragment>(f)
                    TranslatorManagerFragment.setCurrentFragment(weakFragment)
                    translatorManagerFragment?.clearSelectors()

                    f.view?.let {

                        launch {

                            translatorManagerFragment?.prepareSelectors(f)

                            setAddonGlobalLayoutListener(it)

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
