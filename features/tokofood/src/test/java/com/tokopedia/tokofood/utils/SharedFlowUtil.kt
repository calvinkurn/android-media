package com.tokopedia.tokofood.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope

@ExperimentalCoroutinesApi
fun<T> SharedFlow<T>.collectFromSharedFlow(whenAction: () -> Unit,
                                           then: (T?) -> Unit) {
    val testCoroutineScope = TestCoroutineScope().apply {
        pauseDispatcher()
    }
    var actualValue: T? = null
    val job = testCoroutineScope.launch {
        collect {
            actualValue = it
        }
    }
    testCoroutineScope.runCurrent()
    whenAction()
    testCoroutineScope.runCurrent()
    then(actualValue)
    job.cancel()
}