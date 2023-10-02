package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutDonationModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEgoldModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelCrossSellGroupTest : BaseCheckoutViewModelTest() {

    @Test
    fun update_donation_checked() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    CheckoutDonationModel(
                        Donation(
                            nominal = 100
                        )
                    ),
                    CheckoutEgoldModel(
                        EgoldAttributeModel()
                    )
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateDonation(true)

        // then
        assertEquals(
            CheckoutDonationModel(
                Donation(nominal = 100, isChecked = true),
                isChecked = true
            ),
            viewModel.listData.value.crossSellGroup()?.crossSellList?.get(0)
        )
    }

    @Test
    fun update_donation_unchecked() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    CheckoutDonationModel(
                        Donation(
                            nominal = 100,
                            isChecked = true
                        ),
                        isChecked = true
                    )
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateDonation(false)

        // then
        assertEquals(
            CheckoutDonationModel(
                Donation(nominal = 100, isChecked = false),
                isChecked = false
            ),
            viewModel.listData.value.crossSellGroup()?.crossSellList?.get(0)
        )
    }

    @Test
    fun update_egold_checked() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    CheckoutEgoldModel(
                        EgoldAttributeModel()
                    ),
                    CheckoutDonationModel(
                        Donation(
                            nominal = 100
                        )
                    )
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateEgold(true)

        // then
        assertEquals(
            CheckoutEgoldModel(
                EgoldAttributeModel(isChecked = true),
                isChecked = true
            ),
            viewModel.listData.value.crossSellGroup()?.crossSellList?.get(0)
        )
    }

    @Test
    fun update_egold_unchecked() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    CheckoutEgoldModel(
                        EgoldAttributeModel(isChecked = true),
                        isChecked = true
                    )
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateEgold(false)

        // then
        assertEquals(
            CheckoutEgoldModel(
                EgoldAttributeModel(isChecked = false),
                isChecked = false
            ),
            viewModel.listData.value.crossSellGroup()?.crossSellList?.get(0)
        )
    }

    @Test
    fun update_cross_sell_checked() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    CheckoutCrossSellModel(
                        CrossSellModel()
                    ),
                    CheckoutDonationModel(
                        Donation(
                            nominal = 100
                        )
                    )
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateCrossSell(true)

        // then
        assertEquals(
            CheckoutCrossSellModel(
                CrossSellModel(isChecked = true),
                isChecked = true
            ),
            viewModel.listData.value.crossSellGroup()?.crossSellList?.get(0)
        )
    }

    @Test
    fun update_cross_sell_unchecked() {
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
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(
                crossSellList = listOf(
                    CheckoutCrossSellModel(
                        CrossSellModel(isChecked = true),
                        isChecked = true
                    )
                )
            ),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateCrossSell(false)

        // then
        assertEquals(
            CheckoutCrossSellModel(
                CrossSellModel(isChecked = false),
                isChecked = false
            ),
            viewModel.listData.value.crossSellGroup()?.crossSellList?.get(0)
        )
    }
}
