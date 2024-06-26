package com.tokopedia.home.beranda.helper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.HomeVisitable

fun <T> List<T>?.copy(): List<T>{
    return if(this != null) ArrayList(this)
    else ArrayList()
}


fun <X, Y> LiveData<X>.map(func: (source: X) -> Y) = Transformations.map(this, func)

fun <X, Y> LiveData<X>.switchMap(func: (source: X?) -> LiveData<Y>) = Transformations.switchMap(this, func)