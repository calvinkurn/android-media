package com.tokopedia.common.component.observers

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.common.component.utils.safeLifecycleOwner

class ImmediateLifecycleObserver(
    private val owner: LifecycleOwner,
    private val componentCreation: () -> Unit
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreated() {
        try {
            componentCreation()
        } catch (t: Throwable) {
            throw t
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        owner.safeLifecycleOwner()
            .lifecycle
            .removeObserver(this)
    }

}