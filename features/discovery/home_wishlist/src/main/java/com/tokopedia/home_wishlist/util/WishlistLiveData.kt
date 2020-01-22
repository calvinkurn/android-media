package com.tokopedia.home_wishlist.util

import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class WishlistLiveData<T: Any>(private val initValue: T): MutableLiveData<T>() {

    init {
        value = initValue
    }

    override fun getValue(): T {
        return super.getValue() ?: initValue
    }

    override fun setValue(value: T) {
        super.setValue(value)
    }

    fun observe(owner: LifecycleOwner, body: (T) -> Unit) {
        observe(owner, Observer<T> { t -> body(t!!) })
    }

    override fun postValue(value: T) {
        super.postValue(value)
    }
}