package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import org.junit.Test
import rx.Observable

class ShipmentViewModelScheduleDeliveryTest : BaseShipmentViewModelTest() {

    private var shippingDurationConverter = ShippingDurationConverter()

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success THEN should render success`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "",
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates and schedule delivery rates success with default selly THEN should render success with selly`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRecommendedRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

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

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            ratesValidationFlow = true
        )
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.setSelectedCourier(itemPosition, match { it.scheduleDeliveryUiModel!!.isSelected }, true, false)
        }
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

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 0
        val spId = 0
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "",
            ratesValidationFlow = true,
            isAutoCourierSelection = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
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

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            ratesValidationFlow = true,
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
            isDisableChangeCourier = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

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

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
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

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 0
        val spId = 0
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            ratesValidationFlow = true,
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
            isDisableChangeCourier = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

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

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
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

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "",
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
        }
    }

    @Test
    fun `WHEN get shipping rates services success and get schedule delivery rates null THEN should render failed`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData = null

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "",
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
        }
    }

    @Test
    fun `WHEN get shipping rates failed THEN should render failed`() {
        // Given
        val ratesResponse = DataProvider.provideRatesV3Response()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData = null

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.error(
            Throwable()
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "",
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
        }
    }

    @Test
    fun `WHEN get shipping rates schedule success with bo code THEN should hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = true
        val ratesResponse = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            boCode = "WGOIN",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

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

        val isTradeInByDropOff = false
        every { view.isTradeInByDropOff } returns isTradeInByDropOff
        viewModel.recipientAddressModel = RecipientAddressModel()
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        coVerify {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
    }

    @Test
    fun `WHEN get shipping rates schedule got error courier with bo code THEN should not hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = true
        val ratesResponse = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first { it.productData.shipperProductId == 28 }.productData.error =
            ErrorProductData().apply {
                errorMessage = "error"
            }

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            boCode = "WGOIN",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        val isTradeInByDropOff = false
        every { view.isTradeInByDropOff } returns isTradeInByDropOff
        viewModel.recipientAddressModel = RecipientAddressModel()
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        coVerify(inverse = true) {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
        verify { view.renderCourierStateFailed(any(), any(), any()) }
    }

    @Test
    fun `WHEN get shipping rates schedule got error courier THEN should not hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = true
        val ratesResponse = DataProvider.provideRatesV3Response()
        val ratesScheduleDeliveryResponse = DataProvider.provideScheduleDeliveryRatesResponse()
        val shippingRecommendationData =
            shippingDurationConverter.convertModel(ratesResponse.ratesData)
        shippingRecommendationData.scheduleDeliveryData =
            ratesScheduleDeliveryResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
        shippingRecommendationData.shippingDurationUiModels[3].shippingCourierViewModelList.first { it.productData.shipperProductId == 1 }.productData.error =
            ErrorProductData().apply {
                errorMessage = "error"
            }

        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.just(
            shippingRecommendationData
        )

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData(
                originDistrictId = "1",
                originPostalCode = "1",
                originLatitude = "1",
                originLongitude = "1",
                destinationDistrictId = "1",
                destinationPostalCode = "1",
                destinationLatitude = "1",
                destinationLongitude = "1",
                token = "1",
                ut = "1",
                insurance = 1,
                productInsurance = 1,
                orderValue = 1,
                categoryIds = "",
                preOrderDuration = 0,
                isFulfillment = false
            )
        }
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
            ratesValidationFlow = true
        )
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = true

        val isTradeInByDropOff = false
        every { view.isTradeInByDropOff } returns isTradeInByDropOff
        viewModel.recipientAddressModel = RecipientAddressModel()
        viewModel.shipmentCartItemModelList = listOf(shipmentCartItemModel)

        // When
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        coVerify(inverse = true) {
            validateUsePromoRevampUseCase.setParam(any()).executeOnBackground()
        }
        verify { view.renderCourierStateFailed(any(), any(), any()) }
    }
}
