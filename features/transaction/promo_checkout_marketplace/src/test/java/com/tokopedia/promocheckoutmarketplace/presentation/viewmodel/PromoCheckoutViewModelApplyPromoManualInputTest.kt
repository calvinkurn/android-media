package com.tokopedia.promocheckoutmarketplace.presentation.viewmodel

import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListRequest
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseApplyManualFailed
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseApplyManualSuccess
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateCouponListEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseEmptyStateEmpty
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideGetPromoListResponseSuccessAllExpanded
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedExpandedGlobalPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.provideNoCurrentSelectedExpandedMerchantPromoData
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedGlobalPromo
import com.tokopedia.promocheckoutmarketplace.GetPromoListDataProvider.providePromoRequestWithSelectedExpandedMerchantPromo
import com.tokopedia.promocheckoutmarketplace.data.response.CouponListRecommendationResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Test
import java.lang.reflect.Type

class PromoCheckoutViewModelApplyPromoManualInputTest : BasePromoCheckoutViewModelTest() {

    @Test
    fun `WHEN apply promo manual input THEN skip apply should be 0`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val promoRequest = provideGetPromoListRequest()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, attemptedPromoCode)

        //then
        assert(promoRequest.skipApply == 0)
    }

    @Test
    fun `WHEN apply promo manual input THEN should be added to request param`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val promoRequest = provideGetPromoListRequest()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, attemptedPromoCode)

        //then
        assert(promoRequest.attemptedCodes[0] == attemptedPromoCode)
    }

    @Test
    fun `WHEN apply promo manual input success THEN should dismiss bottomsheet`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val promoRequest = provideGetPromoListRequest()
        val promoResponse = provideGetPromoListResponseApplyManualSuccess()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(promoResponse)
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, attemptedPromoCode)

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.needToDismissBottomsheet == true)
    }

    @Test
    fun `WHEN re apply promo manual input THEN attempted code request param should be equal with typed promo code`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(attemptedPromoCode)

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, attemptedPromoCode)

        //then
        assert(promoRequest.attemptedCodes[0] == attemptedPromoCode)
    }

    @Test
    fun `WHEN re apply promo manual input THEN global promo request should not contain attempted promo code`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(attemptedPromoCode)

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, attemptedPromoCode)

        //then
        assert(!promoRequest.codes.contains(attemptedPromoCode))
    }

    @Test
    fun `WHEN re apply promo manual input THEN merchant promo request should not contain attempted promo code`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val promoRequest = provideGetPromoListRequest()
        promoRequest.orders[0].codes.add(attemptedPromoCode)

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, attemptedPromoCode)

        //then
        assert(!promoRequest.orders[0].codes.contains(attemptedPromoCode))
    }

    @Test
    fun `WHEN apply manual input promo has param selected expanded global promo but already unselected THEN request param should not contain global promo code`() {
        //given
        val result = HashMap<Type, Any>()
        viewModel.setPromoListValue(provideNoCurrentSelectedExpandedGlobalPromoData())
        val promoRequest = providePromoRequestWithSelectedExpandedGlobalPromo()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, "GLOBAL PROMO")

        //then
        assert(promoRequest.codes.isEmpty())
    }

    @Test
    fun `WHEN apply manual input promo has param selected expanded merchant promo but already unselected THEN request param should not contain merchant promo code`() {
        //given
        viewModel.setPromoListValue(provideNoCurrentSelectedExpandedMerchantPromoData())
        val promoRequest = providePromoRequestWithSelectedExpandedMerchantPromo()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(CouponListRecommendationResponse())
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(promoRequest, "MERCHANT PROMO")

        //then
        assert(promoRequest.orders[0].codes.isEmpty())
    }

    @Test
    fun `WHEN re apply promo manual input THEN get coupon list action state should be clear data`() {
        //given
        val attemptedPromoCode = "PROMO_CODE"
        val response = provideGetPromoListResponseSuccessAllExpanded()
        val promoRequest = provideGetPromoListRequest()
        promoRequest.codes.add(attemptedPromoCode)

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), attemptedPromoCode)

        //then
        assert(viewModel.getPromoListResponseAction.value?.state == GetPromoListResponseAction.ACTION_CLEAR_DATA)
    }

    @Test
    fun `WHEN apply manual input promo THEN get error`() {
        //given
        val response = provideGetPromoListResponseApplyManualFailed()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "PROMO_CODE")

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.isError == true)
    }

    @Test
    fun `WHEN apply manual input promo THEN get exception`() {
        //given
        val response = provideGetPromoListResponseApplyManualFailed()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }
        every { analytics.eventViewErrorAfterClickTerapkanPromo(any(), any(), any(), any()) } just Runs

        //when
        viewModel.getPromoList(PromoRequest(), "PROMO_CODE")

        //then
        assert(viewModel.promoInputUiModel.value?.uiData?.exception != null)
    }

    @Test
    fun `WHEN apply manual input promo and get empty data THEN promo list state should be show toast error`() {
        //given
        val response = provideGetPromoListResponseEmptyStateEmpty()

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "PROMO CODE")

        //then
        assert(viewModel.getPromoListResponseAction.value?.state == GetPromoListResponseAction.ACTION_SHOW_TOAST_ERROR)
    }

    @Test
    fun `WHEN apply manual input promo and get empty data THEN promo input state should be error`() {
        //given
        val response = provideGetPromoListResponseEmptyStateCouponListEmpty()
        viewModel.setPromoInputUiModelValue(uiModelMapper.mapPromoInputUiModel())

        coEvery { getCouponListRecommendationUseCase.setParams(any(), any()) } just Runs
        coEvery { getCouponListRecommendationUseCase.execute(any(), any()) } answers {
            firstArg<(CouponListRecommendationResponse) -> Unit>().invoke(response)
        }

        //when
        viewModel.getPromoList(PromoRequest(), "PROMO CODE")

        //then
        assert(viewModel.promoInputUiModel.value?.uiState?.isError == true)
    }

}