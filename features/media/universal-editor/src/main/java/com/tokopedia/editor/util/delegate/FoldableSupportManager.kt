package com.tokopedia.editor.util.delegate

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import androidx.window.layout.WindowLayoutInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

internal class FoldableSupportManager constructor(
    private val listener: Listener,
    activity: FragmentActivity
) : DefaultLifecycleObserver {

    private val windowInfoTracker: WindowInfoTracker
    private val activityRef: WeakReference<FragmentActivity>

    private var job: Job? = null

    init {
        activity.lifecycle.addObserver(this)

        activityRef = WeakReference(activity)
        windowInfoTracker = WindowInfoTracker.getOrCreate(activity)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        job = CoroutineScope(Dispatchers.Main).launch {
            try {
                activityRef.get()?.let {
                    windowInfoTracker.windowLayoutInfo(it)
                        .collect { updatedInfo ->
                            listener.onLayoutChanged(ScreenInfo(updatedInfo))
                        }
                }
            } catch (ignored: Throwable) {}
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        job?.cancel()
    }

    interface Listener {
        fun onLayoutChanged(info: ScreenInfo)
    }
}

internal class ScreenInfo constructor(
    info: WindowLayoutInfo
) {

    private var foldingFeature: FoldingFeature? = null

    init {
        foldingFeature = info
            .displayFeatures
            .filterIsInstance(FoldingFeature::class.java)
            .firstOrNull()
    }

    fun isFoldableDevice() = foldingFeature != null
}
