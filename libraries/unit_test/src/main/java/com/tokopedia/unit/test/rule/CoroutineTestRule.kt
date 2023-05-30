package com.tokopedia.unit.test.rule

import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
@Deprecated("Use StandardTestRule or UnconfinedTestRule")
//If you care about testing the order of coroutine execution, use a Standard test dispatcher
//If you have particularly tricky coroutine code, where you need fine control over what is launched and when, use a Standard test dispatcher
//Otherwise, give Unconfined test dispatcher a go as it can simplify every test you write and make them more concise
class CoroutineTestRule : TestWatcher(), AbstractTestRule {

    override val dispatchers = CoroutineTestDispatchers

    override val coroutineDispatcher = dispatchers.coroutineDispatcher

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(coroutineDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    @Deprecated("use runTest")
    fun runTest(block: suspend TestCoroutineScope.() -> Unit) {
        runBlockingTest {
            block.invoke(this)
        }
    }
}
