package com.tokopedia.checkout.view.presenter

import com.google.gson.Gson
import com.tokopedia.checkout.analytics.CheckoutAnalyticsPurchaseProtection
import com.tokopedia.checkout.data.model.response.dynamicdata.UpdateDynamicDataPassingUiModel
import com.tokopedia.checkout.domain.mapper.ShipmentMapper
import com.tokopedia.checkout.domain.model.cartshipmentform.*
import com.tokopedia.checkout.domain.usecase.*
import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.checkout.view.ShipmentPresenter
import com.tokopedia.checkout.view.converter.ShipmentDataConverter
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.EligibleForAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsCourierSelection
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.usecase.GetPrescriptionIdsUseCase
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnsDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnResult
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.SaveAddOnStateResult
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldClearCacheAutoApplyStackUseCase
import com.tokopedia.purchase_platform.common.feature.promo.domain.usecase.OldValidateUsePromoRevampUseCase
import com.tokopedia.purchase_platform.common.schedulers.TestSchedulers
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test
import rx.subscriptions.CompositeSubscription

class ShipmentPresenterUpdateDynamicDataTest {

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
    private lateinit var updateDynamicDataPassingUseCase: UpdateDynamicDataPassingUseCase

    private var shipmentDataConverter = ShipmentDataConverter()
    private var shipmentMapper = ShipmentMapper()
    private var updateDynamicDataParams = DynamicDataPassingParamRequest()

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
            validateUsePromoRevampUseCase, gson, TestSchedulers,
            eligibleForAddressUseCase, updateDynamicDataPassingUseCase
        )
        presenter.attachView(view)
    }

    @Test
    fun updateDynamicData_fireAndForget() {
        // Given
        val dynamicData = UpdateDynamicDataPassingUiModel("data")

        coEvery {
            updateDynamicDataPassingUseCase.setParams(any())
        } just Runs
        coEvery {
            updateDynamicDataPassingUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateDynamicDataPassingUiModel) -> Unit>().invoke(dynamicData)
        }

        // When
        presenter.validateDynamicData()
        presenter.updateDynamicData(updateDynamicDataParams, true)

        // Then
        verifyOrder {
            view.stopTrace()
        }
    }

    @Test
    fun updateDynamicData_notFireAndForget() {
        // Given
        val dynamicData = UpdateDynamicDataPassingUiModel("data")

        coEvery {
            updateDynamicDataPassingUseCase.setParams(any())
        } just Runs
        coEvery {
            updateDynamicDataPassingUseCase.execute(any(), any())
        } answers {
            firstArg<(UpdateDynamicDataPassingUiModel) -> Unit>().invoke(dynamicData)
        }

        // When
        presenter.validateDynamicData()
        presenter.updateDynamicData(updateDynamicDataParams, false)

        // Then
        verifyOrder {
            view.stopTrace()
            view.doCheckout()
        }
    }

    @Test
    fun updateDynamicData_throwsCartResponseErrorException() {
        // Given
        val error = CartResponseErrorException("error")

        coEvery {
            updateDynamicDataPassingUseCase.setParams(any())
        } just Runs
        coEvery { updateDynamicDataPassingUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.validateDynamicData()
        presenter.updateDynamicData(updateDynamicDataParams, true)

        // Then
        verifyOrder {
            view.showToastError(any())
            view.stopTrace()
        }
    }

    @Test
    fun updateDynamicData_throwsException() {
        // Given
        val error = Exception("error")
        coEvery {
            updateDynamicDataPassingUseCase.setParams(any())
        } just Runs
        coEvery { updateDynamicDataPassingUseCase.execute(any(), any()) } answers {
            secondArg<(Throwable) -> Unit>().invoke(error)
        }

        // When
        presenter.validateDynamicData()
        presenter.updateDynamicData(updateDynamicDataParams, true)

        // Then
        verifyOrder {
            view.showToastError(any())
            view.stopTrace()
        }
    }

    @Test
    fun `WHEN presenter detached THEN all usecases is unsubscribed`() {
        // When
        presenter.detachView()

        // Then
        verify {
            getShipmentAddressFormV3UseCase.cancelJobs()
            eligibleForAddressUseCase.cancelJobs()
            updateDynamicDataPassingUseCase.cancelJobs()
        }
    }

    @Test
    fun `WHEN update addOn product level data bottomsheet and is using ddp`() {
        // Given
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
                    errorCode = 0,
                    groupAddress = emptyList(),
                    isUsingDdp = true
                )
            )
        }
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        cartId = 88
                        cartString = "239594-0-301643"
                    }
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )
        presenter.updateAddOnProductLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        // Then
        verify {
            presenter.isUsingDynamicDataPassing = true
            view.updateAddOnsData(AddOnsDataModel(), 0)
            view.updateAddOnsDynamicDataPassing(any(), any(), any(), any(), any())
        }
    }

    @Test
    fun `WHEN update addOn product level data bottomsheet and not using ddp`() {
        // Given
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
                    errorCode = 0,
                    groupAddress = emptyList(),
                    isUsingDdp = true
                )
            )
        }
        val shipmentCartItemModelList = arrayListOf<ShipmentCartItemModel>()
        shipmentCartItemModelList.add(
            ShipmentCartItemModel().apply {
                cartItemModels = arrayListOf(
                    CartItemModel().apply {
                        cartId = 88
                        cartString = "239594-0-301643"
                    }
                )
            }
        )

        val addOnResultList = arrayListOf<AddOnResult>()
        addOnResultList.add(
            AddOnResult().apply {
                addOnKey = "239594-0-301643-88"
            }
        )
        presenter.shipmentCartItemModelList = shipmentCartItemModelList

        // When
        presenter.processInitialLoadCheckoutPage(
            false,
            false,
            false,
            false,
            false,
            null,
            "",
            "",
            false
        )
        presenter.updateAddOnProductLevelDataBottomSheet(SaveAddOnStateResult(addOnResultList))

        // Then
        verify {
            view.updateAddOnsData(AddOnsDataModel(), 0)
            view.updateAddOnsDynamicDataPassing(any(), any(), any(), any(), any())
        }
    }
}
