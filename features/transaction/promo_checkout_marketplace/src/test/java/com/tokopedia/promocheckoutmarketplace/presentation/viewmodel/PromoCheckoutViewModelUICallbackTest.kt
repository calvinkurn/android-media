package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoRecommendationUiModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import org.junit.Test

class PromoCheckoutViewModelUICallbackTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN has any promo selected THEN should return true`() {
        //given
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())

        //when
        val hasAnyPromoSelected = viewModel.isHasAnySelectedPromoItem()

        //then
        assert(hasAnyPromoSelected)
    }

    @Test
    fun `WHEN call reset promo THEN should have expanded promo items`() {
        //given
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickResetPromo(any()) } just Runs

        //when
        viewModel.resetPromo()

        //then
        assert(viewModel.promoListUiModel.value?.size ?: 0 > 0)
    }

    @Test
    fun `WHEN call reset promo THEN fragment state should be no selected promo`() {
        //given
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())

        every { analytics.eventClickResetPromo(any()) } just Runs

        //when
        viewModel.resetPromo()

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasAnyPromoSelected == false)
    }

    @Test
    fun `WHEN call reset promo THEN promo recommendation should be enabled`() {
        //given
        viewModel.setPromoListValue(GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData())
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickResetPromo(any()) } just Runs

        //when
        viewModel.resetPromo()

        //then
        assert(viewModel.promoRecommendationUiModel.value?.uiState?.isButtonSelectEnabled == true)
    }

    @Test
    fun `WHEN click unselected attempted promo item THEN should become selected`() {
        //given
        val data = GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isAttempted = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        //when
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        //then
        assert((viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
    }

    @Test
    fun `WHEN click unselected attempted promo item THEN sibling promo should become unselected`() {
        //given
        val data = GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        val secondPromoListItemUiModel = data[2] as PromoListItemUiModel
        secondPromoListItemUiModel.uiState.isRecommended = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs

        //when
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        //then
        assert(!((viewModel.promoListUiModel.value?.get(2) as PromoListItemUiModel).uiState.isSelected))
    }

    @Test
    fun `WHEN click selected attempted promo item THEN should become unselected`() {
        //given
        val data = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoDataWithHeader()
        val promoListItemUiModel = data[1] as PromoListItemUiModel
        promoListItemUiModel.uiState.isAttempted = true
        viewModel.setPromoListValue(data)

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        //when
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        //then
        assert(!(viewModel.promoListUiModel.value?.get(1) as PromoListItemUiModel).uiState.isSelected)
    }

    @Test
    fun `WHEN click selected recommended promo item THEN recommendation should be resetted`() {
        //given
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

        //when
        viewModel.updatePromoListAfterClickPromoItem(promoListItemUiModel)

        //then
        assert(viewModel.promoRecommendationUiModel.value?.uiState?.isButtonSelectEnabled == true)
    }

    @Test
    fun `WHEN apply recommended promo of expanded item THEN should be applied`() {
        //given
        val data = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoDataWithHeader()
        viewModel.setPromoListValue(data)
        viewModel.setPromoRecommendationValue(PromoRecommendationUiModel(
                uiData = PromoRecommendationUiModel.UiData().apply { promoCodes = listOf("THIRX598GSA7MADK2X7") },
                uiState = PromoRecommendationUiModel.UiState())
        )

        every { analytics.eventClickPilihPromoRecommendation(any(), any()) } just Runs
        every { analytics.eventClickPilihOnRecommendation(any(), any(), any()) } just Runs

        //when
        viewModel.applyRecommendedPromo()

        //then
        assert(viewModel.promoRecommendationUiModel.value?.uiState?.isButtonSelectEnabled == false)
    }

    @Test
    fun `WHEN update state before apply manual input promo THEN state should be loading`() {
        //given
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        //when
        viewModel.updatePromoInputStateBeforeApplyPromo("code", false)

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.isLoading == true)
    }

    @Test
    fun `WHEN update state before apply manual input promo THEN promo code should not be empty`() {
        //given
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        //when
        viewModel.updatePromoInputStateBeforeApplyPromo("code", false)

        //then
        assert(viewModel.promoInputUiModel.value?.uiData?.promoCode?.isNotBlank() == true)
    }

    @Test
    fun `WHEN reset promo manual input state THEN state should be not loading`() {
        //given
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        //when
        viewModel.resetPromoInput()

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.isLoading == false)
    }

    @Test
    fun `WHEN reset promo manual input state THEN promo code should be empty`() {
        //given
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickTerapkanPromo(any(), any()) } just Runs
        every { analytics.eventClickTerapkanAfterTypingPromoCode(any(), any(), any()) } just Runs

        //when
        viewModel.resetPromoInput()

        //then
        assert(viewModel.promoInputUiModel.value?.uiData?.promoCode.isNullOrEmpty())
    }

    @Test
    fun `WHEN set promo input code from last apply THEN promo code should not be empty`() {
        //given
        val data = GetPromoListDataProvider.providePromoInputData()
        viewModel.setPromoInputUiModelValue(data)

        every { analytics.eventClickPromoLastSeenItem(any(), any()) } just Runs

        //when
        viewModel.setPromoInputFromLastApply("code")

        //then
        assert(viewModel.promoInputUiModel.value?.uiData?.promoCode?.isBlank() == false)
    }

    @Test
    fun `WHEN pre applied promo code is empty THEN should has no different pre applied state`() {
        //given
        val data = GetPromoListDataProvider.provideFragmentData()
        viewModel.setFragmentUiModelValue(data)

        //when
        val hasDifferentPreAppliedState = viewModel.hasDifferentPreAppliedState()

        //then
        assert(!hasDifferentPreAppliedState)
    }

    @Test
    fun `WHEN has any expanded promo item unchecked but exist as pre applied promo item THEN should has different pre applied state`() {
        //given
        val fragmentData = GetPromoListDataProvider.provideFragmentData()
        fragmentData.uiData.preAppliedPromoCode = listOf("THIRX598GSA7MADK2X7")
        viewModel.setFragmentUiModelValue(fragmentData)

        val promoData = GetPromoListDataProvider.provideCurrentUnSelectedExpandedGlobalAndMerchantPromoData()
        viewModel.setPromoListValue(promoData)

        //when
        val hasDifferentPreAppliedState = viewModel.hasDifferentPreAppliedState()

        //then
        assert(hasDifferentPreAppliedState)
    }

    @Test
    fun `WHEN has any expanded promo item checked but have not been applied THEN should has different pre applied state`() {
        //given
        val fragmentData = GetPromoListDataProvider.provideFragmentData()
        fragmentData.uiData.preAppliedPromoCode = listOf("CODE")
        viewModel.setFragmentUiModelValue(fragmentData)

        val promoData = GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData()
        viewModel.setPromoListValue(promoData)

        //when
        val hasDifferentPreAppliedState = viewModel.hasDifferentPreAppliedState()

        //then
        assert(hasDifferentPreAppliedState)
    }

}