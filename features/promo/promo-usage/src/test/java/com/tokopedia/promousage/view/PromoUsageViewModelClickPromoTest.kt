package com.tokopedia.promousage.view

import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.Cta
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoItemCta
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.ClickPromoUiAction
import com.tokopedia.promousage.view.viewmodel.PromoCtaUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelClickPromoTest : BasePromoUsageViewModelTest() {

    @Test
    fun clickPromo_successClickRecommendationNormalToSelected() {
        // given
        val code = "CODERECOM1"
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
                                    isGroupHeader = true
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
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasSelectedRecommendedPromo = pageState?.items?.any {
            it is PromoItem && it.isRecommended &&
                it.state is PromoItemState.Selected && it.code == clickedPromo.code
        }
        assert(hasSelectedRecommendedPromo == true)
        assert(viewModel.clickPromoUiAction.value is ClickPromoUiAction.Updated)
    }

    @Test
    fun clickPromo_successClickRecommendationSelectedToNormal() {
        // given
        val code = "CODERECOM1"
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
                                    isGroupHeader = true,
                                    isSelected = true
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
                                    isGroupHeader = true
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
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasSelectedRecommendedPromo = pageState?.items?.any {
            it is PromoItem && it.isRecommended &&
                it.state is PromoItemState.Selected && it.code == clickedPromo.code
        }
        assert(hasSelectedRecommendedPromo == false)
        assert(viewModel.clickPromoUiAction.value is ClickPromoUiAction.Updated)
    }

    @Test
    fun clickPromo_successClickNormalToSelected() {
        // given
        val code = "CODEPAYMENT1"
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
                                    isGroupHeader = true
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
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasSelectedPromo = pageState?.items?.any {
            it is PromoItem && it.state is PromoItemState.Selected && it.code == clickedPromo.code
        }
        assert(hasSelectedPromo == true)
        assert(viewModel.clickPromoUiAction.value is ClickPromoUiAction.Updated)
    }

    @Test
    fun clickPromo_successClickSelectedToNormal() {
        // given
        val code = "CODEPAYMENT1"
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
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasSelectedPromo = pageState?.items?.any {
            it is PromoItem && it.state is PromoItemState.Selected && it.code == clickedPromo.code
        }
        assert(hasSelectedPromo == false)
        assert(viewModel.clickPromoUiAction.value is ClickPromoUiAction.Updated)
    }

    @Test
    fun clickPromo_successWithGopayLaterCicilRegistrationPromo() {
        // given
        val code = "CODESHIPPING1"
        val successResponse = GetPromoListRecommendationResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
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
                            id = PromoPageSection.SECTION_SHIPPING,
                            title = "shipping",
                            coupons = listOf(
                                Coupon(
                                    id = "shipping1",
                                    code = "CODESHIPPING1",
                                    isGroupHeader = true,
                                    couponType = listOf("gopay_later"),
                                    cta = Cta(
                                        type = PromoItemCta.TYPE_REGISTER_GOPAY_LATER_CICIL,
                                        text = "register cicil",
                                        applink = "tokopedia://register-gpl-cicil"
                                    )
                                )
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns successResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)

        // then
        assert(viewModel.promoCtaUiAction.value is PromoCtaUiAction.RegisterGoPayLaterCicil)
    }
}
