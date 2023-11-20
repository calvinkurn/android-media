package com.tokopedia.promousage.view

import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.view.viewmodel.ClickTncUiAction
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelTncClickTest : BasePromoUsageViewModelTest() {

    @Test
    fun onClickTnc_success() {
        // given
        val response = GetPromoListRecommendationResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
                    ),
                    promoRecommendation = PromoRecommendation(
                        codes = listOf("CODERECOM1")
                    ),
                    couponSections = listOf(
                        CouponSection(
                            id = PromoPageSection.SECTION_RECOMMENDATION,
                            title = "recom",
                            coupons = listOf(
                                Coupon(
                                    id = "recom1",
                                    code = "CODERECOM1",
                                    isGroupHeader = true
                                )
                            )
                        ),
                        CouponSection(
                            id = "payment",
                            title = "payment",
                            coupons = listOf(
                                Coupon(
                                    id = "payment1",
                                    code = "CODEPAYMENT1",
                                    isGroupHeader = true,
                                    isSelected = true
                                ),
                                Coupon(
                                    id = "payment2",
                                    code = "CODEPAYMENT2",
                                    isGroupHeader = true
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
        viewModel.loadPromoList()
        viewModel.onClickTnc()

        // then
        assert(viewModel.clickTncUiAction.value is ClickTncUiAction.Success)
    }
}
