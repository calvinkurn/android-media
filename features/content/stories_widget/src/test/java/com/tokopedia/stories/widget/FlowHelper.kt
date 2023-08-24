@file:OptIn(ExperimentalCoroutinesApi::class)

package com.tokopedia.stories.widget

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle

/**
 * Created by kenny.hadisaputra on 24/08/23
 */
class FlowHelper<T>(testScope: TestScope, flow: Flow<T>) {

    private val _values = mutableListOf<T>()
    val values get() = _values.toList()
    init {
        testScope.backgroundScope.launch(UnconfinedTestDispatcher(testScope.testScheduler)) {
            flow.toList(_values)
        }
    }

    fun run(fn: () -> Unit) = chain {
        fn()
    }

    fun onValues(fn: (List<T>) -> Unit) = chain {
        fn(values)
    }

    fun clearValues() = chain {
        _values.clear()
    }

    private fun chain(fn: () -> Unit): FlowHelper<T> {
        fn()
        return this
    }
}

context(TestScope)
fun <T> Flow<T>.createHelper(): FlowHelper<T> {
    return FlowHelper(this@TestScope, this@Flow)
}

context(TestScope)
fun Any?.waitUntilIdle() = this.also { advanceUntilIdle() }
