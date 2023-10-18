package com.tokopedia.promousage.view

import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.promousage.view.viewmodel.UsePromoRecommendationUiAction
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelClickRecommendationTest : BasePromoUsageViewModelTest() {

//    @Test
//    fun onUsePromoRecommendation_success() {
//        // given
//        val response = GetPromoListRecommendationResponse(
//            GetPromoListRecommendationResponseData(
//                PromoListRecommendation(
//                    resultStatus = ResultStatus(
//                        code = "200",
//                        message = "OK",
//                        success = true
//                    ),
//                    promoRecommendation = PromoRecommendation(
//                        codes = listOf("CODERECOM1")
//                    ),
//                    couponSections = listOf(
//                        CouponSection(
//                            id = PromoPageSection.SECTION_RECOMMENDATION,
//                            title = "recom",
//                            coupons = listOf(
//                                Coupon(
//                                    id = "recom1",
//                                    code = "CODERECOM1",
//                                    isGroupHeader = true
//                                )
//                            )
//                        ),
//                        CouponSection(
//                            id = "payment",
//                            title = "payment",
//                            coupons = listOf(
//                                Coupon(
//                                    id = "payment1",
//                                    code = "CODEPAYMENT1",
//                                    isGroupHeader = true,
//                                    isSelected = true
//                                ),
//                                Coupon(
//                                    id = "payment2",
//                                    code = "CODEPAYMENT2",
//                                    isGroupHeader = true
//                                )
//                            )
//                        )
//                    )
//                )
//            )
//        )
//        coEvery {
//            getPromoListRecommendationUseCase(any())
//        } returns response
//
//        // when
//        viewModel.loadPromoList()
//        viewModel.onUsePromoRecommendation()
//
//        // then
//        assert(viewModel.usePromoRecommendationUiAction.value is UsePromoRecommendationUiAction.Success)
//        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
//        val hasUnselectedRecommendationPromo = pageState?.items?.any {
//            it is PromoItem && it.isRecommended && it.state == PromoItemState.Normal
//        }
//        assert(hasUnselectedRecommendationPromo == false)
//    }
}
