package com.tokopedia.shop.flashsale.common.extension

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        if (this.value != null && liveData.value != null)
            result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        if (this.value != null && liveData.value != null)
            result.value = block(this.value, liveData.value)
    }
    return result
}

fun <T: Any, K: Any, R> LiveData<Result<T>>.combineResultWith(
    liveData: LiveData<Result<K>>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result: LiveData<R> = this.combineWith(liveData){ result1, result2->
        val a = result1 as? Success
        val b = result2 as? Success
        return@combineWith block(a?.data, b?.data)
    }
    return result
}
