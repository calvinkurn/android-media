package com.tokopedia.checkout.view.presenter

import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoCheckoutVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.ValidateUsePromoRevampUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import rx.Observable

class ShipmentViewModelGetShippingRatesTest : BaseShipmentViewModelTest() {

    private var shippingDurationConverter = ShippingDurationConverter()

    @Test
    fun `WHEN get shipping rates failed THEN should render failed`() {
        // Given
        every { getRatesUseCase.execute(any()) } returns Observable.error(Throwable())

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
            view.renderCourierStateFailed(any(), any(), any())
        }
    }

    @Test
    fun `WHEN get shipping rates failed with akamai THEN should render failed`() {
        // Given
        val exception = AkamaiErrorException("akamai")
        every { getRatesUseCase.execute(any()) } returns Observable.error(exception)

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
            view.renderCourierStateFailed(any(), any(), any())
            view.logOnErrorLoadCourier(exception, any(), any())
            view.showToastErrorAkamai("akamai")
        }
    }

    @Test
    fun `WHEN get shipping rates success with empty data THEN should render failed`() {
        // Given
        every { getRatesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
            view.renderCourierStateFailed(any(), any(), any())
        }
    }

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates success with ui hidden and no bo promo THEN should render failed`() {
        // Given
        val response = DataProvider.provideRatesV3Response()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        shippingRecommendationData.shippingDurationUiModels[3].shippingCourierViewModelList.first { it.productData.shipperProductId == 1 }
            .apply {
                productData = productData.copy(isUiRatesHidden = true)
                serviceData = serviceData.copy(selectedShipperProductId = 0)
            }

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
            view.logOnErrorLoadCourier(
                match { it.message == "rates ui hidden but no promo" },
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN get shipping rates success with selected sp id THEN should render success with selected sp id`() {
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates success with auto courier selection THEN should render success`() {
        // Given
        viewModel.isBoUnstackEnabled = false
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

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
            boCode = "WGOIN",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
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
        verify {
            view.renderCourierStateSuccess(any(), any(), any())
        }
    }

    @Test
    fun `WHEN get shipping rates success with auto courier selection & disabled change courier THEN should hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = false
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

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
            boCode = "WGOIN",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            ),
            isAutoCourierSelection = true,
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
        viewModel.shipmentCartItemModelList =
            listOf(ShipmentCartItemTopModel(cartStringGroup = "111"), shipmentCartItemModel)

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
    fun `WHEN get shipping rates success with bo unstack disabled & matching bo sp id THEN should hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = false
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        val shipperId = 10
        val spId = 28
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
            )
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
    fun `WHEN get shipping rates success with bo code THEN should hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = true
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
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
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            boCode = "WGOIN",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            )
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
    fun `WHEN get shipping rates got error courier with bo code THEN should not hit validate use`() {
        // Given
        viewModel.isBoUnstackEnabled = true
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        val courier =
            shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first { it.productData.shipperProductId == 28 }
        courier.productData = courier.productData.copy(
            error = ErrorProductData().apply {
                errorMessage = "error"
            }
        )

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
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            boCode = "WGOIN",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            )
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
    fun `WHEN get shipping rates got error courier without bo code THEN should render failed`() {
        // Given
        val response = DataProvider.provideRatesV3EnabledBoPromoResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        val courier =
            shippingRecommendationData.shippingDurationUiModels[3].shippingCourierViewModelList.first { it.productData.shipperProductId == 1 }
        courier.productData = courier.productData.copy(
            error = ErrorProductData().apply {
                errorMessage = "error"
            }
        )

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
        val shipmentCartItemModel = ShipmentCartItemModel(
            cartStringGroup = "1",
            cartItemModels = listOf(
                CartItemModel(cartStringGroup = "1")
            )
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket success with ui hidden and no bo promo THEN should render failed`() {
        // Given
        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first()
            .apply {
                productData = productData.copy(isUiRatesHidden = true)
                serviceData = serviceData.copy(selectedShipperProductId = 0)
            }

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
            view.logOnErrorLoadCourier(
                match { it.message == "rates ui hidden but no promo" },
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket failed THEN should render failed`() {
        // Given
        val throwable = Throwable()

        every { getRatesApiUseCase.execute(any()) } returns Observable.error(
            throwable
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket failed with akamai THEN should render failed and show toaster`() {
        // Given
        val throwable = AkamaiErrorException("akamai")

        every { getRatesApiUseCase.execute(any()) } returns Observable.error(
            throwable
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
            view.logOnErrorLoadCourier(throwable, itemPosition, any())
            view.showToastErrorAkamai("akamai")
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket return empty data THEN should render failed`() {
        // Given
        every { getRatesApiUseCase.execute(any()) } returns Observable.just(
            ShippingRecommendationData()
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket return error THEN should render failed`() {
        // Given
        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        val courier =
            shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first()
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply {
                errorMessage = "error"
            }
        )

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
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
        viewModel.processGetCourierRecommendation(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc success THEN should render success`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3Response()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesUseCase.execute(any()) } returns Observable.just(
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc success with ui hidden and no bo promo THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3Response()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        shippingRecommendationData.shippingDurationUiModels[3].shippingCourierViewModelList.first { it.productData.shipperProductId == 1 }
            .apply {
                productData = productData.copy(isUiRatesHidden = true)
                serviceData = serviceData.copy(selectedShipperProductId = 0)
            }

        every { getRatesUseCase.execute(any()) } returns Observable.just(
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
            view.logOnErrorLoadCourier(
                match { it.message == "rates ui hidden but no promo" },
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc return error THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3Response()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        val courier =
            shippingRecommendationData.shippingDurationUiModels[3].shippingCourierViewModelList.first { it.productData.shipperProductId == 1 }
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply { errorMessage = "error" }
        )

        every { getRatesUseCase.execute(any()) } returns Observable.just(
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc return empty data THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        every { getRatesUseCase.execute(any()) } returns Observable.just(
            ShippingRecommendationData()
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc failed THEN should render nothing`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        every { getRatesUseCase.execute(any()) } returns Observable.error(
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify(inverse = true) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc failed with akamai THEN should render nothing`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val exception = AkamaiErrorException("akamai")
        every { getRatesUseCase.execute(any()) } returns Observable.error(
            exception
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify(inverse = true) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
        verify {
            view.logOnErrorLoadCourier(exception, itemPosition, any())
            view.showToastErrorAkamai("akamai")
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc success THEN should render success`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc success with ui hidden and no promo code THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first()
            .apply {
                productData = productData.copy(isUiRatesHidden = true)
                serviceData = serviceData.copy(selectedShipperProductId = 0)
            }

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(any(), isTradeInDropOff, false)
            view.logOnErrorLoadCourier(
                match { it.message == "rates ui hidden but no promo" },
                any(),
                any()
            )
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc return error THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)
        val courier =
            shippingRecommendationData.shippingDurationUiModels.first().shippingCourierViewModelList.first()
        courier.productData = courier.productData.copy(
            error =
            ErrorProductData().apply { errorMessage = "error" }
        )

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc return empty data THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        every { getRatesApiUseCase.execute(any()) } returns Observable.just(
            ShippingRecommendationData()
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc return empty courier data THEN should render failed`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        every { getRatesApiUseCase.execute(any()) } returns Observable.just(
            ShippingRecommendationData(shippingDurationUiModels = listOf(ShippingDurationUiModel()))
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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc failed THEN should render nothing`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        every { getRatesApiUseCase.execute(any()) } returns Observable.error(Throwable())

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify(inverse = true) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
    }

    @Test
    fun `WHEN get shipping rates api with mvc failed with akamai THEN should render nothing`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val exception = AkamaiErrorException("akamai")
        every { getRatesApiUseCase.execute(any()) } returns Observable.error(exception)

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
        val shipmentCartItemModel = ShipmentCartItemModel(cartStringGroup = "")
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = true
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify(inverse = true) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, any())
        }
        verify {
            view.logOnErrorLoadCourier(exception, itemPosition, any())
            view.showToastErrorAkamai("akamai")
        }
    }

    @Test
    fun `WHEN get shipping rates schedule with mvc success THEN should render success`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

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
        val shipmentCartItemModel =
            ShipmentCartItemModel(cartStringGroup = "", ratesValidationFlow = true)
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
    }

    @Test
    fun `WHEN get shipping rates schedule with mvc failed with akamai THEN should render nothing`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        viewModel.validateUsePromoRevampUiModel =
            ValidateUsePromoCheckoutMapper
                .mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val exception = AkamaiErrorException("akamai")
        every { getRatesWithScheduleUseCase.execute(any(), any()) } returns Observable.error(
            exception
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
        val shipmentCartItemModel =
            ShipmentCartItemModel(cartStringGroup = "", ratesValidationFlow = true)
        val shopShipmentList = ArrayList<ShopShipment>()
        val products = ArrayList<Product>()
        val cartString = "123-abc"
        val isTradeInDropOff = false
        val recipientAddressModel = RecipientAddressModel().apply {
            id = "1"
        }
        val skipMvc = false

        // When
        viewModel.processGetCourierRecommendationMvc(
            shipperId, spId, itemPosition, shipmentDetailData, shipmentCartItemModel,
            shopShipmentList, products, cartString, isTradeInDropOff,
            recipientAddressModel, skipMvc
        )

        // Then
        verify(inverse = true) {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff)
        }
        verify {
            view.logOnErrorLoadCourier(exception, itemPosition, any())
            view.showToastErrorAkamai("akamai")
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

//        every { view.renderCourierStateSuccess(any(), any(), any()) } answers {
//            presenter.logisticDonePublisher!!.onCompleted()
//        }

        // When get first shipping
        viewModel.processGetCourierRecommendation(
            shipperId,
            spId,
            itemPosition,
            shipmentDetailData,
            ShipmentCartItemModel(cartStringGroup = "", orderNumber = itemPosition),
            shopShipmentList,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel
        )

        itemPosition++

        // When get second shipping
        viewModel.processGetCourierRecommendation(
            shipperId,
            spId,
            itemPosition,
            shipmentDetailData,
            ShipmentCartItemModel(cartStringGroup = "", orderNumber = itemPosition),
            shopShipmentList,
            products,
            cartString,
            isTradeInDropOff,
            recipientAddressModel
        )

        // Then
        assertNotNull(viewModel.getShippingCourierViewModelsState(itemPosition))
        assertNotNull(viewModel.getShippingCourierViewModelsState(itemPosition - 1))

        assertNull(viewModel.getShippingCourierViewModelsState(itemPosition - 2))
        assertNull(viewModel.getShippingCourierViewModelsState(itemPosition + 1))
    }

    @Test
    fun `WHEN get shipping courier view model state before get rates THEN should return null`() {
        assertNull(viewModel.getShippingCourierViewModelsState(0))
    }
}
