package com.tokopedia.checkout.revamp.view

import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutAddressModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutButtonPaymentModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCostModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutCrossSellGroupModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutEpharmacyModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderInsurance
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderShipment
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPromoModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerErrorModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutTickerModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutUpsellModel
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.scheduledelivery.domain.model.DeliveryProduct
import com.tokopedia.logisticcart.scheduledelivery.domain.model.PromoStacking
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.CashOnDeliveryProduct
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.MerchantVoucherProductModel
import com.tokopedia.logisticcart.shipping.model.OntimeDelivery
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ScheduleDeliveryUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerAnnouncementHolderData
import com.tokopedia.unifycomponents.Toaster
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.IOException

class CheckoutViewModelLogisticTest : BaseCheckoutViewModelTest() {

    private var shippingDurationConverter = ShippingDurationConverter()

    @Test
    fun generate_rates_param() {
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
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        val ratesParam = viewModel.generateRatesParam(CheckoutOrderModel("123"), "")

        // then
        assertEquals(
            RatesParam.Builder(
                emptyList(),
                ShippingParam(
                    uniqueId = "123",
                    destinationDistrictId = "1",
                    destinationLatitude = "123",
                    destinationLongitude = "321",
                    destinationPostalCode = "123",
                    shopId = "0",
                    insurance = 1,
                    categoryIds = "0",
                    addressId = "1",
                    products = listOf(Product()),
                    boMetadata = BoMetadata()
                )
            ).apply { warehouseId("0") }.build(),
            ratesParam
        )
    }

    @Test
    fun `GIVEN bmgm in cart WHEN get shipping rates THEN should calculate orderValue with bmgm discount`() {
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
                isError = false,
                quantity = 1,
                price = 1000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = true,
                bmgmOfferName = "tokopedia 1",
                bmgmOfferMessage = listOf("jakarta"),
                bmgmTotalDiscount = 500.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_HEADER
            ),
            CheckoutProductModel(
                "123",
                isError = false,
                quantity = 1,
                price = 2000.0,
                isBMGMItem = true,
                shouldShowBmgmInfo = true,
                bmgmOfferName = "tokopedia 2",
                bmgmOfferMessage = listOf("medan"),
                bmgmTotalDiscount = 500.0,
                bmgmItemPosition = ShipmentMapper.BMGM_ITEM_DEFAULT
            ),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        val ratesParam = viewModel.generateRatesParam(CheckoutOrderModel("123"), "")

