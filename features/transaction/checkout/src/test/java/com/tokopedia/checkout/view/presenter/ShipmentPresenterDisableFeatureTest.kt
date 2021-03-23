package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.UnitTestFileUtils
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormGqlResponse
import com.tokopedia.checkout.data.model.response.shipment_address_form.ShipmentAddressFormResponse
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.logisticCommon.data.analytics.CodAnalytics
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.domain.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.helpticket.domain.usecase.SubmitHelpTicketUseCase
import com.tokopedia.purchase_platform.common.feature.localizationchooseaddress.request.ChosenAddressRequestHelper
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.purchase_platform.common.utils.each
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.lang.reflect.Type
import java.util.*

const val PATH_JSON_SAF_DISABLE_DROPSHIPPER = "assets/saf_disable_dropshipper.json"
const val PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS = "assets/saf_disable_order_prioritas.json"
const val PATH_JSON_SAF_DISABLE_EGOLD = "assets/saf_disable_egold.json"
const val PATH_JSON_SAF_DISABLE_PPP = "assets/saf_disable_ppp.json"
const val PATH_JSON_SAF_DISABLE_DONATION = "assets/saf_disable_donation.json"
const val PATH_JSON_SAF_DISABLE_ALL = "assets/saf_disable_all.json"

class ShipmentPresenterDisableFeatureTest {

    @MockK
    private lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    private lateinit var compositeSubscription: CompositeSubscription

    @MockK
    private lateinit var checkoutUseCase: CheckoutGqlUseCase

    @MockK(relaxUnitFun = true)
    private lateinit var graphqlUseCase: GraphqlUseCase

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

    @MockK
    private lateinit var ratesStatesConverter: RatesResponseStateConverter

    @MockK
    private lateinit var shippingCourierConverter: ShippingCourierConverter

    @MockK(relaxed = true)
    private lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    private lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK
    private lateinit var codAnalytics: CodAnalytics

    @MockK
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK
    private lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    private var shipmentDataConverter = ShipmentDataConverter()

    private val gson = Gson()
    private val unitTestFileUtils = UnitTestFileUtils()
    private lateinit var getShipmentAddressFormGqlUseCase: GetShipmentAddressFormGqlUseCase

    private lateinit var presenter: ShipmentPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        getShipmentAddressFormGqlUseCase = GetShipmentAddressFormGqlUseCase("", graphqlUseCase, ShipmentMapper(), TestSchedulers, chosenAddressRequestHelper)
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
    fun disable_dropshipper() {
        // Given
        val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DROPSHIPPER), ShipmentAddressFormDataResponse::class.java)
        val result = hashMapOf<Type, Any>(
                ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
        )
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
        every { chosenAddressRequestHelper.addChosenAddressParam(any()) } returns RequestParams.create()

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        presenter.shipmentCartItemModelList.each { assertEquals(true, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_order_prioritas() {
        // Given
        val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ORDER_PRIORITAS), ShipmentAddressFormDataResponse::class.java)
        val result = hashMapOf<Type, Any>(
                ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
        )
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
        every { chosenAddressRequestHelper.addChosenAddressParam(any()) } returns RequestParams.create()

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { assertEquals(true, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_egold() {
        // Given
        val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_EGOLD), ShipmentAddressFormDataResponse::class.java)
        val result = hashMapOf<Type, Any>(
                ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
        )
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
        every { chosenAddressRequestHelper.addChosenAddressParam(any()) } returns RequestParams.create()

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
        assertNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_ppp() {
        // Given
        val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_PPP), ShipmentAddressFormDataResponse::class.java)
        val result = hashMapOf<Type, Any>(
                ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
        )
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
        every { chosenAddressRequestHelper.addChosenAddressParam(any()) } returns RequestParams.create()

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(false, isProtectionAvailable) } }
        assertNotNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_donation() {
        // Given
        val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_DONATION), ShipmentAddressFormDataResponse::class.java)
        val result = hashMapOf<Type, Any>(
                ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
        )
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
        every { chosenAddressRequestHelper.addChosenAddressParam(any()) } returns RequestParams.create()

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        presenter.shipmentCartItemModelList.each { assertEquals(false, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { assertEquals(false, isOrderPrioritasDisable) }
        assertNotNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(true, isProtectionAvailable) } }
        assertNull(presenter.shipmentDonationModel)
    }

    @Test
    fun disable_all() {
        // Given
        val dataResponse = gson.fromJson(unitTestFileUtils.getJsonFromAsset(PATH_JSON_SAF_DISABLE_ALL), ShipmentAddressFormDataResponse::class.java)
        val result = hashMapOf<Type, Any>(
                ShipmentAddressFormGqlResponse::class.java to ShipmentAddressFormGqlResponse(ShipmentAddressFormResponse(status = "OK", data = dataResponse))
        )
        every { graphqlUseCase.createObservable(any()) } returns Observable.just(GraphqlResponse(result, HashMap<Type, List<GraphqlError>>(), false))
        every { chosenAddressRequestHelper.addChosenAddressParam(any()) } returns RequestParams.create()

        // When
        presenter.processInitialLoadCheckoutPage(false, false, false, false, false, null, "", "")

        // Then
        presenter.shipmentCartItemModelList.each { assertEquals(true, isDropshipperDisable) }
        presenter.shipmentCartItemModelList.each { assertEquals(true, isOrderPrioritasDisable) }
        assertNull(presenter.egoldAttributeModel)
        presenter.shipmentCartItemModelList.each { cartItemModels.each { assertEquals(false, isProtectionAvailable) } }
        assertNull(presenter.shipmentDonationModel)
    }
}