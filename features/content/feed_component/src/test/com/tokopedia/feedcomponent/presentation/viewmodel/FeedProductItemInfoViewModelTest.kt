package com.tokopedia.feedcomponent.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.mvcwidget.ResultStatus
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummary
import com.tokopedia.mvcwidget.TokopointsCatalogMVCSummaryResponse
import com.tokopedia.mvcwidget.usecases.MVCSummaryUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by Muhammad Furqan on 01/11/22
 */
class FeedProductItemInfoViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val usecase: MVCSummaryUseCase = mockk()
    private val dispatcherProvider: CoroutineDispatchers = CoroutineTestDispatchersProvider

    private lateinit var viewModel: FeedProductItemInfoViewModel

    @Before
    fun setUp() {
        viewModel = FeedProductItemInfoViewModel(usecase, dispatcherProvider)
    }

    @Test
    fun fetchMerchantVoucherSummary_whenFailed_shouldReturnFail() {
        // given
        val dummyErrorData = TokopointsCatalogMVCSummaryResponse(
            data = TokopointsCatalogMVCSummary(
                resultStatus = ResultStatus(
                    code = "404",
                    message = listOf("Failed to fetch"),
                    status = "failed",
                    reason = "failed"
                ),
                isShown = false,
                counterTotal = null,
                animatedInfoList = null
            )
        )
        coEvery { usecase.getQueryParams(any()) } returns hashMapOf()
        coEvery {
            usecase.getResponse(any())
        } returns dummyErrorData

        // when
        viewModel.fetchMerchantVoucherSummary("12345")

        // then
        assert(viewModel.merchantVoucherSummary.value is Fail)
        assert((viewModel.merchantVoucherSummary.value as Fail).throwable is ResponseErrorException)
        assertEquals(
            ((viewModel.merchantVoucherSummary.value as Fail).throwable as ResponseErrorException).message,
            "Failed to fetch"
        )
    }

    @Test
    fun fetchMerchantVoucherSummary_whenSuccess_shouldReturnCorrectValue() {
        // given
        val dummyData = TokopointsCatalogMVCSummaryResponse(
            data = TokopointsCatalogMVCSummary(
                resultStatus = ResultStatus(
                    code = "200",
                    message = listOf(""),
                    status = "success",
                    reason = "success"
                ),
                isShown = false,
                counterTotal = 10,
                animatedInfoList = listOf()
            )
        )
        coEvery { usecase.getQueryParams(any()) } returns hashMapOf()
        coEvery {
            usecase.getResponse(any())
        } returns dummyData

        // when
        viewModel.fetchMerchantVoucherSummary("12345")

        // then
        assert(viewModel.merchantVoucherSummary.value is Success)
        assertEquals((viewModel.merchantVoucherSummary.value as Success).data, dummyData.data)
    }
}
