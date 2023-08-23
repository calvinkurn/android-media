package com.tokopedia.checkout.revamp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.analytics.CheckoutTradeInAnalytics
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetPaymentFeeCheckoutUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV4UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.revamp.view.converter.CheckoutDataConverter
import com.tokopedia.checkout.revamp.view.processor.CheckoutAddOnProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutCalculator
import com.tokopedia.checkout.revamp.view.processor.CheckoutCartProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutDataHelper
import com.tokopedia.checkout.revamp.view.processor.CheckoutLogisticProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPaymentProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutPromoProcessor
import com.tokopedia.checkout.revamp.view.processor.CheckoutToasterProcessor
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageToaster
import com.tokopedia.checkout.view.converter.ShipmentDataRequestConverter
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.addons.domain.SaveAddOnStateUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCaseCoroutine
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.ValidateUsePromoRevampUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Rule

open class BaseCheckoutViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    lateinit var getShipmentAddressFormV4UseCase: GetShipmentAddressFormV4UseCase

    @MockK(relaxed = true)
    lateinit var saveShipmentStateGqlUseCase: SaveShipmentStateGqlUseCase

    @MockK
    lateinit var changeShippingAddressGqlUseCase: Lazy<ChangeShippingAddressGqlUseCase>

    @MockK
    lateinit var releaseBookingUseCase: Lazy<ReleaseBookingUseCase>

    @MockK
    lateinit var eligibleForAddressUseCase: Lazy<EligibleForAddressUseCase>

    @MockK
    lateinit var editAddressUseCase: EditAddressUseCase

    @MockK
    lateinit var ratesUseCase: GetRatesCoroutineUseCase

    @MockK
    lateinit var ratesApiUseCase: GetRatesApiCoroutineUseCase

    @MockK
    lateinit var ratesWithScheduleUseCase: GetRatesWithScheduleDeliveryCoroutineUseCase

    private val ratesResponseStateConverter: RatesResponseStateConverter =
        RatesResponseStateConverter()

    private val shippingCourierConverter: ShippingCourierConverter = ShippingCourierConverter()

    @MockK(relaxed = true)
    lateinit var userSessionInterface: UserSessionInterface

    @MockK
    lateinit var clearCacheAutoApplyStackUseCase: ClearCacheAutoApplyStackUseCase

    @MockK
    lateinit var validateUsePromoRevampUseCase: ValidateUsePromoRevampUseCase

    @MockK(relaxed = true)
    lateinit var mTrackerShipment: CheckoutAnalyticsCourierSelection

    @MockK(relaxed = true)
    lateinit var toasterProcessor: CheckoutToasterProcessor

    var helper: CheckoutDataHelper = CheckoutDataHelper()

    @MockK
    lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCaseCoroutine

    @MockK
    lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK(relaxUnitFun = true)
    lateinit var saveAddOnProductUseCase: SaveAddOnStateUseCase

    @MockK
    lateinit var getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase

    @MockK
    lateinit var checkoutGqlUseCase: CheckoutUseCase

    @MockK(relaxed = true)
    lateinit var checkoutAnalyticsCourierSelection: CheckoutAnalyticsCourierSelection

    private val shipmentDataRequestConverter: ShipmentDataRequestConverter =
        ShipmentDataRequestConverter(
            Gson()
        )

    var dataConverter: CheckoutDataConverter = CheckoutDataConverter()

    @MockK(relaxed = true)
    lateinit var mTrackerTradeIn: CheckoutTradeInAnalytics

    @MockK(relaxed = true)
    lateinit var mTrackerPurchaseProtection: CheckoutAnalyticsPurchaseProtection

    private val dispatchers: CoroutineDispatchers = CoroutineTestDispatchers

    lateinit var viewModel: CheckoutViewModel

    var latestToaster: CheckoutPageToaster? = null

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery {
            toasterProcessor.commonToaster.emit(any())
        } answers {
            latestToaster = it.invocation.args[0] as CheckoutPageToaster
        }
        viewModel = CheckoutViewModel(
            CheckoutCartProcessor(
                getShipmentAddressFormV4UseCase,
                saveShipmentStateGqlUseCase,
                changeShippingAddressGqlUseCase,
                releaseBookingUseCase,
                dispatchers
            ),
            CheckoutLogisticProcessor(
                eligibleForAddressUseCase,
                editAddressUseCase,
                ratesUseCase,
                ratesApiUseCase,
                ratesWithScheduleUseCase,
                ratesResponseStateConverter,
                shippingCourierConverter,
                userSessionInterface,
                dispatchers
            ),
            CheckoutPromoProcessor(
                clearCacheAutoApplyStackUseCase,
                validateUsePromoRevampUseCase,
                mTrackerShipment,
                toasterProcessor,
                helper,
                dispatchers
            ),
            CheckoutAddOnProcessor(
                prescriptionIdsUseCase,
                epharmacyUseCase,
                saveAddOnProductUseCase,
                dispatchers
            ),
            CheckoutPaymentProcessor(
                getPaymentFeeCheckoutUseCase,
                checkoutAnalyticsCourierSelection,
                userSessionInterface,
                dispatchers
            ),
            CheckoutProcessor(
                checkoutGqlUseCase,
                shipmentDataRequestConverter,
                mTrackerShipment,
                userSessionInterface,
                helper,
                dispatchers
            ),
            CheckoutCalculator(helper, dispatchers),
            toasterProcessor,
            dataConverter,
            mTrackerShipment,
            mTrackerTradeIn,
            mTrackerPurchaseProtection,
            helper,
            userSessionInterface,
            dispatchers
        )
    }
}
