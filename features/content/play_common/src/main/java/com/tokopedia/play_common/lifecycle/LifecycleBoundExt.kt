package com.tokopedia.play_common.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

/**
 * Created by jegul on 09/09/20
 */
fun <LO: LifecycleOwner, T: Any> LO.lifecycleBound(
        creator: (LO) -> T,
        onLifecycle: DefaultOnLifecycle<T>? = null
): LifecycleBoundDelegate<LO, T> {
    return LifecycleBoundDelegate(creator, onLifecycle)
}

fun <T: Any> Fragment.viewLifecycleBound(
        creator: (Fragment) -> T,
        onLifecycle: DefaultOnLifecycle<T>? = null
): ViewLifecycleBoundDelegate<T> {
    return ViewLifecycleBoundDelegate(creator, onLifecycle)
}

fun <T: Any> whenLifecycle(onLifecycleHandler: DefaultOnLifecycle<T>.() -> Unit): DefaultOnLifecycle<T> {
    return DefaultOnLifecycle<T>().apply {
        onLifecycleHandler()
    }
}