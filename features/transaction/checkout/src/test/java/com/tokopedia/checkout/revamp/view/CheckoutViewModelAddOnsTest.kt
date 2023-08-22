package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingMetadataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingNoteItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingTickerModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnBottomSheetResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnButtonResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnMetadata
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnNote
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.ProductResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.TickerResult
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import io.mockk.coEvery
import org.junit.Assert.assertEquals
import org.junit.Test

class CheckoutViewModelAddOnsTest : BaseCheckoutViewModelTest() {

    @Test
    fun update_gifting_product_level() {
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
            CheckoutProductModel(
                "123",
                cartId = 1,
                addOnGiftingProductLevelModel = AddOnGiftingDataModel()
            ),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateAddOnGiftingProductLevelDataBottomSheet(
            SaveAddOnStateResult(
                listOf(
                    AddOnResult(
                        addOnKey = "123-1",
                        status = 1,
                        addOnButton = AddOnButtonResult(1, "a", "b", "c", "d"),
                        addOnBottomSheet = AddOnBottomSheetResult(
                            "e",
                            "f",
                            emptyList(),
                            TickerResult("g")
                        )
                    )
                )
            )
        )

        // then
        assertEquals(
            AddOnGiftingDataModel(
                status = 1,
                emptyList(),
                AddOnGiftingButtonModel("b", "c", "a", 1, "d"),
                AddOnGiftingBottomSheetModel(AddOnGiftingTickerModel("g"), "f", "e", emptyList())
            ),
            (viewModel.listData.value[4] as CheckoutProductModel).addOnGiftingProductLevelModel
        )
    }

    @Test
    fun update_gifting_order_level() {
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
            CheckoutOrderModel("123", addOnsOrderLevelModel = AddOnGiftingDataModel()),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.updateAddOnGiftingOrderLevelDataBottomSheet(
            SaveAddOnStateResult(
                listOf(
                    AddOnResult(
                        addOnKey = "123-0",
                        status = 1,
                        addOnButton = AddOnButtonResult(1, "a", "b", "c", "d"),
                        addOnBottomSheet = AddOnBottomSheetResult(
                            "e",
                            "f",
                            listOf(
                                ProductResult("p1", "p2")
                            ),
                            TickerResult("g")
                        ),
                        addOnData = listOf(
                            AddOnData(
                                "a1",
                                "a2",
                                AddOnMetadata(
                                    AddOnNote(
                                        "a3",
                                        true,
                                        "a4",
                                        "a5"
                                    )
                                ),
                                1.0,
                                1
                            )
                        )
                    )
                )
            )
        )

        // then
        assertEquals(
            AddOnGiftingDataModel(
                status = 1,
                listOf(
                    AddOnGiftingDataItemModel(
                        1.0,
                        "a1",
                        AddOnGiftingMetadataItemModel(
                            AddOnGiftingNoteItemModel(
                                true,
                                "a5",
                                "a3",
                                "a4"
                            )
                        ),
                        "a2",
                        1
                    )
                ),
                AddOnGiftingButtonModel("b", "c", "a", 1, "d"),
                AddOnGiftingBottomSheetModel(
                    AddOnGiftingTickerModel("g"),
                    "f",
                    "e",
                    listOf(
                        AddOnGiftingProductItemModel(
                            "p1",
                            "p2"
                        )
                    )
                )
            ),
            (viewModel.listData.value[5] as CheckoutOrderModel).addOnsOrderLevelModel
        )
    }

    @Test
    fun set_addon() {
        // given
        coEvery {
            saveAddOnProductUseCase.executeOnBackground()
        }

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
            CheckoutProductModel(
                "123",
                addOnProduct = AddOnProductDataModel(
                    listAddOnProductData = listOf(
                        AddOnProductDataItemModel(uniqueId = "a1", status = 0),
                        AddOnProductDataItemModel(uniqueId = "a2", status = 0)
                    )
                )
            ),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setAddon(true, AddOnProductDataItemModel(uniqueId = "a2"), 4)

        // then
    }
}
