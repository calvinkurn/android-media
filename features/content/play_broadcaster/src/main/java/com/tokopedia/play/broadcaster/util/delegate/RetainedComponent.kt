package com.tokopedia.play.broadcaster.util.delegate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified T: ViewModel> ViewModelStoreOwner.retainedComponent(
    noinline owner: () -> ViewModelStoreOwner = { this },
    noinline creator: () -> T
): RetainedComponentDelegate<T> {
    return RetainedComponentDelegate(
        clazz = T::class.java,
        owner = owner,
        componentCreator = creator
    )
}