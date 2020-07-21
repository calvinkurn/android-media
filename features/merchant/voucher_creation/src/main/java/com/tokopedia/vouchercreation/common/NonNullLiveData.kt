package com.tokopedia.vouchercreation.common

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class NonNullLiveData<T : Any>(private val defaultValue: T) : MutableLiveData<T>() {

    override fun getValue(): T = super.getValue() ?: defaultValue

    fun observe(owner: LifecycleOwner, body: (T) -> Unit) {
        observe(owner, Observer<T> {
            body(it ?: defaultValue)
        })
    }
}