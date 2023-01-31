package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.dynamicdatapassing.domain.UpdateDynamicDataPassingUseCase
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.GetPrescriptionIdsResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterPrescriptionIdsTest {
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

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

    @MockK
    private lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    private var shipmentDataConverter = ShipmentDataConverter()

    private lateinit var presenter: ShipmentPresenter

    private var gson = Gson()

    companion object {
        const val CHECKOUT_ID = "100"
    }

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
            validateUsePromoRevampUseCase, gson, TestSchedulers,
            eligibleForAddressUseCase, updateDynamicDataPassingUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun `WHEN upload prescription then should hit upload prescription use case with checkout id`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(mockk<GetPrescriptionIdsResponse>(relaxed = true))
        presenter.setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                false,
                "",
                "",
                checkoutId = CHECKOUT_ID,
                arrayListOf(),
                0,
                ""
            )
        )

        // When
        presenter.fetchPrescriptionIds(true, CHECKOUT_ID)

        // Then
        verify { prescriptionIdsUseCase.execute(CHECKOUT_ID) }
        verify(exactly = 1) { view.updatePrescriptionIds(any()) }
    }

    @Test
    fun `GIVEN no checkout item WHEN upload prescription THEN should not hit upload prescription use case`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(mockk<GetPrescriptionIdsResponse>(relaxed = true))
        presenter.setUploadPrescriptionData(null)

        // When
        presenter.fetchPrescriptionIds(true, "")

        // Then
        verify(inverse = true) { prescriptionIdsUseCase.execute(any()) }
    }

    @Test
    fun `GIVEN upload false item WHEN upload prescription THEN should not hit upload prescription use case`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(mockk<GetPrescriptionIdsResponse>(relaxed = true))
        presenter.setUploadPrescriptionData(null)

        // When
        presenter.fetchPrescriptionIds(false, CHECKOUT_ID)

        // Then
        verify(inverse = true) { prescriptionIdsUseCase.execute(any()) }
    }

    @Test
    fun `GIVEN error item WHEN upload prescription THEN should not hit upload prescription use case`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.error(Throwable())
        presenter.setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                false,
                "",
                "",
                checkoutId = CHECKOUT_ID,
                arrayListOf(),
                0,
                ""
            )
        )

        // When
        presenter.fetchPrescriptionIds(true, CHECKOUT_ID)

        // Then
        verify { prescriptionIdsUseCase.execute(CHECKOUT_ID) }
        verify(exactly = 0) { view.updatePrescriptionIds(any()) }
    }

    @Test
    fun `CHECK upload prescription data initialization`() {
        // Given
        every { prescriptionIdsUseCase.execute(any()) } returns Observable.just(mockk<GetPrescriptionIdsResponse>(relaxed = true))
        presenter.setUploadPrescriptionData(
            UploadPrescriptionUiModel(
                false,
                "",
                "",
                checkoutId = CHECKOUT_ID,
                arrayListOf(),
                0,
                ""
            )
        )

        // Then
        assert(presenter.uploadPrescriptionUiModel != null)
    }
}
