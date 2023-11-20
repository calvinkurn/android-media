package com.tokopedia.promousage.view

import com.tokopedia.localizationchooseaddress.common.ChosenAddress
import com.tokopedia.promousage.data.response.AttemptedPromoCodeError
import com.tokopedia.promousage.data.response.Coupon
import com.tokopedia.promousage.data.response.CouponSection
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoItemState
import com.tokopedia.promousage.domain.entity.PromoPageSection
import com.tokopedia.promousage.domain.entity.list.PromoAttemptItem
import com.tokopedia.promousage.domain.entity.list.PromoItem
import com.tokopedia.promousage.view.viewmodel.AttemptPromoUiAction
import com.tokopedia.promousage.view.viewmodel.PromoPageUiState
import com.tokopedia.purchase_platform.common.feature.promo.data.request.promolist.PromoRequest
import io.mockk.coEvery
import org.junit.Test

class PromoUsageViewModelLoadTest : BasePromoUsageViewModelTest() {

    @Test
    fun loadPromoList_getPromoListRecommendation_successNoAttemptPromo() {
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

        // when
        viewModel.loadPromoList()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        assert(hasPromo == true)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_successWithAttemptPromo() {
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
        viewModel.loadPromoList(
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

    @Test
    fun loadPromoList_getPromoListRecommendation_errorAttemptPromo() {
        // given
        val attemptedPromoCode = "CODE3"

        val response = GetPromoListRecommendationResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
                    ),
                    attemptedPromoCodeError = AttemptedPromoCodeError(
                        code = attemptedPromoCode,
                        message = "error"
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns response

        // when
        viewModel.loadPromoList(
            attemptedPromoCode = attemptedPromoCode
        )

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        assert(viewModel.attemptPromoUiAction.value is AttemptPromoUiAction.Failed)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_error() {
        // given
        val response = GetPromoListRecommendationResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "ERROR",
                        success = false
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns response

        // when
        viewModel.loadPromoList()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Error)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_errorListEmpty() {
        // given
        val response = GetPromoListRecommendationResponse(
            GetPromoListRecommendationResponseData(
                PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "42050",
                        message = "ERROR",
                        success = false
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationUseCase(any())
        } returns response

        // when
        viewModel.loadPromoList()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        val hasPromoInput = pageState?.items?.any { it is PromoAttemptItem }
        assert(pageState?.items?.size == 1 && hasPromo == false && hasPromoInput == true)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_errorException() {
        // given
        val exception = Exception("no internet connection")
        coEvery {
            getPromoListRecommendationUseCase(any())
        } throws exception

        // when
        viewModel.loadPromoList()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Error)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Error
        assert(pageState?.exception == exception)
    }

    @Test
    fun reloadPromoList_getPromoListRecommendation_success() {
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

        // when
        viewModel.reloadPromoList()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        assert(hasPromo == true)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_successWithFirstGopayPaylaterCicilPromoPreselected() {
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
                                    isGroupHeader = true,
                                    couponType = listOf("gopay_later")
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
        viewModel.loadPromoListWithPreSelectedGopayLaterPromo()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        assert(hasPromo == true)
        val hasSelectedGoPayLaterCicil = pageState?.items?.any {
            it is PromoItem && it.state is PromoItemState.Selected && it.couponType.contains("gopay_later")
        }
        assert(hasSelectedGoPayLaterCicil == true)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_successWithPreviouslyClickedGopayPaylaterCicilPromoPreselected() {
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
                                    isGroupHeader = true,
                                    couponType = listOf("gopay_later")
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

        // when
        viewModel.clickedGoPayLaterCicilPromoCode = "CODE1"
        viewModel.loadPromoListWithPreSelectedGopayLaterPromo()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        assert(hasPromo == true)
        val hasSelectedGoPayLaterCicil = pageState?.items?.any {
            it is PromoItem && it.state is PromoItemState.Selected && it.couponType.contains("gopay_later")
        }
        assert(hasSelectedGoPayLaterCicil == true)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_successWitheGopayPaylaterCicilPromoTypeNotMatch() {
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

        // when
        viewModel.clickedGoPayLaterCicilPromoCode = "CODE1"
        viewModel.loadPromoListWithPreSelectedGopayLaterPromo()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        assert(hasPromo == true)
        val hasSelectedGoPayLaterCicil = pageState?.items?.any {
            it is PromoItem && it.state is PromoItemState.Selected && it.couponType.contains("gopay_later")
        }
        assert(hasSelectedGoPayLaterCicil == false)
    }

    @Test
    fun loadPromoList_getPromoListRecommendation_successWithClickedGopayPaylaterCicilPromoNotMatch() {
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
                                    isGroupHeader = true,
                                    couponType = listOf("gopay_later")
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

        // when
        viewModel.clickedGoPayLaterCicilPromoCode = "CODE5"
        viewModel.loadPromoListWithPreSelectedGopayLaterPromo()

        // then
        assert(viewModel.promoPageUiState.value is PromoPageUiState.Success)
        val pageState = viewModel.promoPageUiState.value as? PromoPageUiState.Success
        val hasPromo = pageState?.items?.any { it is PromoItem }
        assert(hasPromo == true)
        val hasSelectedGoPayLaterCicil = pageState?.items?.any {
            it is PromoItem && it.state is PromoItemState.Selected && it.couponType.contains("gopay_later")
        }
        assert(hasSelectedGoPayLaterCicil == false)
    }
}
