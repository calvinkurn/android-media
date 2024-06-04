package com.tokopedia.cart.view.viewmodel

import com.tokopedia.cart.data.model.response.promo.CartPromoData
import com.tokopedia.cart.data.model.response.promo.LastApplyPromo
import com.tokopedia.cart.data.model.response.promo.LastApplyPromoData
import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartData
import com.tokopedia.cart.view.helper.CartDataHelper
import com.tokopedia.cart.view.mapper.CartUiModelMapper
import com.tokopedia.cart.view.uimodel.EntryPointInfoEvent
import com.tokopedia.promousage.data.response.EntryPointInfo
import com.tokopedia.promousage.data.response.GetPromoListRecommendationEntryPointResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.PromoRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import io.mockk.coEvery
import io.mockk.every
import org.junit.Assert
import org.junit.Test

class PromoEntryPointTest : BaseCartViewModelTest() {

    @Test
    fun isPromoRevamp_successUserGroupA() {
        // given
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        userGroupMetadata = listOf(
                            UserGroupMetadata(
                                key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                                value = UserGroupMetadata.PROMO_USER_VARIANT_A
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        val isPromoRevamp = cartViewModel.isPromoRevamp()

        // then
        Assert.assertTrue(isPromoRevamp)
    }

    @Test
    fun isPromoRevamp_successUserGroupB() {
        // given
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        userGroupMetadata = listOf(
                            UserGroupMetadata(
                                key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                                value = UserGroupMetadata.PROMO_USER_VARIANT_B
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        val isPromoRevamp = cartViewModel.isPromoRevamp()

        // then
        Assert.assertTrue(isPromoRevamp)
    }

    @Test
    fun isPromoRevamp_successUserGroupC() {
        // given
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        userGroupMetadata = listOf(
                            UserGroupMetadata(
                                key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                                value = UserGroupMetadata.PROMO_USER_VARIANT_C
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        val isPromoRevamp = cartViewModel.isPromoRevamp()

        // then
        Assert.assertFalse(isPromoRevamp)
    }

    @Test
    fun isPromoRevamp_failedCartDataNull() {
        // given

        // when
        val isPromoRevamp = cartViewModel.isPromoRevamp()

        // then
        Assert.assertFalse(isPromoRevamp)
    }

    @Test
    fun getEntryPointInfoFromLastApply_success() {
        // given
        val lastApplyPromo = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                userGroupMetadata = listOf(
                    UserGroupMetadata(
                        key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                        value = UserGroupMetadata.PROMO_USER_VARIANT_A
                    )
                )
            )
        )
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = lastApplyPromo
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1
        every { CartDataHelper.hasSelectedCartItem(any()) } returns true
        val entryPointInfoResponse = GetPromoListRecommendationEntryPointResponse(
            promoListRecommendation = GetPromoListRecommendationResponseData(
                data = PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
                    ),
                    promoRecommendation = PromoRecommendation(
                        codes = listOf("code")
                    ),
                    entryPointInfo = EntryPointInfo(
                        state = "green"
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns entryPointInfoResponse

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        val lastApply = CartUiModelMapper.mapLastApplySimplified(lastApplyPromo.lastApplyPromoData)
        cartViewModel.getEntryPointInfoFromLastApply(lastApply)

        // then
        Assert.assertEquals(
            EntryPointInfoEvent.ActiveNew(
                lastApply = LastApplyUiModel(
                    benefitSummaryInfo = BenefitSummaryInfoUiModel(
                        finalBenefitAmount = 0
                    ),
                    userGroupMetadata = listOf(
                        UserGroupMetadata(
                            key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                            value = UserGroupMetadata.PROMO_USER_VARIANT_A
                        )
                    )
                ),
                entryPointInfo = PromoEntryPointInfo(
                    isSuccess = true,
                    statusCode = "200",
                    color = "green"
                ),
                recommendedPromoCodes = listOf("code")
            ),
            cartViewModel.entryPointInfoEvent.value
        )
    }

    @Test
    fun getEntryPointInfoFromLastApply_error() {
        // given
        val lastApplyPromo = LastApplyPromo(
            lastApplyPromoData = LastApplyPromoData(
                userGroupMetadata = listOf(
                    UserGroupMetadata(
                        key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                        value = UserGroupMetadata.PROMO_USER_VARIANT_A
                    )
                )
            )
        )
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = lastApplyPromo
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1
        every { CartDataHelper.hasSelectedCartItem(any()) } returns true
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } throws Exception("error")

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        val lastApply = CartUiModelMapper.mapLastApplySimplified(lastApplyPromo.lastApplyPromoData)
        cartViewModel.getEntryPointInfoFromLastApply(lastApply)

        // then
        Assert.assertEquals(
            EntryPointInfoEvent.Error(
                lastApply = LastApplyUiModel(
                    benefitSummaryInfo = BenefitSummaryInfoUiModel(
                        finalBenefitAmount = 0
                    ),
                    userGroupMetadata = listOf(
                        UserGroupMetadata(
                            key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                            value = UserGroupMetadata.PROMO_USER_VARIANT_A
                        )
                    )
                )
            ),
            cartViewModel.entryPointInfoEvent.value
        )
    }

    @Test
    fun getEntryPointInfoDefault_success() {
        // given
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        userGroupMetadata = listOf(
                            UserGroupMetadata(
                                key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                                value = UserGroupMetadata.PROMO_USER_VARIANT_A
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1
        every { CartDataHelper.hasSelectedCartItem(any()) } returns true
        val entryPointInfoResponse = GetPromoListRecommendationEntryPointResponse(
            promoListRecommendation = GetPromoListRecommendationResponseData(
                data = PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
                    ),
                    promoRecommendation = PromoRecommendation(
                        codes = listOf("code")
                    ),
                    entryPointInfo = EntryPointInfo(
                        state = "green"
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns entryPointInfoResponse

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        cartViewModel.getEntryPointInfoDefault()

        // then
        Assert.assertEquals(
            EntryPointInfoEvent.ActiveNew(
                lastApply = LastApplyUiModel(
                    benefitSummaryInfo = BenefitSummaryInfoUiModel(
                        finalBenefitAmount = 0
                    ),
                    userGroupMetadata = listOf(
                        UserGroupMetadata(
                            key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                            value = UserGroupMetadata.PROMO_USER_VARIANT_A
                        )
                    )
                ),
                entryPointInfo = PromoEntryPointInfo(
                    isSuccess = true,
                    statusCode = "200",
                    color = "green"
                ),
                recommendedPromoCodes = listOf("code")
            ),
            cartViewModel.entryPointInfoEvent.value
        )
    }

    @Test
    fun getEntryPointInfoDefault_error() {
        // given
        val cartData = CartData(
            promo = CartPromoData(
                lastApplyPromo = LastApplyPromo(
                    lastApplyPromoData = LastApplyPromoData(
                        userGroupMetadata = listOf(
                            UserGroupMetadata(
                                key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                                value = UserGroupMetadata.PROMO_USER_VARIANT_A
                            )
                        )
                    )
                )
            )
        )
        coEvery {
            getCartRevampV4UseCase(any())
        } returns cartData
        coEvery { updateCartCounterUseCase(Unit) } returns 1
        every { CartDataHelper.hasSelectedCartItem(any()) } returns true
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } throws Exception("error")

        // when
        cartViewModel.processInitialGetCartData(
            cartId = "",
            initialLoad = true,
            isLoadingTypeRefresh = false
        )
        cartViewModel.getEntryPointInfoDefault()

        // then
        Assert.assertEquals(
            EntryPointInfoEvent.Error(
                lastApply = LastApplyUiModel(
                    benefitSummaryInfo = BenefitSummaryInfoUiModel(
                        finalBenefitAmount = 0
                    ),
                    userGroupMetadata = listOf(
                        UserGroupMetadata(
                            key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                            value = UserGroupMetadata.PROMO_USER_VARIANT_A
                        )
                    )
                )
            ),
            cartViewModel.entryPointInfoEvent.value
        )
    }

    @Test
    fun getEntryPointInfoDefault_errorDefault() {
        // given
        cartPromoEntryPointProcessor.isPromoRevamp = false
        val appliedPromoCodes = listOf("PROMO1", "PROMO2")

        // when
        cartViewModel.getEntryPointInfoDefault(
            appliedPromoCodes = appliedPromoCodes,
            isError = true
        )

        // then
        Assert.assertEquals(
            EntryPointInfoEvent.ActiveDefault(appliedPromoCodes),
            cartViewModel.entryPointInfoEvent.value
        )
    }

    @Test
    fun getEntryPointInfoNoItemSelected_success() {
        // given

        // when
        cartViewModel.getEntryPointInfoNoItemSelected()

        // then
        val expectedEntryPointInfoEvent = EntryPointInfoEvent.Inactive(
            isNoItemSelected = true
        )
        Assert.assertEquals(
            expectedEntryPointInfoEvent,
            cartViewModel.entryPointInfoEvent.value
        )
    }
}
