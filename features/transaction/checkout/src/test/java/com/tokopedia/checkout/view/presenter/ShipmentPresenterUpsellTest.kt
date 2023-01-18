package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.usecase.ChangeShippingAddressGqlUseCase
import com.tokopedia.checkout.domain.usecase.CheckoutGqlUseCase
import com.tokopedia.checkout.domain.usecase.GetShipmentAddressFormV3UseCase
import com.tokopedia.checkout.domain.usecase.ReleaseBookingUseCase
import com.tokopedia.checkout.domain.usecase.SaveShipmentStateGqlUseCase
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.promocheckout.common.view.uimodel.VoucherLogisticItemUiModel
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.feature.bometadata.BoMetadata
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.ClearPromoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.clearpromo.SuccessDataUiModel
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rx.Observable
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterUpsellTest {

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
    private lateinit var getRatesWithScheduleUseCase: GetRatesWithScheduleUseCase

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

    @MockK
    private lateinit var eligibleForAddressUseCase: EligibleForAddressUseCase

    @MockK
    private lateinit var prescriptionIdsUseCase: GetPrescriptionIdsUseCase

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
            checkoutAnalytics, shipmentDataConverter, releaseBookingUseCase, prescriptionIdsUseCase,
            validateUsePromoRevampUseCase, gson, TestSchedulers, eligibleForAddressUseCase,
            getRatesWithScheduleUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun `WHEN cancel upsell THEN should try clear all BO`() {
        // Given
        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.cancelUpsell(
            true, false, false, true,
            false, null, "", "",
            false
        )

        // Then
        verify {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
            getShipmentAddressFormV3UseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state THEN should try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.clearAllBoOnTemporaryUpsell()

        // Then
        verify {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
            getShipmentAddressFormV3UseCase.execute(any(), any())
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state with no BO THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.clearAllBoOnTemporaryUpsell()

        // Then
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state with no BO code THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel()
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.clearAllBoOnTemporaryUpsell()

        // Then
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }

    @Test
    fun `WHEN clear all BO on temporary state with no shipment data THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = true,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.clearAllBoOnTemporaryUpsell()

        // Then
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }

    @Test
    fun `WHEN clear all BO not on temporary state THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = true,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = false,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.clearAllBoOnTemporaryUpsell()

        // Then
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }

    @Test
    fun `WHEN clear all BO not on upsell THEN should not try clear all BO`() {
        // Given
        val groupAddress = GroupAddress().apply {
            userAddress = UserAddress(state = 0)
        }
        val upsell = NewUpsellData(
            isShow = false,
            description = "desc",
            appLink = "applink",
            image = "image",
            isSelected = false,
            price = 100,
            priceWording = "Rp100",
            duration = "duration",
            summaryInfo = "wording",
            buttonText = "button"
        )
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } answers {
            firstArg<(CartShipmentAddressFormData) -> Unit>().invoke(
                CartShipmentAddressFormData(
                    groupAddress = listOf(groupAddress),
                    newUpsell = upsell
                )
            )
        }

        presenter.processInitialLoadCheckoutPage(
            true,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            true
        )

        presenter.shipmentCartItemModelList = listOf(
            ShipmentCartItemModel().apply {
                cartItemModels = listOf(CartItemModel())
                cartString = "cartString"
                shipmentCartData = ShipmentCartData(boMetadata = BoMetadata())
                voucherLogisticItemUiModel = VoucherLogisticItemUiModel("BOCODE")
            }
        )
        every { clearCacheAutoApplyStackUseCase.createObservable(any()) } returns Observable.just(
            ClearPromoUiModel(
                successDataModel = SuccessDataUiModel(
                    success = true
                )
            )
        )
        every { clearCacheAutoApplyStackUseCase.setParams(any()) } just Runs
        coEvery {
            getShipmentAddressFormV3UseCase.setParams(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } just Runs
        coEvery { getShipmentAddressFormV3UseCase.execute(any(), any()) } just Runs
        every {
            shipmentAnalyticsActionListener.sendAnalyticsViewInformationAndWarningTickerInCheckout(
                any()
            )
        } just Runs

        // When
        presenter.clearAllBoOnTemporaryUpsell()

        // Then
        verify(inverse = true) {
            clearCacheAutoApplyStackUseCase.setParams(any())
            clearCacheAutoApplyStackUseCase.createObservable(any())
        }
    }
}
