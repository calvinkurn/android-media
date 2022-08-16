package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Test

class PromoCheckoutViewModelUICallbackTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN has any promo selected THEN should return true`() {
        // GIVEN
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())

        // WHEN
        val hasAnyPromoSelected = viewModel.isHasAnySelectedPromoItem()

        // THEN
        assert(hasAnyPromoSelected)
    }

    @Test
    fun `WHEN call reset promo THEN should have expanded promo items`() {
        // GIVEN
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickResetPromo(any()) } just Runs

        // WHEN
        viewModel.resetPromo()

        // THEN
        assert(viewModel.promoListUiModel.value?.size ?: 0 > 0)
    }

    @Test
    fun `WHEN call reset promo THEN fragment state should be no selected promo`() {
        // GIVEN
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickResetPromo(any()) } just Runs

        // WHEN
        viewModel.resetPromo()

        // THEN
        assert(viewModel.fragmentUiModel.value?.uiState?.hasAnyPromoSelected == false)
    }

    @Test
    fun `WHEN call reset promo THEN promo recommendation should be enabled`() {
        // GIVEN
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickResetPromo(any()) } just Runs

        // WHEN
        viewModel.resetPromo()

        // THEN
        assert(viewModel.promoRecommendationUiModel.value?.uiState?.isButtonSelectEnabled == true)
    }

    @Test
    fun `WHEN click unselected attempted promo item THEN should become selected`() {
        // GIVEN
        val data = GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isAttempted = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert((viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
    }

    @Test
    fun `WHEN click unselected BO clashing promo item THEN should show ticker BO clashing`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoListWithClashingBoPromo()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert((viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasSelectedBoClashingPromo == true)
        assert(viewModel.fragmentUiModel.value?.uiData?.boClashingMessage == promoListItemUiModel.uiData.boClashingInfos.first().message)
        assert(viewModel.fragmentUiModel.value?.uiData?.boClashingImage == promoListItemUiModel.uiData.boClashingInfos.first().icon)
    }

    @Test
    fun `WHEN click selected BO clashing promo item THEN should unshow ticker BO clashing`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoListWithClashingBoPromo()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isSelected = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert(!(viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasSelectedBoClashingPromo == false)
    }

    @Test
    fun `WHEN click unselected attempted promo item THEN sibling promo should become unselected`() {
        // GIVEN
        val data = GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        val secondPromoListItemUiModel = data[2] as PromoListItemUiModel
        secondPromoListItemUiModel.uiState.isRecommended = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert(!((viewModel.promoListUiModel.value?.get(2) as PromoListItemUiModel).uiState.isSelected))
    }

    @Test
    fun `WHEN click selected attempted promo item THEN should become unselected`() {
        // GIVEN
        val data = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isAttempted = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert(!(viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
    }

    @Test
    fun `WHEN click selected attempted clashing BO promo item THEN should unshow ticker BO clashing`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoListWithClashingBoPromo()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isSelected = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert(!(viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasSelectedBoClashingPromo == false)
    }

    @Test
    fun `WHEN click selected recommended promo item THEN recommendation should be resetted`() {
        // GIVEN
        val data = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isRecommended = true
        viewModel.setPromoListValue(data)
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        // THEN
        assert(viewModel.promoRecommendationUiModel.value?.uiState?.isButtonSelectEnabled == true)
    }

    @Test
    fun `WHEN apply recommended promo of expanded item THEN should be applied`() {
        // GIVEN
        val data = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoDataWithHeader()
        viewModel.setPromoListValue(data)
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickPilihPromoRecommendation(any(), any()) } just Runs
        every { analytics.eventClickPilihOnRecommendation(any(), any(), any()) } just Runs

        // WHEN
        viewModel.applyRecommendedPromo()

        // THEN
        assert(viewModel.promoRecommendationUiModel.value?.uiState?.isButtonSelectEnabled == false)
    }

    @Test
    fun `WHEN apply recommended promo of BO promo item THEN should get promo code from additional bo datas`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoListWithBoPlusAsRecommendedPromo()
        viewModel.setPromoListValue(data)
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
            uiData = PromoRecommendationUiModel.UiData().apply {
                promoCodes = listOf(
                    "PLUSAA"
                )
            },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickPilihPromoRecommendation(any(), any()) } just Runs
        every { analytics.eventClickPilihOnRecommendation(any(), any(), any()) } just Runs

        // WHEN
        viewModel.applyRecommendedPromo()

        // THEN
        assert((viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
    }

    @Test
    fun `WHEN apply recommended promo THEN sibling coupon need to be unselected`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoListWithClashingSectionRecommendedPromo()
        val preSelectedPromo = data[1] as PromoListItemUiModel
        preSelectedPromo.uiState.isSelected = true
        viewModel.setPromoListValue(data)
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
            uiData = PromoRecommendationUiModel.UiData().apply {
                promoCodes = listOf(
                    "PROMO1"
                )
            },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickPilihPromoRecommendation(any(), any()) } just Runs
        every { analytics.eventClickPilihOnRecommendation(any(), any(), any()) } just Runs

        // WHEN
        viewModel.applyRecommendedPromo()

        // THEN
        assert(!(viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
        assert((viewModel.promoListUiModel.value?.get(2) as PromoListItemUiModel).uiState.isSelected)
    }

    @Test
    fun `WHEN update state before apply manual input promo THEN state should be loading`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoInputStateBeforeApplyPromo("code", false)

        // THEN
        assert(viewModel.promoInputUiModel.value?.uiState?.isLoading == true)
    }

    @Test
    fun `WHEN update state before apply manual input promo THEN promo code should not be empty`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoInputStateBeforeApplyPromo("code", false)

        // THEN
        assert(viewModel.promoInputUiModel.value?.uiData?.promoCode?.isNotBlank() == true)
    }

    @Test
    fun `WHEN reset promo manual input state THEN state should be not loading`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        // WHEN
        viewModel.resetPromoInput()

        // THEN
        assert(viewModel.promoInputUiModel.value?.uiState?.isLoading == false)
    }

    @Test
    fun `WHEN reset promo manual input state THEN promo code should be empty`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        // WHEN
        viewModel.resetPromoInput()

        // THEN
        assert(viewModel.promoInputUiModel.value?.uiData?.promoCode.isNullOrEmpty())
    }

    @Test
    fun `WHEN set promo input code from last apply THEN promo code should not be empty`() {
        // GIVEN
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickPromoLastSeenItem(any(), any()) } just Runs

        // WHEN
        viewModel.setPromoInputFromLastApply("code")

        // THEN
        assert(viewModel.promoInputUiModel.value?.uiData?.promoCode?.isBlank() == false)
    }

    @Test
    fun `WHEN pre applied promo code is empty THEN should has no different pre applied state`() {
        // GIVEN
        val data = GetPromoListDataProvider.provideFragmentData()
        viewModel.setFragmentUiModelValue(data)

        // WHEN
        val hasDifferentPreAppliedState = viewModel.hasDifferentPreAppliedState()

        // THEN
        assert(!hasDifferentPreAppliedState)
    }

    @Test
    fun `WHEN has any promo item unchecked but exist as pre applied promo item THEN should has different pre applied state`() {
        // GIVEN
        val fragmentData = GetPromoListDataProvider.provideFragmentData()
        fragmentData.uiData.preAppliedPromoCode = listOf("THIRX598GSA7MADK2X7")
        viewModel.setFragmentUiModelValue(fragmentData)

        val promoData = GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData()
        viewModel.setPromoListValue(promoData)

        // WHEN
        val hasDifferentPreAppliedState = viewModel.hasDifferentPreAppliedState()

        // THEN
        assert(hasDifferentPreAppliedState)
    }

    @Test
    fun `WHEN has any promo item checked but have not been applied THEN should has different pre applied state`() {
        // GIVEN
        val fragmentData = GetPromoListDataProvider.provideFragmentData()
        fragmentData.uiData.preAppliedPromoCode = listOf("CODE")
        viewModel.setFragmentUiModelValue(fragmentData)

        val promoData = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData()
        viewModel.setPromoListValue(promoData)

        // WHEN
        val hasDifferentPreAppliedState = viewModel.hasDifferentPreAppliedState()

        // THEN
        assert(hasDifferentPreAppliedState)
    }

    @Test
    fun `WHEN select promo item and cause clash THEN should has clashed promo`() {
        // GIVEN
        val response = GetPromoListDataProvider.provideGetPromoListResponseSuccessWithClashingData()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        viewModel.getPromoList(PromoRequest(), "")

        val selectedPromoItem = viewModel.promoListUiModel.value?.firstOrNull { it is PromoListItemUiModel } as PromoListItemUiModel

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(selectedPromoItem)

        // THEN
        val lastModifiedData = viewModel.tmpUiModel.value as? Update<*>
        assert(lastModifiedData != null)
        assert(lastModifiedData?.data is PromoListItemUiModel)
        assert((lastModifiedData?.data as PromoListItemUiModel).uiData.errorMessage.isNotBlank())
    }

    @Test
    fun `WHEN unselect promo item and clear up clash THEN should has no clashed promo`() {
        // GIVEN
        val response = GetPromoListDataProvider.provideGetPromoListResponseSuccessWithClashingData()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        viewModel.getPromoList(PromoRequest(), "")

        var selectedPromoItem: PromoListItemUiModel? = null
        viewModel.promoListUiModel.value?.forEach {
            if (it is PromoListItemUiModel && selectedPromoItem == null) {
                selectedPromoItem = it
            }
        }

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs

        // WHEN
        viewModel.updatePromoListAfterClickPromoItem(selectedPromoItem!!)
        viewModel.updatePromoListAfterClickPromoItem(selectedPromoItem!!)

        // THEN
        val lastModifiedData = viewModel.tmpUiModel.value as? Update<*>
        assert(lastModifiedData != null)
        assert(lastModifiedData?.data is PromoListItemUiModel)
        assert((lastModifiedData?.data as PromoListItemUiModel).uiData.errorMessage.isBlank())
    }

}