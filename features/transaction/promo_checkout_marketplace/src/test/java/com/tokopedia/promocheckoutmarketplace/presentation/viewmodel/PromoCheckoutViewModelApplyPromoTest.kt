package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

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
        //given
        val request = provideApplyPromoGlobalAndMerchantRequest()
        val response = provideApplyPromoGlobalAndMerchantResponseSuccess()

        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo and get success result THEN apply promo response action state should be navigate to caller page`() {
        //given
        val request = provideApplyPromoGlobalAndMerchantRequest()
        val response = provideApplyPromoGlobalAndMerchantResponseSuccess()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_NAVIGATE_TO_CALLER_PAGE)
    }

    @Test
    fun `WHEN apply promo success but have red state on voucher order THEN promo response action state should be error`() {
        //given
        val response = provideApplyPromoMerchantSuccessButGetRedState()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assert(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get error result THEN apply promo response action state should be show error`() {
        //given
        val response = provideApplyPromoResponseError()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get global failed result THEN apply promo response action state should be show error`() {
        //given
        val response = provideApplyPromoGlobalResponseFailed()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }
        every { analytics.eventViewErrorAfterClickPakaiPromo(any(), any(), any()) } just Runs

        //when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get merchant failed result THEN apply promo response action state should be show error`() {
        //given
        val response = provideApplyPromoMerchantResponseFailed()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }
        every { analytics.eventViewErrorAfterClickPakaiPromo(any(), any(), any()) } just Runs

        //when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get global and merchant failed result THEN apply promo response action state should be show error`() {
        //given
        val response = provideApplyPromoResponseFailed()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply promo and get clashing result THEN apply promo response action state should be show error and reload promo list`() {
        //given
        val response = provideApplyPromoResponseClashing()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(ValidateUsePromoRequest(), ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value?.state == ApplyPromoResponseAction.ACTION_SHOW_TOAST_AND_RELOAD_PROMO)
    }

    @Test
    fun `WHEN apply promo global and get success result THEN apply promo response action is not null`() {
        //given
        val request = provideApplyPromoGlobalRequest()
        val response = provideApplyPromoGlobalResponseSuccess()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo global from expanded selected promo THEN promo request should contain promo global`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.codes.isNotEmpty())
    }

    @Test
    fun `WHEN apply promo global and promo is disabled and expanded THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoGlobalRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentDisabledExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.codes.isEmpty())
    }

    @Test
    fun `WHEN apply promo global from collapsed selected promo THEN promo request should contain promo global`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.codes.isNotEmpty())
    }

    @Test
    fun `WHEN apply promo global and promo is disabled and collapsed THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoGlobalRequest()
        val response = provideApplyPromoGlobalResponseSuccess()
        viewModel.setPromoListValue(provideCurrentDisabledExpandedGlobalPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.codes.isEmpty())
    }

    @Test
    fun `WHEN apply promo merchant and get success result THEN apply promo response action is not null`() {
        //given
        val request = provideApplyPromoMerchantRequest()
        val response = provideApplyPromoMerchantResponseSuccess()

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assertNotNull(viewModel.applyPromoResponseAction.value)
    }

    @Test
    fun `WHEN apply promo merchant from expanded selected promo THEN promo request should contain promo merchant`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoMerchantResponseSuccess()
        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.orders.firstOrNull()?.codes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant and promo is disabled and expanded THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoMerchantRequest()
        val response = provideApplyPromoMerchantResponseSuccess()

        viewModel.setPromoListValue(provideCurrentDisabledExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.orders.firstOrNull()?.codes?.isEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant from collapsed selected promo THEN promo request should contain promo merchant`() {
        //given
        val request = provideApplyPromoEmptyRequest()
        val response = provideApplyPromoMerchantResponseSuccess()

        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.orders.firstOrNull()?.codes?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN apply promo merchant and promo is disabled and collapsed THEN promo request should not contain disabled promo global`() {
        //given
        val request = provideApplyPromoMerchantRequest()
        val response = provideApplyPromoMerchantResponseSuccess()
        viewModel.setPromoListValue(provideCurrentDisabledExpandedMerchantPromoData())

        every { analytics.eventClickPakaiPromoSuccess(any(), any(), any()) } just Runs
        coEvery { validateUseUseCase.setParam(any()) } returns validateUseUseCase
        coEvery { validateUseUseCase.execute(any(), any()) } answers {
            firstArg<(ValidateUsePromoRevampUiModel) -> Unit>().invoke(ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(response.validateUsePromoRevamp))
        }

        //when
        viewModel.applyPromo(request, ArrayList())

        //then
        assert(request.orders.firstOrNull()?.codes?.isEmpty() == true)
    }

}