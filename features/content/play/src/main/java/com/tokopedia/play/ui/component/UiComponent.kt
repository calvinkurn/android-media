package com.tokopedia.play.ui.component

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.tokopedia.play.util.CachedState

/**
 * Created by kenny.hadisaputra on 06/07/22
 */
interface UiComponent<T> : LifecycleEventObserver {

    fun render(state: CachedState<T>)

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {}
}