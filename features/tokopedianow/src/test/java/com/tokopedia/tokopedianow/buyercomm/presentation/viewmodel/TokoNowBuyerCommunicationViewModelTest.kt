package com.tokopedia.tokopedianow.buyercomm.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.tokopedianow.buyercomm.domain.model.GetBuyerCommunication.GetBuyerCommunicationResponse
import com.tokopedia.tokopedianow.buyercomm.domain.usecase.GetBuyerCommunicationUseCase
import com.tokopedia.tokopedianow.buyercomm.mapper.BuyerCommunicationMapper
import com.tokopedia.tokopedianow.buyercomm.presentation.data.BuyerCommunicationData
import com.tokopedia.tokopedianow.common.util.TokoNowLocalAddress
import com.tokopedia.tokopedianow.data.BuyerCommunicationDataFactory.createBuyerCommunicationResponse
import com.tokopedia.unit.test.ext.verifyValueEquals
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TokoNowBuyerCommunicationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private lateinit var getBuyerCommunicationUseCase: GetBuyerCommunicationUseCase
    private lateinit var addressData: TokoNowLocalAddress

    private lateinit var viewModel: TokoNowBuyerCommunicationViewModel

    @Before
    fun setUp() {
        getBuyerCommunicationUseCase = mockk(relaxed = true)
        addressData = mockk(relaxed = true)

        viewModel = TokoNowBuyerCommunicationViewModel(
            getBuyerCommunicationUseCase,
            addressData,
            coroutineTestRule.dispatchers
        )
    }

    @Test
    fun `given buyer communication data is NOT null when onViewCreated called should update live data`() {
        val buyerCommunicationData = BuyerCommunicationData()

        viewModel.onViewCreated(data = buyerCommunicationData)

        viewModel.buyerCommunicationData
            .verifyValueEquals(buyerCommunicationData)
    }

    @Test
    fun `given buyer communication data is null when onViewCreated called should call use case and update live data`() {
        val response = createBuyerCommunicationResponse()

        onGetBuyerRecommendation_thenReturn(response)

        viewModel.onViewCreated(data = null)

        val expectedBuyerCommunicationData = BuyerCommunicationMapper
            .mapToBuyerCommunicationData(response)

        viewModel.buyerCommunicationData
            .verifyValueEquals(expectedBuyerCommunicationData)
    }

    @Test
    fun `given get buyer communication throws error when onViewCreated called should NOT update live data`() {
        onGetBuyerRecommendation_thenReturn(NullPointerException())

        viewModel.onViewCreated(data = null)

        viewModel.buyerCommunicationData
            .verifyValueEquals(null)
    }

    private fun onGetBuyerRecommendation_thenReturn(response: GetBuyerCommunicationResponse) {
        coEvery { getBuyerCommunicationUseCase.execute(addressData) } returns response
    }

    private fun onGetBuyerRecommendation_thenReturn(error: Throwable) {
        coEvery { getBuyerCommunicationUseCase.execute(addressData) } throws error
    }
}
