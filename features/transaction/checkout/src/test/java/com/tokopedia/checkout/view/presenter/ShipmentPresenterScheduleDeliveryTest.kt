package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.DataProvider
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationConverter
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterScheduleDeliveryTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: OldValidateUsePromoRevampUseCase

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

    @MockK(relaxed = true)
    private lateinit var getRatesUseCase: GetRatesUseCase

    @MockK(relaxed = true)
    private lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK(relaxed = true)
    private lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

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
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

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
            compositeSubscription, checkoutUseCase, getShipmentAddressFormV3UseCase,
            editAddressUseCase, changeShippingAddressGqlUseCase, saveShipmentStateGqlUseCase,
            getRatesUseCase, getRatesApiUseCase, clearCacheAutoApplyStackUseCase,
            ratesStatesConverter, shippingCourierConverter,
            shipmentAnalyticsActionListener, userSessionInterface, analyticsPurchaseProtection,
            checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
            validateUsePromoRevampUseCase, gson, TestSchedulers, eligibleForAddressUseCase,
            getRatesWithScheduleUseCase
        )
        presenter.attachView(view)
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
        val shipmentCartItemModel = ShipmentCartItemModel(
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
            shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
            recipientAddressModel, isForceReload, skipMvc
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateSuccess(any(), itemPosition, isTradeInDropOff, isForceReload)
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
        val shipmentCartItemModel = ShipmentCartItemModel(
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
            shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
            recipientAddressModel, isForceReload, skipMvc
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
        val shipmentCartItemModel = ShipmentCartItemModel(
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
            shopShipmentList, isInitialLoad, products, cartString, isTradeInDropOff,
            recipientAddressModel, isForceReload, skipMvc
        )

        // Then
        verify {
            getRatesWithScheduleUseCase.execute(any(), any())
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
        }
    }
}
