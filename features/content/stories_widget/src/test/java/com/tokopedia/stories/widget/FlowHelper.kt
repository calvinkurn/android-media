package com.tokopedia.stories.widget

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Created by kenny.hadisaputra on 24/08/23
 */
@OptIn(ExperimentalCoroutinesApi::class)
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

fun <T> Flow<T>.createHelper(testScope: TestScope): FlowHelper<T> {
    return FlowHelper(testScope, this)
}
