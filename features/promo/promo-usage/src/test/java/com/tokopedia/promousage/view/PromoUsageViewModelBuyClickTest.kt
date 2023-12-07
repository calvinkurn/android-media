package com.tokopedia.promousage.view

import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoPageEntryPoint
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.ApplyPromoUiAction
import com.tokopedia.promousage.view.viewmodel.ClearPromoUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.OrdersItem
import com.tokopedia.purchase_platform.common.feature.promo.data.request.validateuse.ValidateUsePromoRequest
import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.ClearCacheAutoApplyStackResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.response.clearpromo.SuccessData
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ClashingInfoDetail
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.Message
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.PromoValidateUseResponse
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUsePromoRevamp
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.ValidateUseResponse
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelBuyClickTest : BasePromoUsageViewModelTest() {

    @Test
    fun onClickBuy_successApply() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        val validateUseResponse = ValidateUseResponse(
            ValidateUsePromoRevamp(
                status = "OK",
                errorCode = "200",
                promo = PromoValidateUseResponse(
                    globalSuccess = true,
                    message = Message(
                        state = "green"
                    )
                )
            )
        )
        coEvery {
            validateUseUseCase(any())
        } returns validateUseResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.SuccessWithApplyPromo)
    }

    @Test
    fun onClickBuy_successApplyWithMvcPromo() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                                    isGroupHeader = true,
                                    shopId = 1,
                                    uniqueId = "UNIQUE1"
                                ),
                                Coupon(
                                    id = "2",
                                    code = "CODE2",
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
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest(
            codes = mutableListOf("CODEGLOBAL", "CODEGLOBAL2"),
            orders = listOf(
                OrdersItem(
                    codes = mutableListOf("CODE1", "CODE4"),
                    shopId = 1,
                    uniqueId = "UNIQUE1"
                ),
                OrdersItem(
                    codes = mutableListOf("CODE5"),
                    shopId = 2,
                    uniqueId = "UNIQUE2"
                )
            )
        )
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        val validateUseResponse = ValidateUseResponse(
            ValidateUsePromoRevamp(
                status = "OK",
                errorCode = "200",
                promo = PromoValidateUseResponse(
                    globalSuccess = true,
                    message = Message(
                        state = "green"
                    )
                )
            )
        )
        coEvery {
            validateUseUseCase(any())
        } returns validateUseResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.SuccessWithApplyPromo)
    }

    @Test
    fun onClickBuy_errorApplyStateRed() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        val validateUseResponse = ValidateUseResponse(
            ValidateUsePromoRevamp(
                status = "OK",
                errorCode = "200",
                promo = PromoValidateUseResponse(
                    globalSuccess = false,
                    message = Message(
                        state = "red"
                    )
                )
            )
        )
        coEvery {
            validateUseUseCase(any())
        } returns validateUseResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.Failed)
    }

    @Test
    fun onClickBuy_errorApplyCodeError() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        val validateUseResponse = ValidateUseResponse(
            ValidateUsePromoRevamp(
                status = "ERROR",
                errorCode = "500",
                promo = PromoValidateUseResponse(
                    globalSuccess = false,
                    message = Message(
                        state = "red"
                    )
                )
            )
        )
        coEvery {
            validateUseUseCase(any())
        } returns validateUseResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.Failed)
    }

    @Test
    fun onClickBuy_errorApplyClashing() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        val validateUseResponse = ValidateUseResponse(
            ValidateUsePromoRevamp(
                status = "OK",
                errorCode = "200",
                promo = PromoValidateUseResponse(
                    clashingInfoDetail = ClashingInfoDetail(
                        isClashedPromos = true
                    )
                )
            )
        )
        coEvery {
            validateUseUseCase(any())
        } returns validateUseResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.Failed)
    }

    @Test
    fun onClickBuy_errorApplyUnexpected() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        coEvery {
            validateUseUseCase(any())
        } throws Exception("error")

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.Failed)
    }

    @Test
    fun onClickBuy_successClear() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                                    isGroupHeader = true,
                                    isSelected = true
                                ),
                                Coupon(
                                    id = "1",
                                    code = "CODE2",
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
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        val clearResponse = ClearCacheAutoApplyStackResponse(
            SuccessData(
                success = true,
                tickerMessage = "message",
                defaultEmptyPromoMessage = "empty message"
            )
        )
        coEvery {
            clearCacheAutoApplyStackUseCase(any())
        } returns clearResponse

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.clearPromoUiAction.value is ClearPromoUiAction.Success)
    }

    @Test
    fun onClickBuy_errorClear() {
        // given
        val code = "CODE1"
        val promoResponse = GetPromoListRecommendationResponse(
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
                                    isGroupHeader = true,
                                    isSelected = true
                                ),
                                Coupon(
                                    id = "1",
                                    code = "CODE2",
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
        } returns promoResponse
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true
        coEvery {
            clearCacheAutoApplyStackUseCase(any())
        } throws Exception("error")

        // when
        viewModel.loadPromoList()
        val clickedPromo = (viewModel.promoPageUiState.value as PromoPageUiState.Success)
            .items.first { it is PromoItem && it.code == code } as PromoItem
        viewModel.onClickPromo(clickedPromo)
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.clearPromoUiAction.value is ClearPromoUiAction.Failed)
    }

    @Test
    fun onClickBuy_successNoAction() {
        // given
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
                        )
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns response
        val entryPoint = PromoPageEntryPoint.CART_PAGE
        val validateUseRequest = ValidateUsePromoRequest()
        val boPromoCodes = emptyList<String>()
        val isCartCheckoutRevamp = true

        // when
        viewModel.loadPromoList()
        viewModel.onClickBuy(entryPoint, validateUseRequest, boPromoCodes, isCartCheckoutRevamp)

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.applyPromoUiAction.value is ApplyPromoUiAction.SuccessNoAction)
    }
}
