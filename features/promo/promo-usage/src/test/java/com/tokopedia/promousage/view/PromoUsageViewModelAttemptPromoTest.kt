package com.tokopedia.promousage.view

import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelAttemptPromoTest : BasePromoUsageViewModelTest() {

    @Test
    fun onAttemptPromoCode_success() {
        // given
        val promoRequest = PromoRequest()
        val chosenAddress = ChosenAddress()
        val attemptedPromoCode = "CODEATTEMPT"

        val response = GetPromoListRecommendationResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
                    ),
                    couponSections = listOf(
                        CouponSection(
                            id = "section1",
                            title = "title1",
                            coupons = listOf(
                                Coupon(
                                    id = "1",
                                    code = "CODE1",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "1",
                                    code = "CODE2",
                                    isGroupHeader = true
                                )
                            )
                        ),
                        CouponSection(
                            id = PromoPageSection.SECTION_INPUT_PROMO_CODE,
                            coupons = listOf(
                                Coupon(
                                    id = "3",
                                    code = "CODEATTEMPT",
                                    isGroupHeader = true,
                                    isAttempted = true
                                )
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns response

        // when
        viewModel.onAttemptPromoCode(
            promoRequest = promoRequest,
            chosenAddress = chosenAddress,
            attemptedPromoCode = attemptedPromoCode
        )

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val attemptedPromos = pageState?.items?.filterIsInstance<PromoItem>()
            ?.filter { it.isAttempted }
        assert(attemptedPromos?.size == 1)
        assert(attemptedPromos?.first()?.code == attemptedPromoCode)
    }
}
