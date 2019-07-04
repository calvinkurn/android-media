package com.tokopedia.home_recom

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Created by Lukas on 2019-07-04
 */

@ExperimentalCoroutinesApi
class MainCoroutineRule(
        val testDispatcher: CoroutineDispatcher
) : TestWatcher() {
    override fun starting(description: Description?) {
        super.starting(description)
    }

    override fun finished(description: Description?) {
        super.finished(description)
    }
}