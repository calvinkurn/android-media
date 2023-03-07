package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseBoPromoInfoDataComplete
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseBoPromoInfoDataEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseBoPromoInfoDataIncomplete
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateBlacklisted
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateCouponListEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStatePhoneVerification
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateUnknown
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseError
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllEligible
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllExpanded
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllIneligible
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessWithBoPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessWithBoPromoNotSelected
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessWithPreSelectedPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessWithSelectedPromoBoClashing
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoListWithBoPlusAsRecommendedPromo
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoListItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class PromoCheckoutViewModelGetPromoListTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN get promo list and get complete expanded data THEN fragment ui model should not be null and state success`() {
        //given
        val response = provideGetPromoListResponseSuccessAllExpanded()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and get complete expanded data THEN promoRecommendation ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllExpanded()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete expanded data THEN promo input ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllExpanded()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get complete expanded data THEN promo list ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllExpanded()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and get no selected promo THEN fragment ui model state should be has no promo`() {
        //given
        val response = provideGetPromoListResponseSuccessAllExpanded()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasAnyPromoSelected == false)
    }

    @Test
    fun `WHEN get promo list and get selected promo THEN fragment ui model state should be has promo`() {
        //given
        val response = provideGetPromoListResponseSuccessWithPreSelectedPromo()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasAnyPromoSelected == true)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN fragment ui model should not be null and state success`() {
        //given
        val response = provideGetPromoListResponseSuccessAllEligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN promo input ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllEligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN promo list ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllEligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all eligible THEN promo recommendation ui model should not be null`() {
        //given
        val response =  provideGetPromoListResponseSuccessAllEligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN fragment ui model should not be null and state success`() {
        //given
        val response = provideGetPromoListResponseSuccessAllIneligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.fragmentUiModel.value)
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN promo input ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllIneligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN promo list ui model should not be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllIneligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoListUiModel.value)
    }

    @Test
    fun `WHEN get promo list and all ineligible THEN promo recommendation ui model should be null`() {
        //given
        val response = provideGetPromoListResponseSuccessAllIneligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNull(viewModel.promoRecommendationUiModel.value)
    }

    @Test
    fun `WHEN reload action has selected expanded global promo THEN should be added to request param`() {
        //given
        viewModel.setPromoListValue(provideCurrentSelectedExpandedGlobalPromoData())
        val promoRequest = provideGetPromoListRequest()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, "")

        //then
        assert(promoRequest.codes.isNotEmpty())
    }

    @Test
    fun `WHEN reload action has selected expanded merchant promo THEN should be added to request param`() {
        //given
        viewModel.setPromoListValue(provideCurrentSelectedExpandedMerchantPromoData())
        val promoRequest = provideGetPromoListRequest()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, "")

        //then
        assert(promoRequest.orders[0].codes.isNotEmpty() ||
                promoRequest.orders[1].codes.isNotEmpty() ||
                promoRequest.orders[2].codes.isNotEmpty())
    }

    @Test
    fun `WHEN reload action has selected expanded BO promo THEN should be added to request param`() {
        // given
        val promoList = providePromoListWithBoPlusAsRecommendedPromo()
        val selectedBoPromo = promoList[1] as PromoListItemUiModel
        selectedBoPromo.uiState.isSelected = true
        viewModel.setPromoListValue(promoList)
        val promoRequest = provideGetPromoListRequest()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        // when
        viewModel.getPromoList(promoRequest, "")

        // then
        assert(promoRequest.orders[2].codes.isNotEmpty())
        assert(promoRequest.orders[2].spId > 0)
        assert(promoRequest.orders[2].shippingId > 0)
    }

    @Test
    fun `WHEN get promo list and get response error THEN fragment state should be failed to load`() {
        //given
        val response = provideGetPromoListResponseError()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == true)
    }

    @Test
    fun `WHEN get promo list empty and empty state also empty THEN fragment state should be failed to load`() {
        //given
        val response = provideGetPromoListResponseEmptyStateEmpty()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == true)
    }

    @Test
    fun `WHEN get promo list and status is coupon list is empty THEN should show empty state`() {
        //given
        val response = provideGetPromoListResponseEmptyStateCouponListEmpty()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewAvailablePromoListNoPromo(any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is coupon list is empty THEN should not show button action`() {
        //given
        val response = provideGetPromoListResponseEmptyStateCouponListEmpty()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewAvailablePromoListNoPromo(any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == false)
    }

    @Test
    fun `WHEN get promo list and status is coupon list is empty THEN should show promo input`() {
        //given
        val response = provideGetPromoListResponseEmptyStateCouponListEmpty()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewAvailablePromoListNoPromo(any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoInputUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is phone not verified THEN should show empty state`() {
        //given
        val response = provideGetPromoListResponseEmptyStatePhoneVerification()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewPhoneVerificationMessage(any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is phone not verified THEN should show button action`() {
        //given
        val response = provideGetPromoListResponseEmptyStatePhoneVerification()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewPhoneVerificationMessage(any()) } just Runs


        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == true)
    }

    @Test
    fun `WHEN get promo list and status is blacklisted THEN should show empty state`() {
        //given
        val response = provideGetPromoListResponseEmptyStateBlacklisted()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewBlacklistErrorAfterApplyPromo(any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is blacklisted THEN should not show button action`() {
        //given
        val response = provideGetPromoListResponseEmptyStateBlacklisted()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        every { analytics.eventViewBlacklistErrorAfterApplyPromo(any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == false)
    }

    @Test
    fun `WHEN get promo list and status is unknown THEN should show empty state`() {
        //given
        val response = provideGetPromoListResponseEmptyStateUnknown()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assertNotNull(viewModel.promoEmptyStateUiModel.value)
    }

    @Test
    fun `WHEN get promo list and status is unknown THEN should show button action`() {
        //given
        val response = provideGetPromoListResponseEmptyStateUnknown()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.promoEmptyStateUiModel.value?.uiState?.isShowButton == true)
    }

    @Test
    fun `WHEN get promo list with promo BO selected THEN fragment state has applied BO should be true`() {
        // GIVEN
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        // WHEN
        viewModel.getPromoList(PromoRequest(), "")

        // THEN
        assert(viewModel.fragmentUiModel.value?.uiState?.hasPreAppliedBo == true)
        assert(viewModel.fragmentUiModel.value?.uiData?.unApplyBoMessage?.isNotEmpty() == true)
        assert(viewModel.fragmentUiModel.value?.uiData?.unApplyBoIcon?.isNotEmpty() == true)
    }

    @Test
    fun `WHEN get promo list with promo BO not selected THEN fragment state has applied BO should be false`() {
        // GIVEN
        val response = provideGetPromoListResponseSuccessWithBoPromoNotSelected()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        // WHEN
        viewModel.getPromoList(PromoRequest(), "")

        // THEN
        assert(viewModel.fragmentUiModel.value?.uiState?.hasPreAppliedBo == false)
        assert(viewModel.fragmentUiModel.value?.uiData?.unApplyBoMessage?.isEmpty() == true)
        assert(viewModel.fragmentUiModel.value?.uiData?.unApplyBoIcon?.isEmpty() == true)
    }

    @Test
    fun `WHEN get promo list and get response error followed with success THEN fragment state should be success`() {
        //precondition
        val response = provideGetPromoListResponseError()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        viewModel.getPromoList(PromoRequest(), "")

        //given
        val newResponse = provideGetPromoListResponseSuccessAllEligible()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(newResponse)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.hasFailedToLoad == false)
    }

    @Test
    fun `WHEN get promo list with BO info bottom sheet data complete THEN bottom sheet isVisible state should be true`() {
        // given
        val newResponse = provideGetPromoListResponseBoPromoInfoDataComplete()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(newResponse)
        }

        // when
        viewModel.getPromoList(PromoRequest(), "")

        // then
        assert(viewModel.boInfoBottomSheetUiModel.value?.uiState?.isVisible == true)
    }

    @Test
    fun `WHEN get promo list with BO info bottom sheet data incomplete THEN bottom sheet isVisible state should be false`() {
        // given
        val newResponse = provideGetPromoListResponseBoPromoInfoDataIncomplete()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(newResponse)
        }

        // when
        viewModel.getPromoList(PromoRequest(), "")

        // then
        assert(viewModel.boInfoBottomSheetUiModel.value?.uiState?.isVisible == false)
    }

    @Test
    fun `WHEN get promo list with BO info bottom sheet data empty THEN bottom sheet isVisible state should be false`() {
        //given
        val newResponse = provideGetPromoListResponseBoPromoInfoDataEmpty()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(newResponse)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.boInfoBottomSheetUiModel.value?.uiState?.isVisible == false)
    }

    @Test
    fun `WHEN get promo list and show selected promo with bo clashing THEN show ticker BO Clashing`() {
        // given
        val response = provideGetPromoListResponseSuccessWithSelectedPromoBoClashing()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.shouldShowTickerBoClashing == true)
    }

    @Test
    fun `WHEN get promo list and show selected promo without bo clashing THEN dont show ticker BO Clashing`() {
        // given
        val response = provideGetPromoListResponseSuccessWithBoPromo()
        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "")

        //then
        assert(viewModel.fragmentUiModel.value?.uiState?.shouldShowTickerBoClashing == false)
    }
}