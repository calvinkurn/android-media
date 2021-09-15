package com.tokopedia.shop.home.view.viewmodel

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class GGZ {

    @Test
    fun `test shared flow with deferred`() = runBlockingTest {
        val sharedFlow = MutableSharedFlow<Int>(replay = 0)

        val deferred = async {
            sharedFlow.first()
        }

        sharedFlow.emit(1)

        Assert.assertEquals(1, deferred.await())
    }
}