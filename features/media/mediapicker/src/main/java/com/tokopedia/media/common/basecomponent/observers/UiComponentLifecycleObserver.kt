package com.tokopedia.media.common.basecomponent.observers

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.media.common.basecomponent.utils.safeLifecycleOwner

class UiComponentLifecycleObserver(
    private val owner: LifecycleOwner,
    private val release: () -> Unit
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroyed() {
        owner.safeLifecycleOwner()
            .lifecycle
            .removeObserver(this)

        release()
    }

}