package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoBoRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequestInvalid
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseSuccess
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoListWithBoPlusAsRecommendedPromo
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import io.mockk.coEvery
import org.junit.Assert.assertNotNull
import org.junit.Test

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

        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        //when
        viewModel.clearPromo(PromoRequest(), ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.clearPromoResponse.value)
    }

    @Test
    fun `WHEN clear promo and success THEN clear promo response action state should be success`() {
        //given
        val validateUseRequest = provideApplyPromoGlobalAndMerchantRequestInvalid()
        val response = provideClearPromoResponseSuccess()
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), validateUseRequest, ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_SUCCESS)
    }

    @Test
    fun `WHEN clear promo and failed THEN clear promo response action state should not be null`() {
        //given
        val response = provideClearPromoResponseFailed()
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.clearPromoResponse.value)
    }

    @Test
    fun `WHEN clear promo and failed THEN clear promo response action state should be error`() {
        //given
        val response = provideClearPromoResponseFailed()
        viewModel.setPromoListValue(provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData())

        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), ValidateUsePromoRequest(), ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_ERROR)
    }

    @Test
    fun `WHEN clear promo and show BO promo THEN should include real BO promo code instead of hard coded BO promo code`() {
        // given
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val validateUseRequest = provideApplyPromoBoRequest()
        val promoBo = "PLUSAA"
        val response = provideClearPromoResponseSuccess()
        viewModel.setPromoListValue(promoList)
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), validateUseRequest, ArrayList())

        //then
        assert(!validateUseRequest.orders[0].codes.contains(promoBo))

    }

    @Test
    fun `WHEN clear promo and show global promo THEN should include in global codes clear promo param`() {
        // given
        val promoList = provideCurrentSelectedExpandedGlobalPromoData()
        val response = provideClearPromoResponseSuccess()
        val clearPromoParam = ClearPromoRequest()
        viewModel.setPromoListValue(promoList)
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), ValidateUsePromoRequest(), ArrayList(), clearPromoParam)

        //then
        assert(clearPromoParam.orderData.codes.isNotEmpty())
        assert(clearPromoParam.orderData.orders.isEmpty())
    }

    @Test
    fun `WHEN clear promo THEN should include global promo in apply promo param to clear promo param`() {
        // given
        val applyPromoParam = provideApplyPromoGlobalRequest()
        val response = provideClearPromoResponseSuccess()
        val clearPromoParam = ClearPromoRequest()
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), applyPromoParam, ArrayList(), clearPromoParam)

        //then
        assert(clearPromoParam.orderData.codes.isNotEmpty())
    }

    @Test
    fun `WHEN clear promo THEN should include order promo from apply promo param to clear promo param`() {
        // given
        val applyPromoParam = provideApplyPromoMerchantRequest()
        val response = provideClearPromoResponseSuccess()
        val clearPromoParam = ClearPromoRequest()
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), applyPromoParam, ArrayList(), clearPromoParam)

        //then
        assert(clearPromoParam.orderData.orders.isNotEmpty())
        assert(clearPromoParam.orderData.codes.isEmpty())
    }

    @Test
    fun `WHEN clear promo and show merchant promo THEN should include in order code clear promo param`() {
        // given
        val promoList = provideCurrentSelectedExpandedMerchantPromoData()
        val applyPromoParam = provideApplyPromoMerchantRequest()
        val response = provideClearPromoResponseSuccess()
        val clearPromoParam = ClearPromoRequest()
        viewModel.setPromoListValue(promoList)
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), applyPromoParam, ArrayList(), clearPromoParam)

        //then
        assert(clearPromoParam.orderData.codes.isEmpty())
        assert(clearPromoParam.orderData.orders.isNotEmpty())
    }

    @Test
    fun `WHEN clear promo and order not in promo list nor in apply promo param THEN should include order code in get promo param to clear promo param`() {
        // given
        val promoList = provideCurrentSelectedExpandedMerchantPromoData()
        val response = provideClearPromoResponseSuccess()
        val clearPromoParam = ClearPromoRequest()
        val getPromoParam = provideGetPromoListRequest()
        viewModel.setPromoListValue(promoList)
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(getPromoParam, ValidateUsePromoRequest(), ArrayList(), clearPromoParam)

        //then
        assert(clearPromoParam.orderData.codes.isEmpty())
        assert(clearPromoParam.orderData.orders.isNotEmpty())
    }

}