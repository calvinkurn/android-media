package com.tokopedia.product.addedit.common.util

import androidx.lifecycle.LiveData

fun <T> LiveData<T>.getValueOrDefault(defaultValue: T) = value ?: defaultValue