package com.tokopedia.gamification.giftbox.util

import androidx.lifecycle.MutableLiveData
import com.tokopedia.gamification.pdp.data.LiveDataResult
import org.junit.Assert

object TestUtil {
    fun <T> MutableLiveData<LiveDataResult<T>>.verifyAssertEquals(
        expected: Any?,
        status: LiveDataResult.STATUS
    ) {
        Assert.assertEquals(expected, value?.data)
        Assert.assertEquals(status, value?.status)
    }

    fun Any.verifyAssertEquals(
        expected: Any?
    ) {
        Assert.assertEquals(expected, this)
    }
}
