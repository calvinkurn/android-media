package com.tokopedia.checkout.revamp.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.tokopedia.cartcommon.domain.usecase.UpdateCartUseCase
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
import com.tokopedia.checkoutpayment.domain.CreditCardTenorListUseCase
import com.tokopedia.checkoutpayment.domain.DynamicPaymentFeeUseCase
import com.tokopedia.checkoutpayment.domain.GetPaymentWidgetUseCase
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOptionUseCase
import com.tokopedia.checkoutpayment.processor.PaymentProcessor
import com.tokopedia.common_epharmacy.usecase.EPharmacyPrepareProductsGroupUseCase
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.mapper.ScheduleDeliveryMapper
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.processor.CheckoutShippingProcessor
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import com.tokopedia.promousage.domain.usecase.PromoUsageGetPromoListRecommendationEntryPointUseCase
import com.tokopedia.promousage.view.mapper.PromoUsageGetPromoListRecommendationMapper
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

    @MockK(relaxed = true)
    lateinit var updateCartUseCase: Lazy<UpdateCartUseCase>

    @MockK
    lateinit var pinpointUseCase: UpdatePinpointUseCase

    @MockK
    lateinit var ratesUseCase: GetRatesCoroutineUseCase

    @MockK
    lateinit var ratesApiUseCase: GetRatesApiCoroutineUseCase

    @MockK
    lateinit var ratesWithScheduleUseCase: GetRatesWithScheduleDeliveryCoroutineUseCase

    @MockK
    lateinit var scheduleDeliveryUseCase: GetScheduleDeliveryCoroutineUseCase

    private val scheduleDeliveryMapper: ScheduleDeliveryMapper = ScheduleDeliveryMapper()

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

    @MockK(relaxUnitFun = true)
    lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK(relaxUnitFun = true)
    lateinit var saveAddOnProductUseCase: SaveAddOnStateUseCase

    @MockK(relaxUnitFun = true)
    lateinit var getPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase

    @MockK(relaxed = true)
    lateinit var creditCardTenorListUseCase: CreditCardTenorListUseCase

    @MockK(relaxed = true)
    lateinit var goCicilInstallmentOptionUseCase: GoCicilInstallmentOptionUseCase

    @MockK(relaxed = true)
    lateinit var dynamicPaymentFeeUseCase: DynamicPaymentFeeUseCase

    @MockK(relaxed = true)
    lateinit var getPaymentWidgetUseCase: GetPaymentWidgetUseCase

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

    @MockK(relaxed = true)
    lateinit var getPromoListRecommendationEntryPointUseCase: PromoUsageGetPromoListRecommendationEntryPointUseCase

    var getPromoListRecommendationMapper: PromoUsageGetPromoListRecommendationMapper =
        PromoUsageGetPromoListRecommendationMapper()

    @MockK(relaxed = true)
    lateinit var chosenAddressRequestHelper: ChosenAddressRequestHelper

    val dispatchers: CoroutineTestDispatchers = CoroutineTestDispatchers

    lateinit var logisticProcessor: CheckoutLogisticProcessor

    lateinit var logisticCartProcessor: CheckoutShippingProcessor

    lateinit var promoProcessor: CheckoutPromoProcessor

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
        promoProcessor = CheckoutPromoProcessor(
            clearCacheAutoApplyStackUseCase,
            validateUsePromoRevampUseCase,
            getPromoListRecommendationEntryPointUseCase,
            getPromoListRecommendationMapper,
            chosenAddressRequestHelper,
            mTrackerShipment,
            toasterProcessor,
            helper,
            dispatchers
        )
        logisticProcessor = CheckoutLogisticProcessor(
            scheduleDeliveryMapper
        )
        logisticCartProcessor = CheckoutShippingProcessor(
            pinpointUseCase,
            ratesUseCase,
            ratesApiUseCase,
            ratesWithScheduleUseCase,
            ratesResponseStateConverter,
            shippingCourierConverter,
            scheduleDeliveryUseCase,
            dispatchers
        )
        viewModel = CheckoutViewModel(
            CheckoutCartProcessor(
                getShipmentAddressFormV4UseCase,
                saveShipmentStateGqlUseCase,
                changeShippingAddressGqlUseCase,
                releaseBookingUseCase,
                updateCartUseCase,
                helper,
                dispatchers
            ),
            logisticProcessor,
            logisticCartProcessor,
            promoProcessor,
            CheckoutAddOnProcessor(
                prescriptionIdsUseCase,
                epharmacyUseCase,
                saveAddOnProductUseCase,
                dispatchers
            ),
            CheckoutPaymentProcessor(
                getPaymentFeeCheckoutUseCase,
                PaymentProcessor(
                    creditCardTenorListUseCase,
                    goCicilInstallmentOptionUseCase,
                    dynamicPaymentFeeUseCase,
                    getPaymentWidgetUseCase,
                    dispatchers
                ),
                checkoutAnalyticsCourierSelection,
                userSessionInterface,
                dispatchers,
                helper
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
