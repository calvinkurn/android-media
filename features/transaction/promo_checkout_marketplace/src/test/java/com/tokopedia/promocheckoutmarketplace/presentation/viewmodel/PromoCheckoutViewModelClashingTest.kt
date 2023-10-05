package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.FragmentUiModel
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Test

class PromoCheckoutViewModelClashingTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN select BO promo with MVC primary not clashing THEN calculate clashing and enable MVC promo`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithMvcPrimaryPromoNotClashResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // When
        val boPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir
        } as PromoListItemUiModel
        val mvcWithSecondaryPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiData.secondaryCoupons.isNotEmpty()
        } as PromoListItemUiModel
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(mvcWithSecondaryPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()

        // Then
        val selectedBoPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir && it.uiState.isSelected
        } ?: 0
        val selectedMvcPrimaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        val selectedMvcSecondaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.useSecondaryPromo && it.uiData.currentClashingSecondaryPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedBoPromoCount == 1)
        assert(selectedMvcPrimaryPromoCount == 1)
        assert(selectedMvcSecondaryPromoCount == 0)
    }

    @Test
    fun `WHEN select BO promo with MVC primary clashing and secondary not clashing THEN calculate clashing and enable secondary promo`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithMvcSecondaryPromoNotClashResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // When
        val boPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir
        } as PromoListItemUiModel
        val mvcWithSecondaryPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiData.secondaryCoupons.isNotEmpty()
        } as PromoListItemUiModel
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(mvcWithSecondaryPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()

        // Then
        val selectedBoPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir && it.uiState.isSelected
        } ?: 0
        val selectedMvcPrimaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        val selectedMvcSecondaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.useSecondaryPromo && it.uiData.currentClashingSecondaryPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedBoPromoCount == 1)
        assert(selectedMvcPrimaryPromoCount == 0)
        assert(selectedMvcSecondaryPromoCount == 1)
        assert(viewModel.fragmentUiModel.value?.uiData?.benefitAdjustmentMessage != "")
    }

    @Test
    fun `WHEN selected promo is BO promo and MVC secondary then unselect BO Promo THEN BO Promo should be disabled and MVC changed to primary`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithMvcSecondaryPromoNotClashResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // When
        val boPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir
        } as PromoListItemUiModel
        val mvcWithSecondaryPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiData.secondaryCoupons.isNotEmpty()
        } as PromoListItemUiModel
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(mvcWithSecondaryPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()

        // Then
        val selectedBoPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir && it.uiState.isSelected
        } ?: 0
        val selectedMvcPrimaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        val selectedMvcSecondaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.useSecondaryPromo && it.uiData.currentClashingSecondaryPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedBoPromoCount == 0)
        assert(selectedMvcPrimaryPromoCount == 1)
        assert(selectedMvcSecondaryPromoCount == 0)
        assert(viewModel.fragmentUiModel.value?.uiData?.benefitAdjustmentMessage != "")
    }

    @Test
    fun `WHEN select BO promo with MVC primary and secondary clashing THEN calculate clashing and disable MVC promo`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithMvcSecondaryPromoClashResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // When
        val boPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir
        } as PromoListItemUiModel
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()

        // Then
        val selectedBoPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir && it.uiState.isSelected
        } ?: 0
        val clashingMvcSecondaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingSecondaryPromo.isNotEmpty()
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedBoPromoCount == 1)
        assert(clashingMvcSecondaryPromoCount == 1)
        assert(viewModel.fragmentUiModel.value?.uiData?.benefitAdjustmentMessage == "")
    }

    @Test
    fun `WHEN multiple order select BO Promo THEN calculate clashing and set MVC to secondary`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithMultipleOrderMvcSecondaryResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // When
        val boPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir
        } as PromoListItemUiModel
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()

        // Then
        val selectedBoPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir && it.uiState.isSelected
        } ?: 0
        val enabledMvcSecondaryPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingPromo.isEmpty() && !it.uiState.isDisabled
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedBoPromoCount == 1)
        assert(enabledMvcSecondaryPromoCount == 2)
        assert(viewModel.fragmentUiModel.value?.uiData?.benefitAdjustmentMessage != "")
    }

    @Test
    fun `WHEN multiple order select BO Promo with MVC secondary selected then unselect BO Promo THEN set first MVC promo to primary then second MVC promo to secondary`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithMultipleOrderMvcSecondaryResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs

        // When
        val boPromo = viewModel.promoListUiModel.value?.first {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir
        } as PromoListItemUiModel
        val mvcWithSecondaryPromos = viewModel.promoListUiModel.value?.filter {
            it is PromoListItemUiModel && it.uiData.secondaryCoupons.isNotEmpty()
        } as List<PromoListItemUiModel>
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(mvcWithSecondaryPromos[0])
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(mvcWithSecondaryPromos[1])
        testDispatchers.coroutineDispatcher.advanceUntilIdle()
        viewModel.updatePromoListAfterClickPromoItem(boPromo)
        testDispatchers.coroutineDispatcher.advanceUntilIdle()

        // Then
        val selectedBoPromoCount = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiState.isBebasOngkir && it.uiState.isSelected
        } ?: 0
        val selectedMvcWithPrimaryPromo = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        val selectedMvcWithSecondaryPromo = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.useSecondaryPromo && it.uiData.currentClashingSecondaryPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedBoPromoCount == 0)
        assert(selectedMvcWithPrimaryPromo == 1)
        assert(selectedMvcWithSecondaryPromo == 1)
    }

    @Test
    fun `WHEN recommended promo is secondary mvc promo and primary tokopedia voucher THEN should select secondary mvc promo and primary tokopedia voucher`() {
        // Given
        val response = GetPromoListDataProvider.provideCouponListRecommendationWithSecondaryAndPrimaryRecommendedResponse()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        viewModel.getPromoList(PromoRequest(), "")

        every { analytics.eventClickSelectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickSelectPromo(any(), any()) } just Runs
        every { analytics.eventClickDeselectKupon(any(), any(), any()) } just Runs
        every { analytics.eventClickDeselectPromo(any(), any()) } just Runs
        every { analytics.eventClickPilihPromoRecommendation(any(), any()) } just Runs
        every { analytics.eventClickPilihOnRecommendation(any(), any(), any()) } just Runs

        // When
        viewModel.applyRecommendedPromo()

        // Then
        val selectedMvcWithPrimaryPromo = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.currentClashingPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        val selectedMvcWithSecondaryPromo = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && it.uiData.shopId > 0 && it.uiData.useSecondaryPromo && it.uiData.currentClashingSecondaryPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        val selectedPrimaryTokopediaVoucher = viewModel.promoListUiModel.value?.count {
            it is PromoListItemUiModel && !it.uiState.isBebasOngkir && it.uiData.shopId == 0 && it.uiData.currentClashingPromo.isEmpty() && it.uiState.isSelected
        } ?: 0
        assert(viewModel.promoListUiModel.value != null)
        assert(selectedMvcWithPrimaryPromo == 0)
        assert(selectedMvcWithSecondaryPromo == 1)
        assert(selectedPrimaryTokopediaVoucher == 1)
    }

    @Test
    fun `WHEN benefit adjustment message is set to false THEN set flag to false`() {
        // Given
        val benefitAdjustmentMessage = "promo disesuaikan"
        val fragmentUiModel = FragmentUiModel(
            uiData = FragmentUiModel.UiData(
                benefitAdjustmentMessage = benefitAdjustmentMessage
            ),
            FragmentUiModel.UiState(
                shouldShowToasterBenefitAdjustmentMessage = true
            )
        )

        // When
        viewModel.setFragmentUiModelValue(fragmentUiModel)
        viewModel.setShouldShowToasterBenefitAdjustmentMessage(false)

        // Then
        assert(viewModel.fragmentUiModel.value?.uiData?.benefitAdjustmentMessage == benefitAdjustmentMessage)
        assert(viewModel.fragmentUiModel.value?.uiState?.shouldShowToasterBenefitAdjustmentMessage == false)
    }
}
