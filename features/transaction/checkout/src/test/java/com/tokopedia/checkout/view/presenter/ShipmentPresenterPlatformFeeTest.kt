package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeCheckoutRequest
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeGqlResponse
import com.tokopedia.checkout.domain.model.platformfee.PaymentFeeResponse
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
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
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterPlatformFeeTest {

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

    @MockK
    private lateinit var checkoutAnalytics: CheckoutAnalyticsCourierSelection

    @MockK
    private lateinit var shipmentAnalyticsActionListener: ShipmentContract.AnalyticsActionListener

    @MockK
    private lateinit var releaseBookingUseCase: ReleaseBookingUseCase

    @MockK(relaxed = true)
    private lateinit var view: ShipmentContract.View

    @MockK(relaxed = true)
    private lateinit var getShipmentAddressFormV3UseCase: GetShipmentAddressFormV3UseCase

    @MockK(relaxed = true)
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    @MockK(relaxed = true)
    private lateinit var epharmacyUseCase: EPharmacyPrepareProductsGroupUseCase

    @MockK(relaxed = true)
    private lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

    @MockK(relaxed = true)
    private lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    @MockK(relaxed = true)
    private lateinit var dynamicPaymentFeeCheckoutUseCase: GetPaymentFeeCheckoutUseCase

    private var shipmentDataConverter = ShipmentDataConverter()
    private var platformFeeParams = PaymentFeeCheckoutRequest()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    @Before
    fun before() {
        MockKAnnotations.init(this)
        presenter = ShipmentPresenter(
            compositeSubscription,
            checkoutUseCase,
            getShipmentAddressFormV3UseCase,
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
            eligibleForAddressUseCase,
            getRatesWithScheduleUseCase,
            updateDynamicDataPassingUseCase,
            dynamicPaymentFeeCheckoutUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun getDynamicPlatformFee() {
        // Given
        val platformFee = PaymentFeeGqlResponse(PaymentFeeResponse(success = true))

        coEvery {
            dynamicPaymentFeeCheckoutUseCase.setParams(any())
        } just Runs
        coEvery {
            dynamicPaymentFeeCheckoutUseCase.execute(any(), any())
        } answers {
            firstArg<(PaymentFeeGqlResponse) -> Unit>().invoke(platformFee)
        }

        // When
        presenter.getDynamicPaymentFee(platformFeeParams)

        // Then
        verifyOrder {
            view.showPaymentFeeData(platformFee)
        }
    }
}
