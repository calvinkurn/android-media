package com.tokopedia.v2.home.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class HomeLiveData<T: Any>(private val initValue: T): MediatorLiveData<T>() {

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