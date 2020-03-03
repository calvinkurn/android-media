package com.tokopedia.kotlin.extensions.view

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LifecycleOwner.observe(liveData: LiveData<T>, block: (T) -> Unit) {
    liveData.observe(this, Observer { block(it) })
}

fun <T> LifecycleOwner.removeObservers(liveData: LiveData<T>) {
    liveData.removeObservers(this)
}