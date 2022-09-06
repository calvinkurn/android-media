package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoBoRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoEmptyRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequestInvalid
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.ClearPromoDataProvider.provideClearPromoResponseSuccess
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideExpandedMerchantParentNotEligiblePromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoListGlobalParentNotEnabled
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoListWithBoPlusAsRecommendedPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithBoPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedMerchantPromo
import com.tokopedia.promocheckoutmarketplace.data.response.ClearPromoResponse
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.clear.ClearPromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import io.mockk.coEvery
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.lang.Exception

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

    // todo duplicate
//    @Test
//    fun `WHEN clear promo and show BO promo THEN should not clear BO promo in validate use request`() {
//        // given
//        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
//        val validateUseRequest = provideApplyPromoBoRequest()
//        val promoBo = "PLUSAA"
//        val response = provideClearPromoResponseSuccess()
//        viewModel.setPromoListValue(promoList)
//        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
//        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
//            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
//        }
//
//        //when
//        viewModel.clearPromo(PromoRequest(), validateUseRequest, ArrayList(), ClearPromoRequest())
//
//        //then
//        assert(!validateUseRequest.orders[0].codes.contains(promoBo))
//
//    }

    @Test
    fun `WHEN show BO promo and BO applied in previous page THEN should add BO promo code in clear promo order param`() {
        // given
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val validateUseRequest = provideApplyPromoEmptyRequest()
        val promoBo = "PLUSAA"
        val bboAppliedFromPreviousPage = arrayListOf(promoBo)
        val response = provideClearPromoResponseSuccess()
        val clearPromoRequest = ClearPromoRequest()
        viewModel.setPromoListValue(promoList)
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), validateUseRequest, bboAppliedFromPreviousPage, clearPromoRequest)

        //then
        assert(clearPromoRequest.orderData.orders[0].codes.contains(promoBo))
        assert(!validateUseRequest.orders[0].codes.contains(promoBo))
    }

    @Test
    fun `WHEN show BO promo and bo not applied in previous page THEN should not add BO promo code in clear promo order param`() {
        // given
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val validateUseRequest = provideApplyPromoEmptyRequest()
        val promoBo = "PLUSAA"
        val bboAppliedFromPreviousPage = arrayListOf<String>()
        val response = provideClearPromoResponseSuccess()
        val clearPromoRequest = ClearPromoRequest()
        viewModel.setPromoListValue(promoList)
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
        }

        //when
        viewModel.clearPromo(PromoRequest(), validateUseRequest, bboAppliedFromPreviousPage, clearPromoRequest)

        //then
        assert(!clearPromoRequest.orderData.orders[0].codes.contains(promoBo))
    }

//    @Test
//    fun `WHEN show BO promo and no same unique id in order clear param THEN should add new order clear param from get promo request`() {
//        // given
//        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
//        val promoParam = providePromoRequestWithBoPromo()
//        val validateUseRequest = provideApplyPromoEmptyRequest()
//        validateUseRequest.orders = emptyList()
//        val promoBo = "PLUSAA"
//        val response = provideClearPromoResponseSuccess()
//        val clearPromoRequest = ClearPromoRequest()
//        viewModel.setPromoListValue(promoList)
//
//        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
//        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
//            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
//        }
//
//        //when
//        viewModel.clearPromo(promoParam, validateUseRequest, ArrayList(), clearPromoRequest)
//
//        //then
//        assert(clearPromoRequest.orderData.orders[0].codes.contains(promoBo))
//    }

    @Test
    fun `WHEN clear promo and global promo attempted THEN should include in global codes clear promo param`() {
        // given
        val promoList = provideCurrentSelectedExpandedGlobalPromoData()
        val promoAttempted = promoList[0] as PromoListItemUiModel
        promoAttempted.uiState.isAttempted = true
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
    fun `WHEN clear promo and show ineligible global promo THEN should not include in global codes clear promo param`() {
        // given
        val promoList = providePromoListGlobalParentNotEnabled()
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
        assert(clearPromoParam.orderData.codes.isEmpty())
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
    fun `WHEN clear promo and merchant promo is attempted THEN should include in order code clear promo param`() {
        // given
        val promoList = provideCurrentSelectedExpandedMerchantPromoData()
        val merchantPromo = promoList[0] as PromoListItemUiModel
        merchantPromo.uiState.isAttempted = true
        val applyPromoParam = provideApplyPromoEmptyRequest()
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
    fun `WHEN clear promo and show parent not enabled merchant promo THEN should not include in order code clear promo param`() {
        // given
        val promoList = provideExpandedMerchantParentNotEligiblePromoData()
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
        assert(clearPromoParam.orderData.orders.isEmpty())
    }

//    @Test
//    fun `WHEN clear promo and order not in promo list nor in apply promo param THEN should include order code in get promo param to clear promo param`() {
//        // given
//        val promoList = provideCurrentSelectedExpandedMerchantPromoData()
//        val response = provideClearPromoResponseSuccess()
//        val clearPromoParam = ClearPromoRequest()
//        val getPromoParam = provideGetPromoListRequest()
//        viewModel.setPromoListValue(promoList)
//        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
//        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
//            firstArg<(ClearPromoUiModel) -> Unit>().invoke(mapUiModel(response))
//        }
//
//        //when
//        viewModel.clearPromo(getPromoParam, ValidateUsePromoRequest(), ArrayList(), clearPromoParam)
//
//        //then
//        assert(clearPromoParam.orderData.codes.isEmpty())
//        assert(clearPromoParam.orderData.orders.isNotEmpty())
//    }

    @Test
    fun `WHEN clear promo error THEN should set state to error state`() {
        // given
        coEvery { clearCacheAutoApplyUseCase.setParams(any()) } returns clearCacheAutoApplyUseCase
        coEvery { clearCacheAutoApplyUseCase.execute(any(), any()) } answers {
            secondArg<(Exception) -> Unit>().invoke(Exception())
        }

        //when
        viewModel.clearPromo(PromoRequest(), ValidateUsePromoRequest(), ArrayList())

        //then
        assert(viewModel.clearPromoResponse.value?.state == ClearPromoResponseAction.ACTION_STATE_ERROR)
    }

}