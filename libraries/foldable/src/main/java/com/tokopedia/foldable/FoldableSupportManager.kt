package com.tokopedia.foldable

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference


class FoldableSupportManager(
    private val foldableInfoCallback: FoldableInfoCallback,
    activity: AppCompatActivity,
) : LifecycleObserver {
    private val windowInfoRepo: WindowInfoTracker
    private val activityRef: WeakReference<Activity>

    init {
        activity.lifecycle.addObserver(this)
        activityRef = WeakReference(activity)
        windowInfoRepo = WindowInfoTracker.getOrCreate(activity)
    }

    private var layoutUpdatesJob: Job? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        layoutUpdatesJob = CoroutineScope(Dispatchers.Main).launch {
            try {
                activityRef.get()?.let {
                    windowInfoRepo.windowLayoutInfo(activity = it)
                        .collect { newLayoutInfo ->
                            foldableInfoCallback.onChangeLayout(FoldableInfo(newLayoutInfo))
                        }
                }
            }catch (e:Throwable){

            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
        layoutUpdatesJob?.cancel()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {

    }

    //    https://stackoverflow.com/questions/47656728/is-it-mandatory-to-remove-yourself-as-an-observer-from-android-lifecycle
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {

    }

    interface FoldableInfoCallback {
        fun onChangeLayout(foldableInfo: FoldableInfo)
    }
}
