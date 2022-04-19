package com.tokopedia.play_common.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * Created by jegul on 21/09/20
 */
class ViewLifecycleBoundDelegate<T: Any> internal constructor(
        creator: (Fragment) -> T,
        onLifecycle: DefaultOnLifecycle<T>? = null
) : AbstractLifecycleBoundDelegate<Fragment, T>(creator, onLifecycle) {

    override val lock: Any = this

    override fun getValidLifecycleOwner(thisRef: Fragment): LifecycleOwner {
        return thisRef.viewLifecycleOwner
    }

    override fun addObserver(owner: Fragment, observer: LifecycleObserver) {
        owner.viewLifecycleOwnerLiveData.observe(owner, Observer { viewLifecycleOwner ->
            viewLifecycleOwner.lifecycle.addObserver(observer)
        })
    }
}