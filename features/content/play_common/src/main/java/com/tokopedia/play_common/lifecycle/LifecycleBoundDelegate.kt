package com.tokopedia.play_common.lifecycle

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Created by jegul on 09/09/20
 */
class LifecycleBoundDelegate<LO: LifecycleOwner, T: Any> internal constructor(
        creator: (LO) -> T,
        onLifecycle: DefaultOnLifecycle<T>? = null
) : AbstractLifecycleBoundDelegate<LO, T>(creator, onLifecycle) {

    override val lock: Any = this

    override fun getValidLifecycleOwner(thisRef: LO): LifecycleOwner {
        return thisRef
    }

    override fun addObserver(owner: LO, observer: LifecycleObserver) {
        owner.lifecycle.addObserver(observer)
    }
}