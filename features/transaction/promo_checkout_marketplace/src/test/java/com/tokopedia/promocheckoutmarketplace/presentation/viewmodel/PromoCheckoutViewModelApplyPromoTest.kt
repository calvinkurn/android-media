package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoBoResponseFailed
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoEmptyRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalAndMerchantResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalResponseFailed
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoGlobalResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantRequest
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantResponseFailed
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantResponseSuccess
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoMerchantSuccessButGetRedState
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoResponseClashing
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoResponseError
import com.tokopedia.promocheckoutmarketplace.ApplyPromoDataProvider.provideApplyPromoResponseFailed
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentDisabledExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoListWithBoPlusAsRecommendedPromo
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.util.*

class PromoCheckoutViewModelApplyPromoTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN apply promo and get success result THEN apply promo response action is not null`() {
        // given
        val request = provideApplyPromoGlobalAndMerchantRequest()
        val response = provideApplyPromoGlobalAndMerchantResponseSuccess()

        viewModel.setPromoRecommendationValue(
            PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState()
            )
        )

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo and get success result THEN apply promo response action state should be navigate to caller page`() {
        // given
        val request = provideApplyPromoGlobalAndMerchantRequest()
        val response = provideApplyPromoGlobalAndMerchantResponseSuccess()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE)
    }

    @Test
    fun `WHEN apply promo success but have red state on voucher order THEN promo response action state should be error`() {
        // given
        val response = provideApplyPromoMerchantSuccessButGetRedState()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        // then
        assert(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get error result THEN apply promo response action state should be show error`() {
        // given
        val response = provideApplyPromoResponseError()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get global failed result THEN apply promo response action state should be show error`() {
        // given
        val response = provideApplyPromoGlobalResponseFailed()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }
        every { analytics.eventViewErrorAfterClickPakaiPromo(any(), any(), any()) } just Runs

        // when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get merchant failed result THEN apply promo response action state should be show error`() {
        // given
        val response = provideApplyPromoMerchantResponseFailed()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }
        every { analytics.eventViewErrorAfterClickPakaiPromo(any(), any(), any()) } just Runs

        // when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get global and merchant failed result THEN apply promo response action state should be show error`() {
        // given
        val response = provideApplyPromoResponseFailed()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get clashing result THEN apply promo response action state should be show error and reload promo list`() {
        // given
        val response = provideApplyPromoResponseClashing()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO)
    }

    @Test
    fun `WHEN apply promo global and get success result THEN apply promo response action is not null`() {
        // given
        val request = provideApplyPromoGlobalRequest()
        val response = provideApplyPromoGlobalResponseSuccess()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo global from expanded selected promo THEN promo request should contain promo global`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.codes.isNotEmpty())
    }

    @Test
    fun `WHEN apply promo global and promo is disabled and expanded THEN promo request should not contain disabled promo global`() {
        // given
        val request = provideApplyPromoGlobalRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentDisabledExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.codes.isEmpty())
    }

    @Test
    fun `WHEN apply promo global from collapsed selected promo THEN promo request should contain promo global`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.codes.isNotEmpty())
    }

    @Test
    fun `WHEN apply promo global and promo is disabled and collapsed THEN promo request should not contain disabled promo global`() {
        // given
        val request = provideApplyPromoGlobalRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentDisabledExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.codes.isEmpty())
    }

    @Test
    fun `WHEN apply promo merchant and get success result THEN apply promo response action is not null`() {
        // given
        val request = provideApplyPromoMerchantRequest()
        val response = provideApplyPromoMerchantResponseSuccess()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo merchant from expanded selected promo THEN promo request should contain promo merchant`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoMerchantResponseSuccess()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.firstOrNull()?.codes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant and promo is disabled and expanded THEN promo request should not contain disabled promo global`() {
        // given
        val request = provideApplyPromoMerchantRequest()
        val response = provideApplyPromoMerchantResponseSuccess()

        viewModel.setPromoListValue(provideCurrentDisabledExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.firstOrNull()?.codes?.isEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant from collapsed selected promo THEN promo request should contain promo merchant`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoMerchantResponseSuccess()

        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.firstOrNull()?.codes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant and promo is disabled and collapsed THEN promo request should not contain disabled promo global`() {
        // given
        val request = provideApplyPromoMerchantRequest()
        val response = provideApplyPromoMerchantResponseSuccess()
        viewModel.setPromoListValue(provideCurrentDisabledExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.firstOrNull()?.codes?.isEmpty() == true)
    }

    @Test
    fun `WHEN apply promo BO from promo page THEN validate use request should contain bo datas`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoMerchantResponseSuccess()
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val selectedBo = promoList[1] as PromoListItemUiModel
        val selectedBoData = selectedBo.uiData.boAdditionalData.first()
        selectedBo.uiState.isSelected = true
        viewModel.setPromoListValue(promoList)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.first().codes.intersect(selectedBo.uiData.boAdditionalData.map { it.code }).size == 1)
        assert(!request.orders.first().codes.contains(selectedBo.uiData.promoCode))
        assert(request.orders.first().shippingId == selectedBoData.shippingId)
        assert(request.orders.first().spId == selectedBoData.shipperProductId)
        assert(request.orders.first().boCampaignId == selectedBoData.boCampaignId)
        assert(request.orders.first().shippingSubsidy == selectedBoData.shippingSubsidy)
        assert(request.orders.first().benefitClass == selectedBoData.benefitClass)
        assert(request.orders.first().shippingPrice == selectedBoData.shippingPrice)
    }

    @Test
    fun `WHEN reapply promo BO from promo page THEN validate use request should contain shipping id and sp id from bo additional data`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        request.orders.first().codes.add("PLUSAA")
        val response = provideApplyPromoMerchantResponseSuccess()
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val selectedBo = promoList[1] as PromoListItemUiModel
        val boAdditionalDataForCurrentUniqueId = selectedBo.uiData.boAdditionalData.first { it.uniqueId == request.orders.first().uniqueId }
        selectedBo.uiState.isSelected = true
        viewModel.setPromoListValue(promoList)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.first().codes.intersect(selectedBo.uiData.boAdditionalData.map { it.code }).size == 1)
        assert(!request.orders.first().codes.contains(selectedBo.uiData.promoCode))
        assert(request.orders.first().shippingId == boAdditionalDataForCurrentUniqueId.shippingId)
        assert(request.orders.first().spId == boAdditionalDataForCurrentUniqueId.shipperProductId)
        assert(request.orders.first().boCampaignId == boAdditionalDataForCurrentUniqueId.boCampaignId)
        assert(request.orders.first().shippingSubsidy == boAdditionalDataForCurrentUniqueId.shippingSubsidy)
        assert(request.orders.first().benefitClass == boAdditionalDataForCurrentUniqueId.benefitClass)
        assert(request.orders.first().shippingPrice == boAdditionalDataForCurrentUniqueId.shippingPrice)
    }

    @Test
    fun `WHEN unapply promo BO THEN validate use request should not contain bo promo code`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        request.orders.first().codes.add("PLUSAA")
        val response = provideApplyPromoMerchantResponseSuccess()
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val selectedBo = promoList[1] as PromoListItemUiModel
        selectedBo.uiState.isSelected = false
        viewModel.setPromoListValue(promoList)

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(request.orders.first().codes.intersect(selectedBo.uiData.boAdditionalData.map { it.code }).isEmpty())
        assert(!request.orders.first().codes.contains(selectedBo.uiData.promoCode))
    }

    @Test
    fun `WHEN apply promo BO failed THEN should see error code based on BO code in bo additional datas`() {
        // given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoBoResponseFailed()
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val selectedBo = promoList[1] as PromoListItemUiModel
        selectedBo.uiState.isSelected = true
        viewModel.setPromoListValue(promoList)

        every { analytics.eventViewErrorAfterClickPakaiPromo(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        // when
        viewModel.applyPromo(request, ArrayList())

        // then
        assert(selectedBo.uiData.errorMessage.isNotEmpty())
    }
}
