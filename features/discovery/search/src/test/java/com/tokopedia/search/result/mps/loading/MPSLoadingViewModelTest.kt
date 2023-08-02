package com.tokopedia.search.result.mps.loading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MPSLoadingViewModelTest {

    private val mpsLoadingConfig = MPSLoadingConfig(
        delay = 10L,
        randomIncrement = { 15 },
        pauseProgressThreshold = 35,
    )
    private fun CoroutineScope.MPSLoadingViewModel(
        initialState: MPSLoadingState = MPSLoadingState()
    ) = MPSLoadingViewModel(initialState, this, mpsLoadingConfig)

    @Test
    fun `start will keep increment loading value after every delay until pause threshold`() = runBlockingTest {
        val loadingValues = mutableListOf<Int>()
        val mpsLoadingViewModel = MPSLoadingViewModel()

        val job = mpsLoadingViewModel.onEach(
            this,
            MPSLoadingState::loadingValue,
            loadingValues::add
        )

        mpsLoadingViewModel.start()
        testScheduler.apply { advanceTimeBy(mpsLoadingConfig.delay * 2); runCurrent() }

        assertThat(loadingValues, `is`(
            listOf(
                MPSLoadingState().loadingValue,
                mpsLoadingConfig.randomIncrement() * 1,
                mpsLoadingConfig.randomIncrement() * 2,
            )
        ))

        job.cancel()
    }

    @Test
    fun `loading value will go to 100 when finished`() = runBlockingTest {
        val initialState = MPSLoadingState(loadingValue = 50)
        val mpsLoadingViewModel = MPSLoadingViewModel(initialState)

        mpsLoadingViewModel.finish()

        assertThat(mpsLoadingViewModel.stateFlow.value, `is`(MPSLoadingState(loadingValue = 100)))
    }
}
