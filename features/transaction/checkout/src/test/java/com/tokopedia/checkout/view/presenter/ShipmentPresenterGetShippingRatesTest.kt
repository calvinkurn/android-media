package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.mapper.ValidateUsePromoCheckoutMapper
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterGetShippingRatesTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    private lateinit var compositeSubscription: CompositeSubscription

    @MockK
    private lateinit var checkoutUseCase: CheckoutGqlUseCase

    @MockK
    private lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    private lateinit var changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase

    @MockK
    private lateinit var saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase

    @MockK
    private lateinit var getRatesUseCase: GetRatesUseCase

    @MockK
    private lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    private lateinit var submitHelpTicketUseCase: SubmitHelpTicketUseCase

    @MockK(relaxed = true)
    private lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    private lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK(relaxed = true)
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase

    private var shipmentDataConverter = ShipmentDataConverter()
    private var ratesStatesConverter = RatesResponseStateConverter()
    private var shippingCourierConverter = ShippingCourierConverter()
    private var shippingDurationConverter = ShippingDurationConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
                compositeSubscription, checkoutUseCase, getShipmentAddressFormGqlUseCase,
                editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
                getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
                submitHelpTicketUseCase, ratesStatesConverter, shippingCourierConverter,
                shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
                checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers)
        presenter.attachView(view)
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
            shipmentCartData = ShipmentCartData().apply {
                originDistrictId = "1"
                originPostalCode = "1"
                originLatitude = "1"
                originLongitude = "1"
                destinationDistrictId = "1"
                destinationPostalCode = "1"
                destinationLatitude = "1"
                destinationLongitude = "1"
                token = "1"
                ut = "1"
                insurance = 1
                productInsurance = 1
                orderValue = 1
                categoryIds = ""
                preOrderDuration = 0
                isFulfillment = false
            }
        }
        val shipmentCartItemModel = ShipmentCartItemModel()
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
                recipientAddressModel, isForceReload, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload);
        }
    }

    @Test
    fun `WHEN get shipping rates for trade in indopaket success THEN should render success`() {
        // Given
        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesApiUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData().apply {
                originDistrictId = "1"
                originPostalCode = "1"
                originLatitude = "1"
                originLongitude = "1"
                destinationDistrictId = "1"
                destinationPostalCode = "1"
                destinationLatitude = "1"
                destinationLongitude = "1"
                token = "1"
                ut = "1"
                insurance = 1
                productInsurance = 1
                orderValue = 1
                categoryIds = ""
                preOrderDuration = 0
                isFulfillment = false
            }
        }
        val shipmentCartItemModel = ShipmentCartItemModel()
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
                recipientAddressModel, isForceReload, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload);
        }
    }

    @Test
    fun `WHEN get shipping rates with mvc success THEN should render success`() {
        // Given
        val validateUseResponse = DataProvider.provideValidateUseResponse()
        presenter.validateUsePromoRevampUiModel = ValidateUsePromoCheckoutMapper.mapToValidateUseRevampPromoUiModel(validateUseResponse.validateUsePromoRevamp)

        val response = DataProvider.provideRatesV3apiResponse()
        val shippingRecommendationData = shippingDurationConverter.convertModel(response.ratesData)

        every { getRatesApiUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)

        val shipperId = 1
        val spId = 1
        val itemPosition = 1
        val shipmentDetailData = ShipmentDetailData().apply {
            shopId = "1"
            isBlackbox = true
            preorder = false
            shipmentCartData = ShipmentCartData().apply {
                originDistrictId = "1"
                originPostalCode = "1"
                originLatitude = "1"
                originLongitude = "1"
                destinationDistrictId = "1"
                destinationPostalCode = "1"
                destinationLatitude = "1"
                destinationLongitude = "1"
                token = "1"
                ut = "1"
                insurance = 1
                productInsurance = 1
                orderValue = 1
                categoryIds = ""
                preOrderDuration = 0
                isFulfillment = false
            }
        }
        val shipmentCartItemModel = ShipmentCartItemModel()
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
                recipientAddressModel, isForceReload, skipMvc
        )

        // Then
        verify {
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload);
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
            shipmentCartData = ShipmentCartData().apply {
                originDistrictId = "1"
                originPostalCode = "1"
                originLatitude = "1"
                originLongitude = "1"
                destinationDistrictId = "1"
                destinationPostalCode = "1"
                destinationLatitude = "1"
                destinationLongitude = "1"
                token = "1"
                ut = "1"
                insurance = 1
                productInsurance = 1
                orderValue = 1
                categoryIds = ""
                preOrderDuration = 0
                isFulfillment = false
            }
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

        // When get first shipping
        presenter.processGetCourierRecommendation(
                shipperId, spId, itemPosition, shipmentDetailData, ShipmentCartItemModel().apply { orderNumber = itemPosition },
                shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
                recipientAddressModel, isForceReload, skipMvc
        )

        itemPosition++

        // When get second shipping
        presenter.processGetCourierRecommendation(
                shipperId, spId, itemPosition, shipmentDetailData, ShipmentCartItemModel().apply { orderNumber = itemPosition },
                shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
                recipientAddressModel, isForceReload, skipMvc
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