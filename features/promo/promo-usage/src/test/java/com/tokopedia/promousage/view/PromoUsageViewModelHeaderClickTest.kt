package com.tokopedia.promousage.view

import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.list.PromoAccordionHeaderItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelHeaderClickTest : BasePromoUsageViewModelTest() {

    @Test
    fun onClickAccordionHeader_expandAll() {
        // given
        val headerId = "section1"
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
                            isCollapsed = true,
                            coupons = listOf(
                                Coupon(
                                    id = "1",
                                    code = "CODE1",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "2",
                                    code = "CODE2",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "3",
                                    code = "CODE3",
                                    isGroupHeader = true
                                )
                            )
                        ),
                        CouponSection(
                            id = "section2",
                            title = "title1",
                            isCollapsed = true,
                            coupons = listOf(
                                Coupon(
                                    id = "4",
                                    code = "CODE5",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "5",
                                    code = "CODE5",
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
        val clickedHeader = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoAccordionHeaderItem } as PromoAccordionHeaderItem
        viewModel.onClickAccordionHeader(clickedHeader)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasHiddenPromo = pageState?.items?.any { it is PromoItem && !it.isVisible && it.headerId == headerId }
        assert(hasHiddenPromo == false)
    }

    @Test
    fun onClickAccordionHeader_collapseAll() {
        // given
        val headerId = "section1"
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
                            isCollapsed = false,
                            coupons = listOf(
                                Coupon(
                                    id = "1",
                                    code = "CODE1",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "2",
                                    code = "CODE2",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "3",
                                    code = "CODE3",
                                    isGroupHeader = true
                                )
                            )
                        ),
                        CouponSection(
                            id = "section2",
                            title = "title1",
                            isCollapsed = false,
                            coupons = listOf(
                                Coupon(
                                    id = "4",
                                    code = "CODE5",
                                    isGroupHeader = true
                                ),
                                Coupon(
                                    id = "5",
                                    code = "CODE5",
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
        val clickedHeader = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoAccordionHeaderItem } as PromoAccordionHeaderItem
        viewModel.onClickAccordionHeader(clickedHeader)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasVisiblePromo = pageState?.items?.any { it is PromoItem && it.isVisible && it.headerId == headerId }
        assert(hasVisiblePromo == false)
    }
}