        // then
        assertEquals("2500", ratesParam.order_value)
    }

    @Test
    fun set_selected_courier() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123"),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        val courierItemData = CourierItemData()

        // when
        viewModel.setSelectedCourier(
            5,
            courierItemData,
            listOf(ShippingCourierUiModel()),
            InsuranceData()
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
    }

    @Test
    fun set_selected_courier_with_previous_promo_code() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(match { it.orderData.orders.first().codes.size == 1 && it.orderData.orders.first().codes.first() == "promoBO" })
                .executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(success = true)
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel(
                "123",
                shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "promoBO"))
            ),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        val courierItemData = CourierItemData()

        // when
        viewModel.setSelectedCourier(
            5,
            courierItemData,
            listOf(ShippingCourierUiModel()),
            InsuranceData()
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun load_shipping_normal() {
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
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val shippingCourierUiModel = ShippingCourierUiModel()
        coEvery { ratesUseCase.invoke(any()) } returns ShippingRecommendationData(
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel(
                    shippingCourierViewModelList = listOf(
                        shippingCourierUiModel
                    )
                )
            )
        )

        // when
        viewModel.loadShipping(CheckoutOrderModel("123"), 5)

        // then
        assertEquals(
            CheckoutOrderShipment(
                courierItemData = CourierItemData(
                    name = "",
                    estimatedTimeDelivery = "",
                    shipperFormattedPrice = "",
                    insuranceUsedInfo = "",
                    promoCode = "",
                    checksum = "",
                    ut = "",
                    now = false,
                    priorityInnactiveMessage = "",
                    priorityFormattedPrice = "",
                    priorityDurationMessage = "",
                    priorityCheckboxMessage = "",
                    priorityWarningboxMessage = "",
                    priorityFeeMessage = "",
                    priorityPdpMessage = "",
                    ontimeDelivery = OntimeDelivery(
                        textLabel = "",
                        textDetail = "",
                        urlDetail = ""
                    ),
                    codProductData = CashOnDeliveryProduct(0, "", 0, "", "", ""),
                    etaText = "",
                    etaErrorCode = -1,
                    merchantVoucherProductModel = MerchantVoucherProductModel(0),
                    isSelected = true
                ),
                shippingCourierUiModels = listOf(shippingCourierUiModel)
            ),
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment
        )
    }

    @Test
    fun load_shipping_normal_with_last_applied_bo_not_found() {
        // given
        val orderModel = CheckoutOrderModel("123", boCode = "boCode", boUniqueId = "12")
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
            CheckoutProductModel("123", cartStringOrder = "12"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        viewModel.logisticProcessor.isBoUnstackEnabled = true

        val shippingCourierUiModel = ShippingCourierUiModel()
        coEvery { ratesUseCase.invoke(any()) } returns ShippingRecommendationData(
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel(
                    shippingCourierViewModelList = listOf(
                        shippingCourierUiModel
                    )
                )
            )
        )

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(match { it.orderData.orders.first().codes.size == 1 && it.orderData.orders.first().codes.first() == "boCode" })
                .executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(success = true)
        )

        // when
        viewModel.loadShipping(orderModel, 5)

        // then
        assertEquals(
            CheckoutOrderShipment(
                courierItemData = null,
                shippingCourierUiModels = emptyList()
            ),
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment
        )

        coVerify {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }

        assertEquals(
            CheckoutPageToaster(
                Toaster.TYPE_NORMAL,
                "Bebas ongkir gagal diaplikasikan, silahkan coba lagi"
            ),
            latestToaster
        )
    }

    @Test
    fun load_shipping_normal_with_last_applied_bo_found() {
        // given
        val orderModel = CheckoutOrderModel("123", boCode = "boCode", boUniqueId = "12")
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
            CheckoutProductModel("123", cartStringOrder = "12"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        viewModel.logisticProcessor.isBoUnstackEnabled = true

        val shippingCourierUiModel = ShippingCourierUiModel()
        coEvery { ratesUseCase.invoke(any()) } returns ShippingRecommendationData(
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel(
                    shippingCourierViewModelList = listOf(
                        shippingCourierUiModel
                    )
                )
            ),
            listLogisticPromo = listOf(
                LogisticPromoUiModel("boCode")
            )
        )

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "boCode",
                        uniqueId = "12",
                        cartStringGroup = "123",
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // when
        viewModel.loadShipping(orderModel, 5)

        // then
        assertEquals(
            CheckoutOrderShipment(
                courierItemData = CourierItemData(
                    name = "",
                    estimatedTimeDelivery = "",
                    shipperFormattedPrice = "",
                    insuranceUsedInfo = "",
                    promoCode = "",
                    checksum = "",
                    ut = "",
                    now = false,
                    priorityInnactiveMessage = "",
                    priorityFormattedPrice = "",
                    priorityDurationMessage = "",
                    priorityCheckboxMessage = "",
                    priorityWarningboxMessage = "",
                    priorityFeeMessage = "",
                    priorityPdpMessage = "",
                    ontimeDelivery = OntimeDelivery(
                        textLabel = "",
                        textDetail = "",
                        urlDetail = ""
                    ),
                    codProductData = CashOnDeliveryProduct(0, "", 0, "", "", ""),
                    etaText = "",
                    etaErrorCode = -1,
                    merchantVoucherProductModel = MerchantVoucherProductModel(0),
                    isSelected = true,
                    logPromoCode = "boCode",
                    promoTitle = "",
                    shipperName = ""
                ),
                shippingCourierUiModels = emptyList()
            ),
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment
        )

        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success THEN should render success`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        val orderModel = CheckoutOrderModel("123", ratesValidationFlow = true, shippingId = 1, spId = 1)
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
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            ratesWithScheduleUseCase(any())
        }
        assertEquals(false, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData!!.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success with default selly THEN should render success with selly`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse =
            DataProvider.provideScheduleDeliveryRecommendedRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "SICEPATTEST",
                        shippingId = 10,
                        spId = 28,
                        cartStringGroup = "1"
                    )
                )
            )
        )

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            ratesWithScheduleUseCase(any())
        }
        assertEquals(true, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData!!.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success with no matching sp id with auto courier selection THEN should render success`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, isAutoCourierSelection = true, shippingId = 2, spId = 2)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            ratesWithScheduleUseCase(any())
        }
        assertEquals(false, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData!!.scheduleDeliveryUiModel!!.isSelected)
    }

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success with disable change courier THEN should hit validate use`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "WGOIN",
                        shippingId = 1,
                        spId = 1,
                        cartStringGroup = "1"
                    )
                )
            )
        )

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, isDisableChangeCourier = true, shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
        assertEquals("WGOIN", (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData!!.selectedShipper.logPromoCode)
    }

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success with no matching spid & disable change courier THEN should hit validate use`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "WGOIN",
                        shippingId = 1,
                        spId = 1,
                        cartStringGroup = "1"
                    )
                )
            )
        )

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, isDisableChangeCourier = true, shippingId = 0, spId = 0)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
        assertEquals("WGOIN", (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData!!.selectedShipper.logPromoCode)
    }

    @Test
    fun `WHEN get shipping rates services null and get schedule delivery rates success THEN should render failed`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3EmptyServicesResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            ratesWithScheduleUseCase(any())
        }
        assertEquals(null, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData)
    }

    @Test
    fun `WHEN get shipping rates services success and get schedule delivery rates null THEN should render failed`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData = null

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            ratesWithScheduleUseCase(any())
        }
        assertEquals(null, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData)
    }

    @Test
    fun `WHEN get shipping rates failed THEN should render failed`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData = null

        coEvery { ratesWithScheduleUseCase(any()) } throws IOException()

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            ratesWithScheduleUseCase(any())
        }
        assertEquals(null, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData)
    }

    @Test
    fun `WHEN get shipping rates schedule success with bo code THEN should hit validate use`() {
        // Given
        viewModel.logisticProcessor.isBoUnstackEnabled = true
        val ratesResponse = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "WGOIN",
                        shippingId = 1,
                        spId = 1,
                        cartStringGroup = "1"
                    )
                )
            )
        )

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, boCode = "WGOIN", shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN get shipping rates schedule got error courier with bo code THEN should not hit validate use`() {
        // Given
        viewModel.logisticProcessor.isBoUnstackEnabled = true
        val ratesResponse = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val courier =
            shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first { it.productData.shipperProductId == 28 }
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                errorMessage = "error"
            }
        )

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, boCode = "WGOIN", shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify(inverse = true) {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
        assertEquals(null, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData)
    }

    @Test
    fun `WHEN get shipping rates schedule got error courier THEN should not hit validate use`() {
        // Given
        viewModel.logisticProcessor.isBoUnstackEnabled = true
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        val courier =
            shippingRecommendationData.shippingDurationUiModels[3].shippingCourierViewModelList.first { it.productData.shipperProductId == 1 }
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                errorMessage = "error"
            }
        )

        coEvery { ratesWithScheduleUseCase(any()) } returns shippingRecommendationData

        val orderModel = CheckoutOrderModel("1", ratesValidationFlow = true, boCode = "WGOIN", shippingId = 1, spId = 1)
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
            CheckoutProductModel("1"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // When
        viewModel.loadShipping(orderModel, 5)

        // Then
        coVerify(inverse = true) {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
        assertEquals(null, (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData)
    }

    @Test
    fun set_selected_schedule_delivery() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        val orderModel = CheckoutOrderModel("123")
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        val scheduleDeliveryUiModel = ScheduleDeliveryUiModel(false)
        val courierItemData = CourierItemData(scheduleDeliveryUiModel = scheduleDeliveryUiModel)

        // when
        viewModel.setSelectedScheduleDelivery(
            5,
            orderModel,
            CourierItemData(),
            scheduleDeliveryUiModel,
            courierItemData
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
    }

    @Test
    fun set_selected_schedule_delivery_with_clear_old_promo() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(status = "OK", errorCode = "200")

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(true)
        )

        val orderModel = CheckoutOrderModel("123", boUniqueId = "12", shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "boCode")))
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123", cartStringOrder = "12"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(code = "boCode", "12", message = LastApplyMessageUiModel(state = "green"), type = "logistic", cartStringGroup = "123")
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        val scheduleDeliveryUiModel = ScheduleDeliveryUiModel(false)
        val courierItemData = CourierItemData(scheduleDeliveryUiModel = scheduleDeliveryUiModel)

        // when
        viewModel.setSelectedScheduleDelivery(
            5,
            orderModel,
            orderModel.shipment.courierItemData!!,
            scheduleDeliveryUiModel,
            courierItemData
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
        assertEquals(
            LastApplyUiModel(),
            (viewModel.listData.value[7] as CheckoutPromoModel).promo
        )
        coVerify(exactly = 1) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
    }

    @Test
    fun set_selected_schedule_delivery_with_clear_old_promo_and_apply_new() {
        // given
        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel("boCode2", "12", type = "logistic", messageUiModel = MessageUiModel(state = "green"), cartStringGroup = "123")
                )
            )
        )

        coEvery {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        } returns ClearPromoUiModel(
            SuccessDataUiModel(true)
        )

        val orderModel = CheckoutOrderModel("123", shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "boCode")))
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(
                promo = LastApplyUiModel(
                    voucherOrders = listOf(
                        LastApplyVoucherOrdersItemUiModel(code = "boCode", "12", message = LastApplyMessageUiModel(state = "green"), type = "logistic", cartStringGroup = "123")
                    )
                )
            ),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )
        val scheduleDeliveryUiModel = ScheduleDeliveryUiModel(true, deliveryProduct = DeliveryProduct(promoStacking = PromoStacking(disabled = false, promoCode = "boCode2")))
        val courierItemData = CourierItemData(scheduleDeliveryUiModel = scheduleDeliveryUiModel)

        // when
        viewModel.setSelectedScheduleDelivery(
            5,
            orderModel,
            orderModel.shipment.courierItemData!!,
            scheduleDeliveryUiModel,
            courierItemData
        )

        // then
        assertEquals(
            courierItemData,
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment.courierItemData
        )
        assertEquals(
            LastApplyUiModel(
                voucherOrders = listOf(
                    LastApplyVoucherOrdersItemUiModel(
                        code = "boCode2",
                        "12",
                        message = LastApplyMessageUiModel(state = "green"),
                        type = "logistic",
                        cartStringGroup = "123"
                    )
                )
            ),
            (viewModel.listData.value[7] as CheckoutPromoModel).promo
        )
        coVerify(exactly = 1) {
            clearCacheAutoApplyStackUseCase.setParams(any()).executeOnBackground()
        }
        coVerify(exactly = 2) {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun prepare_full_checkout_page() {
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
                    provinceName = "jakarta"
                }
            ),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", shippingId = 1, spId = 1),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        val shippingCourierUiModel = ShippingCourierUiModel(productData = ProductData(shipperId = 1, shipperProductId = 1))
        coEvery { ratesUseCase.invoke(any()) } returns ShippingRecommendationData(
            shippingDurationUiModels = listOf(
                ShippingDurationUiModel(
                    shippingCourierViewModelList = listOf(
                        shippingCourierUiModel
                    )
                )
            )
        )

        // when
        viewModel.prepareFullCheckoutPage()

        // then
        assertEquals(
            CheckoutOrderShipment(
                courierItemData = CourierItemData(
                    name = "",
                    estimatedTimeDelivery = "",
                    shipperFormattedPrice = "",
                    insuranceUsedInfo = "",
                    promoCode = "",
                    checksum = "",
                    ut = "",
                    now = false,
                    priorityInnactiveMessage = "",
                    priorityFormattedPrice = "",
                    priorityDurationMessage = "",
                    priorityCheckboxMessage = "",
                    priorityWarningboxMessage = "",
                    priorityFeeMessage = "",
                    priorityPdpMessage = "",
                    ontimeDelivery = OntimeDelivery(
                        textLabel = "",
                        textDetail = "",
                        urlDetail = ""
                    ),
                    codProductData = CashOnDeliveryProduct(0, "", 0, "", "", ""),
                    etaText = "",
                    etaErrorCode = -1,
                    merchantVoucherProductModel = MerchantVoucherProductModel(0),
                    isSelected = true,
                    shipperProductId = 1,
                    shipperId = 1
                ),
                shippingCourierUiModels = listOf(shippingCourierUiModel)
            ),
            (viewModel.listData.value[5] as CheckoutOrderModel).shipment
        )
    }

    @Test
    fun set_selected_courier_insurance() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(insurancePrice = 100))
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setSelectedCourierInsurance(true, orderModel, 5)

        // then
        assertEquals(
            listOf(
                CheckoutTickerErrorModel(errorMessage = ""),
                CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
                CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
                CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
                CheckoutProductModel("123"),
                CheckoutOrderModel(
                    "123",
                    shipment = CheckoutOrderShipment(courierItemData = CourierItemData(insurancePrice = 100), insurance = CheckoutOrderInsurance(true))
                ),
                CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
                CheckoutPromoModel(promo = LastApplyUiModel()),
                CheckoutCostModel(totalPrice = 100.0, totalPriceString = "Rp100", hasSelectAllShipping = true, shippingInsuranceFee = 100.0, totalOtherFee = 100.0),
                CheckoutCrossSellGroupModel(),
                CheckoutButtonPaymentModel(totalPrice = "Rp100", totalPriceNum = 100.0, enable = true, useInsurance = true)
            ),
            viewModel.listData.value
        )
    }

    @Test
    fun set_selected_courier_insurance_same_value() {
        // given
        val orderModel = CheckoutOrderModel(
            "123",
            shipment = CheckoutOrderShipment(courierItemData = CourierItemData(insurancePrice = 100))
        )

        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        viewModel.setSelectedCourierInsurance(false, orderModel, 5)

        // then
        assertEquals(
            listOf(
                CheckoutTickerErrorModel(errorMessage = ""),
                CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
                CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
                CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
                CheckoutProductModel("123"),
                CheckoutOrderModel(
                    "123",
                    shipment = CheckoutOrderShipment(courierItemData = CourierItemData(insurancePrice = 100), insurance = CheckoutOrderInsurance())
                ),
                CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
                CheckoutPromoModel(promo = LastApplyUiModel()),
                CheckoutCostModel(),
                CheckoutCrossSellGroupModel(),
                CheckoutButtonPaymentModel()
            ),
            viewModel.listData.value
        )
    }

    @Test
    fun is_loading() {
        // when
        val result = viewModel.isLoading()

        // then
        assertEquals(false, result)
    }

    @Test
    fun is_loading_true() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(isLoading = true)),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        val result = viewModel.isLoading()

        // then
        assertEquals(true, result)
    }

    @Test
    fun is_loading_false() {
        // given
        viewModel.listData.value = listOf(
            CheckoutTickerErrorModel(errorMessage = ""),
            CheckoutTickerModel(ticker = TickerAnnouncementHolderData()),
            CheckoutAddressModel(recipientAddressModel = RecipientAddressModel()),
            CheckoutUpsellModel(upsell = ShipmentNewUpsellModel()),
            CheckoutProductModel("123"),
            CheckoutOrderModel("123", shipment = CheckoutOrderShipment(isLoading = false)),
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        // when
        val result = viewModel.isLoading()

        // then
        assertEquals(false, result)
    }

    @Test
    fun do_validate_bo() {
        // given
        val orderModel = CheckoutOrderModel("123")
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
            CheckoutProductModel("123", cartStringOrder = "12"),
            orderModel,
            CheckoutEpharmacyModel(epharmacy = UploadPrescriptionUiModel()),
            CheckoutPromoModel(promo = LastApplyUiModel()),
            CheckoutCostModel(),
            CheckoutCrossSellGroupModel(),
            CheckoutButtonPaymentModel()
        )

        viewModel.logisticProcessor.isBoUnstackEnabled = true

        coEvery {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        } returns ValidateUsePromoRevampUiModel(
            status = "OK",
            errorCode = "200",
            promoUiModel = PromoUiModel(
                voucherOrderUiModels = listOf(
                    PromoCheckoutVoucherOrdersItemUiModel(
                        code = "boCode",
                        uniqueId = "12",
                        cartStringGroup = "123",
                        type = "logistic",
                        messageUiModel = MessageUiModel(state = "green")
                    )
                )
            )
        )

        // when
        viewModel.doValidateUseLogisticPromoNew(
            5,
            "123",
            "boCode",
            true,
            CourierItemData(logPromoCode = "boCode"),
            InsuranceData()
        )

        // then
        assertEquals(CheckoutOrderModel("123", boUniqueId = "12", shipment = CheckoutOrderShipment(courierItemData = CourierItemData(logPromoCode = "boCode"))), (viewModel.listData.value[5] as CheckoutOrderModel))
    }
}
