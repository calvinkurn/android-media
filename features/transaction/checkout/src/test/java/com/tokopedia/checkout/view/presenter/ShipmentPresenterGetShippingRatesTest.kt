package com.tokopedia.checkout.view.presenter

import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import rx.Observable

class ShipmentPresenterGetShippingRatesTest : BaseShipmentPresenterTest() {

    private var shippingDurationConverter = ShippingDurationConverter()

    @Test
    fun `WHEN get shipping rates success THEN should render success`() {
        // Given
        val response = DataProvider.provideRatesV3Response()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "")
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
            shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
            recipientAddressModel, isForceReload, skipMvc, "", emptyList()
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload)
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket success THEN should render success`() {
        // Given
        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesApiUseCase.execute(any()) } returns Observable.just(
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
            locationDataModel = LocationDataModel().apply {
                district = "1"
                postalCode = "1"
                latitude = "1"
                longitude = "1"
            }
        }
        val isForceReload = false
        val skipMvc = true

        // When
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
            recipientAddressModel, isForceReload, skipMvc, "", emptyList()
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload)
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc success THEN should render success`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        presenter.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesApiUseCase.execute(any()) } returns Observable.just(
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartString = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val isInitialLoad = true
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val isForceReload = false
        val skipMvc = false

        // When
        presenter.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
            recipientAddressModel, isForceReload, skipMvc, "", emptyList()
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload)
        }
    }

    @Test
    fun `WHEN get shipping rates success twice THEN should have exactly two shipping courier view models states`() {
        // Given
        val response = DataProvider.provideRatesV3Response()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        val shipperId = 1
        val spId = 1
        var itemPosition = 1
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

        every { view.renderCourierStateSuccess(any(), any(), any(), any()) } answers {
            presenter.logisticDonePublisher!!.onCompleted()
        }

        // When get first shipping
        presenter.processGetCourierRecommendation(
            shipperId,
            spId,
            itemPosition,
            shipmentDetailData,
            ShipmentCartItemModel(cartString = "", orderNumber = itemPosition),
            shopShipmentList,
            isInitialLoad,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel,
            isForceReload,
            skipMvc,
            "",
            emptyList()
        )

        itemPosition++

        // When get second shipping
        presenter.processGetCourierRecommendation(
            shipperId,
            spId,
            itemPosition,
            shipmentDetailData,
            ShipmentCartItemModel(cartString = "", orderNumber = itemPosition),
            shopShipmentList,
            isInitialLoad,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel,
            isForceReload,
            skipMvc,
            "",
            emptyList()
        )

        // Then
        assertNotNull(presenter.getShippingCourierViewModelsState(itemPosition))
        assertNotNull(presenter.getShippingCourierViewModelsState(itemPosition - 1))

        assertNull(presenter.getShippingCourierViewModelsState(itemPosition - 2))
        assertNull(presenter.getShippingCourierViewModelsState(itemPosition + 1))
    }

    @Test
    fun `WHEN get shipping courier view model state before get rates THEN should return null`() {
        assertNull(presenter.getShippingCourierViewModelsState(0))
    }
}
