package com.tokopedia.product.addedit.common.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.getValueOrDefault(defaultValue: T) = value ?: defaultValue