package com.tokopedia.topads.credit.history.view.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer

class TestObserver<T> : Observer<T> {
    val observedValues = mutableListOf<T?>()

    override fun onChanged(t: T?) {
        observedValues.add(t)
    }
}

fun <T> LiveData<T>.testObserver() = TestObserver<T>().also {
    observeForever(it)
}