package com.tokopedia.shopdiscount.info.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.shopdiscount.bulk.data.response.GetSlashPriceBenefitResponse
import com.tokopedia.shopdiscount.bulk.domain.usecase.GetSlashPriceBenefitUseCase
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.info.data.response.GetSlashPriceTickerResponse
import com.tokopedia.shopdiscount.info.domain.usecase.GetSlashPriceTickerUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShopDiscountSellerInfoBottomSheetViewModelTest {

    @RelaxedMockK
    lateinit var getSlashPriceBenefitUseCase: GetSlashPriceBenefitUseCase

    @RelaxedMockK
    lateinit var getSlashPriceTickerUseCase: GetSlashPriceTickerUseCase

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockErrorMessage = "Error"
    private val mockListTickerMessage = listOf("Ticker1", "Ticker2")

    private val viewModel by lazy {
        ShopDiscountSellerInfoBottomSheetViewModel(
            CoroutineTestDispatchersProvider,
            getSlashPriceBenefitUseCase,
            getSlashPriceTickerUseCase
        )
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `When success get seller info benefit data, should return success result and matched with mock data`() {
        coEvery {
            getSlashPriceBenefitUseCase.executeOnBackground()
        } returns getGetSlashPriceBenefitMockSuccessResponse()
        viewModel.getSellerInfoBenefitData()
        val liveDataValue = viewModel.sellerInfoLiveData.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.isUseVps)
        assert(liveDataSuccessValue.data.responseHeader.success)
    }

    private fun getGetSlashPriceBenefitMockSuccessResponse(): GetSlashPriceBenefitResponse {
        return GetSlashPriceBenefitResponse(
            GetSlashPriceBenefitResponse.GetSlashPriceBenefit(
                isUseVps = true,
                responseHeader = ResponseHeader(
                    success = true
                )
            )
        )
    }

    @Test
    fun `When error get seller info benefit data, should return fail result`() {
        coEvery {
            getSlashPriceBenefitUseCase.executeOnBackground()
        } throws Exception(mockErrorMessage)
        viewModel.getSellerInfoBenefitData()
        val liveDataValue = viewModel.sellerInfoLiveData.value
        assert(liveDataValue is Fail)
        val liveDataFailValue = liveDataValue as Fail
        assert(liveDataFailValue.throwable.message == mockErrorMessage)
    }

    @Test
    fun `When success get ticker data, should return success result and matched with mock data`() {
        coEvery {
            getSlashPriceTickerUseCase.executeOnBackground()
        } returns getGetSlashPriceTickerMockSuccessResponse()
        viewModel.getTickerData()
        val liveDataValue = viewModel.slashPriceTickerLiveData.value
        assert(liveDataValue is Success)
        val liveDataSuccessValue = liveDataValue as Success
        assert(liveDataSuccessValue.data.responseHeader.success)
        assert(liveDataSuccessValue.data.listTicker.size == mockListTickerMessage.size)
    }

    private fun getGetSlashPriceTickerMockSuccessResponse(): GetSlashPriceTickerResponse {
        return GetSlashPriceTickerResponse(
            GetSlashPriceTickerResponse.GetSlashPriceTicker(
                responseHeader = ResponseHeader(success = true),
                listTicker = mockListTickerMessage
            )
        )
    }

    @Test
    fun `When error get ticker data, should return fail result`() {
        coEvery {
            getSlashPriceTickerUseCase.executeOnBackground()
        } throws Exception(mockErrorMessage)
        viewModel.getTickerData()
        val liveDataValue = viewModel.slashPriceTickerLiveData.value
        assert(liveDataValue is Fail)
        val liveDataFailValue = liveDataValue as Fail
        assert(liveDataFailValue.throwable.message == mockErrorMessage)
    }

}