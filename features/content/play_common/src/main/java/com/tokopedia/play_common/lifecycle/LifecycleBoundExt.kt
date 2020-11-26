package com.tokopedia.play_common.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

/**
 * Created by jegul on 09/09/20
 */
fun <LO: LifecycleOwner, T: Any> LO.lifecycleBound(creator: (LO) -> T, onDestroy: (T) -> Unit = {}): LifecycleBoundDelegate<LO, T> {
    return LifecycleBoundDelegate(creator, onDestroy)
}

fun <T: Any> Fragment.viewLifecycleBound(creator: (Fragment) -> T, onDestroy: (T) -> Unit = {}): LifecycleBoundDelegate<Fragment, T> {
    return LifecycleBoundDelegate(creator, onDestroy)
}