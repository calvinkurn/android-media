package com.tokopedia.checkout.view.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.ShipmentViewModel
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseShipmentViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

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

    @MockK
    lateinit var saveAddOnStateUseCase: SaveAddOnStateUseCase

    @MockK(relaxUnitFun = true)
    lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    private val ratesStatesConverter: RatesResponseStateConverter = RatesResponseStateConverter()

    private val shippingCourierConverter: ShippingCourierConverter = ShippingCourierConverter()

    @MockK(relaxed = true)
    lateinit var userSessionInterface: UserSessionInterface

    @MockK(relaxed = true)
    lateinit var analyticsPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    @MockK(relaxed = true)
    lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK(relaxed = true)
    lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    lateinit var view: ShipmentFragment

    @MockK(relaxed = true)
    lateinit var getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase

    @MockK
    lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine

    @MockK(relaxed = true)
    lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK(relaxed = true)
    lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    @MockK(relaxed = true)
    lateinit var dynamicPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase

    val gson = Gson()

    private val shipmentDataRequestConverter = ShipmentDataRequestConverter(gson)

    private val shipmentDataConverter = ShipmentDataConverter()

    lateinit var viewModel: ShipmentViewModel

    @Before
    fun before() {
        MockKAnnotations.init(this)
        viewModel = ShipmentViewModel(
            getShipmentAddressFormV4UseCase,
            saveShipmentStateGqlUseCase,
            changeShippingAddressGqlUseCase,
            editAddressUseCase,
            getRatesUseCase,
            getRatesApiUseCase,
            getRatesWithScheduleUseCase,
            clearCacheAutoApplyStackUseCase,
            validateUsePromoRevampUseCase,
            releaseBookingUseCase,
            prescriptionIdsUseCase,
            epharmacyUseCase,
            updateDynamicDataPassingUseCase,
            dynamicPaymentFeeCheckoutUseCase,
            checkoutUseCase,
            saveAddOnStateUseCase,
            shipmentDataConverter,
            shippingCourierConverter,
            ratesStatesConverter,
            shipmentDataRequestConverter,
            shipmentAnalyticsActionListener,
            checkoutAnalytics,
            analyticsPurchaseProtection,
            userSessionInterface,
            gson,
            CoroutineTestDispatchers
        )
        viewModel.attachView(view)
    }
}
