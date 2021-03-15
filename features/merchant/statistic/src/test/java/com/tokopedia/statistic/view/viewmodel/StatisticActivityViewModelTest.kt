package com.tokopedia.statistic.view.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.statistic.domain.usecase.CheckWhitelistedStatusUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By @ilhamsuaib on 19/02/21
 */

class StatisticActivityViewModelTest {

    @RelaxedMockK
    lateinit var checkWhitelistedStatusUseCase: CheckWhitelistedStatusUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: StatisticActivityViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = StatisticActivityViewModel(checkWhitelistedStatusUseCase, CoroutineTestDispatchersProvider)
    }

    @Test
    fun `should success when checking user white list status`() = runBlocking {
        val whiteListName = "page-name"
        checkWhitelistedStatusUseCase.params = CheckWhitelistedStatusUseCase.createParam(whiteListName)

        coEvery {
            checkWhitelistedStatusUseCase.executeOnBackground()
        } returns true

        viewModel.checkWhiteListStatus()

        coVerify {
            checkWhitelistedStatusUseCase.executeOnBackground()
        }

        assert(viewModel.whitelistedStatus.value == Success(true))
    }

    @Test
    fun `should return throwable when when checking user white list status `() = runBlocking {
        val whiteListName = "page-name"
        checkWhitelistedStatusUseCase.params = CheckWhitelistedStatusUseCase.createParam(whiteListName)

        val exception = MessageErrorException("error message")

        coEvery {
            checkWhitelistedStatusUseCase.executeOnBackground()
        } throws exception

        viewModel.checkWhiteListStatus()

        coVerify {
            checkWhitelistedStatusUseCase.executeOnBackground()
        }

        assert(viewModel.whitelistedStatus.value is Fail)
    }
}