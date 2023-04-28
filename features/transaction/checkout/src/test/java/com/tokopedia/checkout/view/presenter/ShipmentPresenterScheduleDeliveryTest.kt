package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
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

class ShipmentPresenterScheduleDeliveryTest : BaseShipmentPresenterTest() {

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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
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
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)

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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
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
        presenter.shipmentCartItemModelList = listOf(shipmentCartItemModel)

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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
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
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc, "", emptyList()
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
        }
    }
}
