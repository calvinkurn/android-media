package com.tokopedia.tokofood.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.createTestCoroutineScope
import kotlinx.coroutines.test.pauseDispatcher
import kotlinx.coroutines.test.runCurrent

@ExperimentalCoroutinesApi
fun<T> SharedFlow<T>.collectFromSharedFlow(whenAction: () -> Unit,
                                           then: (T?) -> Unit) {
    val testCoroutineScope = createTestCoroutineScope()
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
