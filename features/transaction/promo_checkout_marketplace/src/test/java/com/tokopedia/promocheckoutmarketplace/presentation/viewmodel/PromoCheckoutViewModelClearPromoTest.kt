package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequestInvalid
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseSuccess
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import io.mockk.coEvery
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class PromoCheckoutViewModelClearPromoTest : BasePromoCheckoutViewModelTest() {

    private fun mapUiModel(response: ClearPromoResponse): ClearPromoUiModel {
        return ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                        success = response.successData.success,
                        tickerMessage = response.successData.tickerMessage,
                        defaultEmptyPromoMessage = response.successData.defaultEmptyPromoMessage,
                )
        )
    }

    @Test
    fun `WHEN clear promo and success THEN clear promo response action state should not be null`() {
        //given
        val response = provideClearPromoResponseSuccess()

        coEvery { clearCacheAutoApplyUseCase.setParams(any(), any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        //when
        viewModel.clearPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.clearPromoResponse.value)
    }

    @Test
    fun `WHEN clear promo and success THEN clear promo response action state should be success`() {
        //given
        val validateUseRequest = provideApplyPromoGlobalAndMerchantRequestInvalid()
        val response = provideClearPromoResponseSuccess()
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { clearCacheAutoApplyUseCase.setParams(any(), any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(validateUseRequest, ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_SUCCESS)
    }

    @Test
    fun `WHEN clear promo and failed THEN clear promo response action state should not be null`() {
        //given
        val response = provideClearPromoResponseFailed()
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { clearCacheAutoApplyUseCase.setParams(any(), any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.clearPromoResponse.value)
    }

    @Test
    fun `WHEN clear promo and failed THEN clear promo response action state should be error`() {
        //given
        val response = provideClearPromoResponseFailed()
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { clearCacheAutoApplyUseCase.setParams(any(), any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_ERROR)
    }

}