package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.promousage.data.response.EntryPointInfo
import com.tokopedia.promousage.data.response.GetPromoListRecommendationEntryPointResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Test

class CheckoutViewModelPromoEntryPointTest : BaseCheckoutViewModelTest() {

    @Test
    fun reloadEntryPointInfo_success() {
        // given
        viewModel.isCartCheckoutRevamp = true
        val response = CartShipmentAddressFormData(
            isError = false,
            groupAddress = listOf(
                GroupAddress(
                    groupShop = listOf(
                        GroupShop(
                            groupShopData = listOf(
                                GroupShopV2(
                                    products = listOf(
                                        Product()
                                    )
                                )
                            )
                        )
                    )
                )
            ),
            donation = Donation(
                title = "donasi",
                nominal = 1,
                isChecked = true
            ),
            lastApplyData = LastApplyUiModel(
                userGroupMetadata = listOf(
                    UserGroupMetadata(
                        key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                        value = UserGroupMetadata.PROMO_USER_VARIANT_A
                    )
                )
            )
        )
        coEvery {
            getShipmentAddressFormV4UseCase.invoke(any())
        } returns response
        val entryPointResponse = GetPromoListRecommendationEntryPointResponse(
            promoListRecommendation = GetPromoListRecommendationResponseData(
                data = PromoListRecommendation(
                    resultStatus = ResultStatus(
                        code = "200",
                        message = "OK",
                        success = true
                    ),
                    entryPointInfo = EntryPointInfo(
                        messages = listOf("message"),
                        iconUrl = "url",
                        state = "green",
                        clickable = true
                    )
                )
            )
        )
        coEvery {
            getPromoListRecommendationEntryPointUseCase(any())
        } returns entryPointResponse

        // When
        viewModel.loadSAF(false, false, false)
        viewModel.reloadEntryPointInfo()

        // then
        val resultPromo = viewModel.listData.value.promo()
        val expectedPromo = resultPromo?.copy(
            entryPointInfo = PromoEntryPointInfo(
                isSuccess = true,
                statusCode = "200",
                messages = listOf("message"),
                iconUrl = "url",
                color = "green",
                isClickable = true
            )
        )
        Assert.assertEquals(expectedPromo, resultPromo)
    }

    @Test
    fun shouldAnimateEntryPointWording_promoBenefitIncreased() {
        // given
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        type = "bebas_ongkir",
                        amount = 5_000
                    ),
                    LastApplyUsageSummariesUiModel(
                        type = "cashback",
                        amount = 15_000
                    )
                )
            )
        )
        val oldLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        type = "bebas_ongkir",
                        amount = 5_000
                    )
                )
            )
        )

        // when
        val shouldAnimateWording =
            viewModel.shouldAnimateEntryPointWording(newLastApply, oldLastApply)

        // then
        Assert.assertTrue(shouldAnimateWording)
    }

    @Test
    fun shouldAnimateEntryPointWording_promoBenefitDecreased() {
        // given
        val newLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        type = "bebas_ongkir",
                        amount = 5_000
                    )
                )
            )
        )
        val oldLastApply = LastApplyUiModel(
            additionalInfo = LastApplyAdditionalInfoUiModel(
                usageSummaries = listOf(
                    LastApplyUsageSummariesUiModel(
                        type = "bebas_ongkir",
                        amount = 5_000
                    ),
                    LastApplyUsageSummariesUiModel(
                        type = "cashback",
                        amount = 15_000
                    )
                )
            )
        )

        // when
        val shouldAnimateWording =
            viewModel.shouldAnimateEntryPointWording(newLastApply, oldLastApply)

        // then
        Assert.assertFalse(shouldAnimateWording)
    }

    @Test
    fun shouldAnimateEntryPointWording_noPromoBenefit() {
        // given
        val newLastApply = LastApplyUiModel()
        val oldLastApply = LastApplyUiModel()

        // when
        val shouldAnimateWording =
            viewModel.shouldAnimateEntryPointWording(newLastApply, oldLastApply)

        // then
        Assert.assertFalse(shouldAnimateWording)
    }
}
