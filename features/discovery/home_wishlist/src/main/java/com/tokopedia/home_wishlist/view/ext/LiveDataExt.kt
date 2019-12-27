package com.tokopedia.home_wishlist.view.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

/**
 * Maps any values that were emitted by the LiveData to the given function
 */
fun <T, O> LiveData<T>.map(function: MapperFunction<T, O>): LiveData<O> {
    return Transformations.map(this, function)
}

/**
 * Maps any values that were emitted by the LiveData to the given function that produces another LiveData
 */
fun <T, O> LiveData<T>.switchMap(function: MapperFunction<T, LiveData<O>>): LiveData<O> {
    return Transformations.switchMap(this, function)
}

fun <T : Any?> MutableLiveData<T>.default(initialValue: T) = apply { setValue(initialValue) }

/**
 * Mapper function used in the operators that need mapping
 */
typealias MapperFunction<T, O> = (T) -> O