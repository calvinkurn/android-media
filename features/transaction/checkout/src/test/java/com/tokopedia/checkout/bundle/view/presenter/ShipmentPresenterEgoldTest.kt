package com.tokopedia.checkout.bundle.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.bundle.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.bundle.domain.usecase.*
import com.tokopedia.checkout.bundle.view.ShipmentContract
import com.tokopedia.checkout.bundle.view.ShipmentPresenter
import com.tokopedia.checkout.bundle.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.bundle.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.bundle.view.uimodel.EgoldTieringModel
import com.tokopedia.checkout.bundle.view.uimodel.ShipmentCostModel
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterEgoldTest {

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

    @MockK
    private lateinit var getRatesUseCase: GetRatesUseCase

    @MockK
    private lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK
    private lateinit var clearCacheAutoApplyStackUseCase: OldClearCacheAutoApplyStackUseCase

    @MockK
    private lateinit var ratesStatesConverter: RatesResponseStateConverter

    @MockK
    private lateinit var shippingCourierConverter: ShippingCourierConverter

    @MockK(relaxed = true)
    private lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    private lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK(relaxed = true)
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK(relaxed = true)
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    private var shipmentDataConverter = ShipmentDataConverter()

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
                checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase,
                validateUsePromoRevampUseCase, gson, TestSchedulers)
        presenter.attachView(view)
    }

    @Test
    fun `WHEN calculate egold value without tiering THEN should correctly calculated`() {
        // Given
        val expectedEgoldValue = 250L
        presenter.shipmentCostModel = ShipmentCostModel().apply {
            totalPrice = 99750.0
        }
        presenter.egoldAttributeModel = EgoldAttributeModel().apply {
            isTiering = false
            minEgoldRange = 50
            maxEgoldRange = 1000
        }

        // When
        presenter.updateEgoldBuyValue()

        // Then
        verify {
            view.renderDataChanged()
        }
        assert(presenter.egoldAttributeModel.buyEgoldValue == expectedEgoldValue)
    }

    @Test
    fun `WHEN calculate egold value with tiering THEN should correctly calculated`() {
        // Given
        val expectedEgoldValue = 5700L
        presenter.shipmentCostModel = ShipmentCostModel().apply {
            totalPrice = 50300.0
        }
        presenter.egoldAttributeModel = EgoldAttributeModel().apply {
            isTiering = true
            minEgoldRange = 50
            maxEgoldRange = 1000
            egoldTieringModelArrayList = arrayListOf<EgoldTieringModel>().apply {
                add(EgoldTieringModel().apply {
                    minTotalAmount = 500
                    minAmount = 2000
                    maxAmount = 5999
                    basisAmount = 4000
                })
                add(EgoldTieringModel().apply {
                    minTotalAmount = 98001
                    minAmount = 2000
                    maxAmount = 11999
                    basisAmount = 10000
                })
                add(EgoldTieringModel().apply {
                    minTotalAmount = 998001
                    minAmount = 2000
                    maxAmount = 51999
                    basisAmount = 50000
                })
            }
        }

        // When
        presenter.updateEgoldBuyValue()

        // Then
        verify {
            view.renderDataChanged()
        }
        assert(presenter.egoldAttributeModel.buyEgoldValue == expectedEgoldValue)
    }

}