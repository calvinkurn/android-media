package com.tokopedia.checkout.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import rx.subscriptions.CompositeSubscription

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseShipmentPresenterTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    lateinit var compositeSubscription: CompositeSubscription

    @MockK
    lateinit var checkoutUseCase: CheckoutUseCase

    @MockK
    lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    lateinit var changeShippingAddressGqlUseCase: ChangeShippingAddressGqlUseCase

    @MockK
    lateinit var saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase

    @MockK
    lateinit var getRatesUseCase: GetRatesUseCase

    @MockK
    lateinit var getRatesApiUseCase: GetRatesApiUseCase

    @MockK
    lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

    @MockK(relaxUnitFun = true)
    lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    lateinit var ratesStatesConverter: RatesResponseStateConverter

    @MockK
    lateinit var shippingCourierConverter: ShippingCourierConverter

    @MockK(relaxed = true)
    lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK
    lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    lateinit var getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase

    @MockK(relaxed = true)
    lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine

    @MockK(relaxed = true)
    lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK(relaxed = true)
    lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    val gson = Gson()

    private val shipmentDataRequestConverter = ShipmentDataRequestConverter(gson)

    private val shipmentDataConverter = ShipmentDataConverter()

    lateinit var presenter: ShipmentPresenter

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
            compositeSubscription,
            checkoutUseCase,
            getShipmentAddressFormV4UseCase,
            editAddressUseCase,
            changeShippingAddressGqlUseCase,
            saveShipmentStateGqlUseCase,
            getRatesUseCase,
            getRatesApiUseCase,
            clearCacheAutoApplyStackUseCase,
            ratesStatesConverter,
            shippingCourierConverter,
            shipmentAnalyticsActionListener,
            userSessionInterface,
            analyticsPurchaseProtection,
            checkoutAnalytics,
            shipmentDataConverter,
            releaseBookingUseCase,
            prescriptionIdsUseCase,
            epharmacyUseCase,
            validateUsePromoRevampUseCase,
            gson,
            TestSchedulers,
            CoroutineTestDispatchers,
            eligibleForAddressUseCase,
            getRatesWithScheduleUseCase,
            updateDynamicDataPassingUseCase,
            shipmentDataRequestConverter
        )
        presenter.attachView(view)
    }
}
