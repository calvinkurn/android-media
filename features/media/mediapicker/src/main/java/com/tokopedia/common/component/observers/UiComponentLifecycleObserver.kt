package com.tokopedia.common.component.observers

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.common.component.utils.getSafeLifecycleOwner

class UiComponentLifecycleObserver(
    private val owner: LifecycleOwner,
    private val release: () -> Unit
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        owner.getSafeLifecycleOwner()
            .lifecycle
            .removeObserver(this)

        release()
    }

}