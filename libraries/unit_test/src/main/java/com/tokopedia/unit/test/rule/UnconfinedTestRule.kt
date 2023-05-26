package com.tokopedia.unit.test.rule

import com.tokopedia.unit.test.dispatcher.UnconfinedTestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class UnconfinedTestRule : TestWatcher(), AbstractTestRule {

    override val dispatchers = UnconfinedTestDispatchers

    override val coroutineDispatcher = dispatchers.coroutineDispatcher

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(coroutineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    @Deprecated("use runTest directly")
    fun runTest(block: suspend TestCoroutineScope.() -> Unit) {
        runBlockingTest {
            block.invoke(this)
        }
    }
}
