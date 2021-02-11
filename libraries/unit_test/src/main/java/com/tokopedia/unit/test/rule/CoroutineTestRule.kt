package com.tokopedia.unit.test.rule

import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineTestRule: TestWatcher() {

    val dispatchers = CoroutineTestDispatchers

    private val coroutineDispatcher = dispatchers.coroutineDispatcher

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(coroutineDispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
        coroutineDispatcher.cleanupTestCoroutines()
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) {
        coroutineDispatcher.runBlockingTest {
            block.invoke(this)
        }
    }
}