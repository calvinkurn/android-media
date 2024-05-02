package com.tokopedia.checkout.revamp.view

import com.tokopedia.cartcommon.data.response.updatecart.Data
import com.tokopedia.cartcommon.data.response.updatecart.UpdateCartV2Data
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkoutpayment.view.CheckoutPaymentWidgetData
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.promousage.data.response.EntryPointInfo
import com.tokopedia.promousage.data.response.GetPromoListRecommendationEntryPointResponse
import com.tokopedia.promousage.data.response.GetPromoListRecommendationResponseData
import com.tokopedia.promousage.data.response.PromoListRecommendation
import com.tokopedia.promousage.data.response.ResultStatus
import com.tokopedia.promousage.domain.entity.PromoEntryPointInfo
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
        assertTrue(shouldAnimateWording)
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
        assertFalse(shouldAnimateWording)
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
        assertFalse(shouldAnimateWording)
    }

    @Test
    fun `GIVEN success update cart WHEN update cart for promo THEN should call on success`() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = true))

        var successCalled = false

        // when
        viewModel.updateCartForPromo {
            successCalled = true
        }

        // then
        assertTrue(successCalled)
    }

    @Test
    fun `GIVEN failed update cart WHEN update cart for promo THEN should call on success`() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(
                recipientAddressModel = RecipientAddressModel().apply {
                    id = "1"
                    destinationDistrictId = "1"
                    addressName = "jakarta"
                    postalCode = "123"
                    latitude = "123"
                    longitude = "321"
                    street = "jl jakarta"
                    provinceName = "jakarta"
                    cityName = "jakarta"
                    countryName = "indonesia"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", quantity = 1, price = 1000.0),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData())),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutPaymentModel(widget = CheckoutPaymentWidgetData(), enable = true),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        coEvery {
            updateCartUseCase.get().executeOnBackground()
        } returns UpdateCartV2Data(status = "OK", data = Data(status = false))

        var successCalled = false

        // when
        viewModel.updateCartForPromo {
            successCalled = true
        }

        // then
        assertFalse(successCalled)
        assertNotNull(latestToaster)
    }
}
