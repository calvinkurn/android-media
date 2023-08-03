package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.Companion.ERROR_PINPOINT_NEEDED
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticcart.datamock.DummyProvider
import com.tokopedia.logisticcart.datamock.DummyProvider.getRatesResponseWithPromo
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithPromoAndPreOrderModel
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithPromoEtaError
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithPromoEtaErrorAndTextEta
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithServiceError
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithServiceUiRatesHidden
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithoutEligibleCourierPromo
import com.tokopedia.logisticcart.shipping.model.ChooseShippingDurationState
import com.tokopedia.logisticcart.shipping.model.DividerModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ProductShipmentDetailModel
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingDurationAnalyticState
import com.tokopedia.logisticcart.shipping.model.ShippingDurationListState
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ShippingDurationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val ratesUseCase: GetRatesCoroutineUseCase = mockk(relaxed = true)
    private val ratesApiUseCase: GetRatesApiCoroutineUseCase = mockk(relaxed = true)
    private val responseConverter: RatesResponseStateConverter = mockk()
    private val ratesParam = mockk<RatesParam>()
    lateinit var viewModel: ShippingDurationViewModel

    private val analyticState: Observer<ShippingDurationAnalyticState> = mockk(relaxed = true)
    private val loadingState: Observer<Boolean> = mockk(relaxed = true)
    private val shipmentState: Observer<ShippingDurationListState> = mockk(relaxed = true)

    private val shipmentDetailData: ShipmentDetailData = DummyProvider.getShipmentDetailData()
    private val shopShipments: List<ShopShipment> = DummyProvider.getShopShipments()
    val products: List<Product> = DummyProvider.getProducts()
    val address: RecipientAddressModel = DummyProvider.getAddress()

    @Before
    fun setup() {
        viewModel = ShippingDurationViewModel(
            ratesUseCase,
            ratesApiUseCase,
            responseConverter
        )
        viewModel.shipmentAnalytic.observeForever(analyticState)
        viewModel.loading.observeForever(loadingState)
        viewModel.shipmentData.observeForever(shipmentState)
        Dispatchers.setMain(TestCoroutineDispatcher())
        every { ratesParam.shopShipments } returns shopShipments
    }

    @Test
    fun `When load courier recommendation return success data Then view shows positive data`() {
        val shippingData = DummyProvider.getShippingRecommendationDataWithState()

        // Given
        coEvery { ratesUseCase(any()) } returns ShippingRecommendationData()
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        verifyOrder {
            loadingState.onChanged(match { it })
            loadingState.onChanged(match { !it })
            shipmentState.onChanged(match { it is ShippingDurationListState.ShowList })
        }
        assert(viewModel.shipmentData.value is ShippingDurationListState.ShowList)
    }

    @Test
    fun `When load courier recommendation using rates api return success data Then view shows positive data`() {
        val shippingData = DummyProvider.getShippingRecommendationDataWithState()

        // Given
        coEvery { ratesApiUseCase(any()) } returns (ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        viewModel.loadDuration(0, 0, ratesParam, true, false)

        // Then
        verifyOrder {
            loadingState.onChanged(match { it })
            loadingState.onChanged(match { !it })
            shipmentState.onChanged(match { it is ShippingDurationListState.ShowList })
        }
    }

    @Test
    fun `When load courier recommendation return 504 error id Then view shows no courier page from errorMessage`() {
        val errorMessage = "Error test"
        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        shippingData.errorId = "504"
        shippingData.errorMessage = errorMessage

        // Given
        coEvery { ratesUseCase(any()) } returns ShippingRecommendationData()
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        verifyOrder {
            loadingState.onChanged(match { it })
            loadingState.onChanged(match { !it })
            shipmentState.onChanged(
                match {
                    it == ShippingDurationListState.NoShipmentAvailable(
                        errorMessage
                    )
                }
            )
        }
    }

    @Test
    fun `When load courier recommendation return error id Then view shows no courier page from errorMessage`() {
        val errorMessage = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda."
        val shippingData = ShippingRecommendationData()
        shippingData.errorId = "503"
        shippingData.errorMessage = "Error test"

        // Given
        coEvery { ratesUseCase(any()) } returns ShippingRecommendationData()
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        verifyOrder {
            loadingState.onChanged(match { it })
            loadingState.onChanged(match { !it })
            shipmentState.onChanged(
                match {
                    it == ShippingDurationListState.NoShipmentAvailable(
                        errorMessage
                    )
                }
            )
        }
    }

    @Test
    fun `When load courier recommendation return empty services Then view shows no courier page`() {
        val errorMessage = "Tidak ada kurir yang mendukung pengiriman ini ke lokasi Anda."
        val shippingData = ShippingRecommendationData()

        // Given
        coEvery { ratesUseCase(any()) } returns ShippingRecommendationData()
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        verifyOrder {
            loadingState.onChanged(match { it })
            loadingState.onChanged(match { !it })
            shipmentState.onChanged(
                match {
                    it == ShippingDurationListState.NoShipmentAvailable(
                        errorMessage
                    )
                }
            )
        }
    }

    @Test
    fun `When load courier recommendation return error id Then view shows error page`() {
        val err = Exception("Unexpected error")

        // Given
        coEvery { ratesUseCase(any()) } throws err

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        verifyOrder {
            loadingState.onChanged(match { it })
            loadingState.onChanged(match { !it })
            shipmentState.onChanged(
                match {
                    it == ShippingDurationListState.RatesError(err)
                }
            )
        }
        assert(viewModel.shipmentData.value == ShippingDurationListState.RatesError(err))
    }

    @Test
    fun `When load courier recommendation and promo courier is disabled Then isPromo flag is zero and promocode in every product is empty`() {
        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        val shippingDurationUIModels = shippingData.shippingDurationUiModels
        val productsShipping = shippingDurationUIModels.map { it.serviceData.products }

        // Given
        coEvery { ratesApiUseCase(any()) } returns ShippingRecommendationData()
        every {
            responseConverter.fillState(any(), shopShipments, 0, 0)
        } returns shippingData

        val shipmentDetailData = DummyProvider.getShipmentDetailData()
        shipmentDetailData.selectedCourier = null

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        assertEquals(
            shippingDurationUIModels.filter { it.serviceData.isPromo == 0 }.size,
            shippingDurationUIModels.size
        )
        assertEquals(
            productsShipping.filter { product -> product.any { item -> item.promoCode.isEmpty() } }.size,
            productsShipping.size
        )
    }

    @Test
    fun `When rates v3 response dont have eligible courier for promo Then should not hit analytic`() {
        // Given
        val shippingRecommendationData = getShippingDataWithoutEligibleCourierPromo()
        coEvery { ratesUseCase(any()) } returns shippingRecommendationData
        every {
            responseConverter.fillState(
                any(),
                shopShipments,
                shipmentDetailData.selectedCourier!!.shipperProductId,
                0
            )
        } returns shippingRecommendationData

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        assert(
            viewModel.shipmentAnalytic.value != ShippingDurationAnalyticState.AnalyticCourierPromo(
                shippingRecommendationData.shippingDurationUiModels
            )
        )
    }

    @Test
    fun `When rates v3 response has eligible courier for promo Then should hit analytic`() {
        // Given
        val ratesV3Response = getRatesResponseWithPromo()
        val shippingRecommendationData =
            ShippingDurationConverter().convertModel(ratesV3Response.ratesData)
        coEvery { ratesUseCase(any()) } returns shippingRecommendationData
        every {
            responseConverter.fillState(
                any(),
                shopShipments,
                any(),
                0
            )
        } returns shippingRecommendationData

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        verify {
            analyticState.onChanged(
                match {
                    it == ShippingDurationAnalyticState.AnalyticCourierPromo(
                        shippingRecommendationData.shippingDurationUiModels
                    )
                }
            )
            analyticState.onChanged(
                match {
                    it == ShippingDurationAnalyticState.AnalyticPromoLogistic(
                        shippingRecommendationData.listLogisticPromo
                    )
                }
            )
        }
    }

    /*
     * convertServiceListToUiModel
     */

    @Test
    fun `When service list has service with ui rates hidden Then dont show in bottomsheet`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceUiRatesHidden()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        val result = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list
        assertFalse(result.any { it is ShippingDurationUiModel && it.serviceData.isUiRatesHidden })
    }

    @Test
    fun `When rates v3 response has promo logistic Then show divider in bottomsheet`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceUiRatesHidden()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list
        val lastIndexOfPromoModel = actual.indexOfLast { it is LogisticPromoUiModel }

        // Then
        assert(actual.any { it is LogisticPromoUiModel })
        assert(actual[lastIndexOfPromoModel + 1] is DividerModel)
    }

    @Test
    fun `When pre order model display is true Then show pre order in bottomsheet`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        assert(actual.first() is ProductShipmentDetailModel)
        assert((actual.first() as ProductShipmentDetailModel).preOrderModel != null)
    }

    @Test
    fun `When should display pre order model and has promo Then show pre order model before promo`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        assert(actual.first() is ProductShipmentDetailModel)
        assert((actual.first() as ProductShipmentDetailModel).preOrderModel != null)
    }

    @Test
    fun `When in checkout and promo has eta error code Then initiate showcase`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoEtaError()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        val firstDuration = actual.find { it is ShippingDurationUiModel } as ShippingDurationUiModel
        assert(firstDuration.isShowShowCase)
    }

    @Test
    fun `When in checkout and promo has eta error code but no services data Then do nothing`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoEtaError()
        shippingRecommendationData.shippingDurationUiModels = listOf()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)

        // Then
        val firstDuration = shippingRecommendationData.shippingDurationUiModels.firstOrNull()
        assertNull(firstDuration?.isShowShowCase)
    }

    @Test
    fun `When in checkout and promo has no eta text but error code is not true Then dont initiate showcase`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoEtaErrorAndTextEta()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        val firstDuration = actual.find { it is ShippingDurationUiModel } as ShippingDurationUiModel
        assertFalse(firstDuration.isShowShowCase)
    }

    @Test
    fun `When in occ and promo has eta error code Then dont initiate showcase`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoEtaError()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, true)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        val firstDuration = actual.find { it is ShippingDurationUiModel } as ShippingDurationUiModel
        assertFalse(firstDuration.isShowShowCase)
    }

    @Test
    fun `When in checkout and duration has eta error code Then show notifier model`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceError()

        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        assert(actual.first() is NotifierModel)
    }

    @Test
    fun `When in checkout and duration is hidden and has eta error code Then dont show notifier model`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceError()
        val hiddenServiceWithEtaError = shippingRecommendationData.shippingDurationUiModels.first()
        hiddenServiceWithEtaError.serviceData.isUiRatesHidden = true
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, false)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        assertFalse(actual.first() is NotifierModel)
    }

    @Test
    fun `When in occ and duration has eta error code Then show notifier model`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceError()
        setRatesResponse(shippingRecommendationData)

        // When
        viewModel.loadDuration(0, 0, ratesParam, false, true)
        val actual = (viewModel.shipmentData.value as ShippingDurationListState.ShowList).list

        // Then
        assertFalse(actual.first() is NotifierModel)
    }

    /*
    OnChooseDuration
     */

    // checkout
    @Test
    fun `When in checkout and service is pinpoint error Then set pinpoint error flag`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        selectedService.serviceData = selectedService.serviceData.copy(
            error = ErrorServiceData(
                errorId = ERROR_PINPOINT_NEEDED,
                errorMessage = "error pinpoint"
            )
        )

        // When
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == selectedService.serviceData.serviceId)
        assert(actual.serviceData == selectedService.serviceData)
        assert(actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When in occ and service is pinpoint error Then don't set pinpoint error flag`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        selectedService.serviceData = selectedService.serviceData.copy(
            error = ErrorServiceData(
                errorId = ERROR_PINPOINT_NEEDED,
                errorMessage = "error pinpoint"
            )
        )

        // When
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            true
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(!actual.flagNeedToSetPinpoint)
        assert(actual.selectedServiceId != selectedService.serviceData.serviceId)
    }

    @Test
    fun `When in checkout and service is error Then pinpoint error flag is false`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        selectedService.serviceData = selectedService.serviceData.copy(
            error = ErrorServiceData(
                errorId = "1",
                errorMessage = "error"
            )
        )

        // When
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When in checkout and service not error Then pinpoint error flag is false`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        selectedService.serviceData = selectedService.serviceData.copy(error = ErrorServiceData())

        // When
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When in checkout select duration and there is recommended courier Then select the recommended courier`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }
        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.courierData == recommendedCourier)
        assert(actual.cartPosition == 0)
        assert(actual.serviceData == selectedService.serviceData)
    }

    @Test
    fun `When in checkout select duration and there is recommended courier but is hidden Then dont select the recommended courier`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }
        recommendedCourier?.apply {
            productData = productData.copy(isUiRatesHidden = true)
        }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.courierData != recommendedCourier)
    }

    @Test
    fun `When in checkout select duration and there is selected shipper product Id Then select the selected shipper product`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData =
            selectedService.serviceData.copy(selectedShipperProductId = selectedShipperProductId)
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.courierData == recommendedCourier)
        assert(actual.cartPosition == 0)
        assert(actual.serviceData == selectedService.serviceData)
    }

    @Test
    fun `When select duration and there is selected shipper product Id but no shipper product with the recommended id Then select the recommended product`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 25
        selectedService.serviceData =
            selectedService.serviceData.copy(selectedShipperProductId = selectedShipperProductId)
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }
        val recommendedFlagCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }
        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        assert(recommendedCourier == null)
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.courierData == recommendedFlagCourier)
        assert(actual.cartPosition == 0)
        assert(actual.serviceData == selectedService.serviceData)
    }

    @Test
    fun `When select duration and there are recommended courier and selected shipper product Id Then select the selected shipper product`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData =
            selectedService.serviceData.copy(selectedShipperProductId = selectedShipperProductId)
        val selectedShipper =
            selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }
        val recommendCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }
        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.courierData == selectedShipper)
        assert(actual.courierData != recommendCourier)
    }

    @Test
    fun `When select duration in occ and recommended courier not found Then select the first not hidden courier`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val courierList =
            selectedService.shippingCourierViewModelList.filter { courier -> !courier.productData.isRecommend }
        selectedService.shippingCourierViewModelList = courierList
        val selectedShipper = courierList.first { !it.productData.isUiRatesHidden }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            true
        )

        // Then
        assert(selectedShipper.isSelected)
    }

    @Test
    fun `When select duration in occ and eligible couriers are error Then select the first courier`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val courierList =
            selectedService.shippingCourierViewModelList.filter { courier -> !courier.productData.isRecommend }
        courierList.forEach { courier ->
            courier.productData = courier.productData.copy(
                error = ErrorProductData().apply {
                    errorId = "2"
                    errorMessage = "error"
                }
            )
        }
        selectedService.shippingCourierViewModelList = courierList
        val selectedShipper = courierList.first { !it.productData.isUiRatesHidden }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            true
        )

        // Then
        assert(selectedShipper.isSelected)
    }

    @Test
    fun `When select duration in occ and couriers are error and hidden Then select the first courier`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val courierList =
            selectedService.shippingCourierViewModelList.filter { courier -> !courier.productData.isRecommend }
        courierList.forEach { courier ->
            courier.productData = courier.productData.copy(isUiRatesHidden = true)
            courier.productData = courier.productData.copy(
                error = ErrorProductData().apply {
                    errorId = "2"
                    errorMessage = ""
                }
            )
        }
        selectedService.shippingCourierViewModelList = courierList
        val selectedShipper = courierList.first()

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            true
        )

        // Then
        assert(selectedShipper.isSelected)
    }

    @Test
    fun `When select duration in checkout but service is pinpoint error Then set pinpoint error flag`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        selectedService.serviceData = selectedService.serviceData.copy(
            error = ErrorServiceData(
                errorId = ERROR_PINPOINT_NEEDED,
                errorMessage = "error pinpoint"
            )
        )

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == selectedService.serviceData.serviceId)
        assert(actual.serviceData == selectedService.serviceData)
        assert(actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration and get selected courier and courier is pinpoint error Then set pinpoint flag to true`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val product = selectedService.shippingCourierViewModelList.first()
        product.productData = product.productData.copy(
            error = ErrorProductData().apply {
                errorId = ERROR_PINPOINT_NEEDED
                errorMessage = "error pinpoint"
            }
        )

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            true
        )

        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == selectedService.serviceData.serviceId)
        assert(actual.serviceData == selectedService.serviceData)
        assert(actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration and get selected courier and courier is error but not pinpoint error Then pinpoint error flag is false`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val product = selectedService.shippingCourierViewModelList.first()
        product.productData = product.productData.copy(
            error = ErrorProductData().apply {
                errorId = "1"
                errorMessage = "error"
            }
        )

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration and get selected courier and courier is not error Then pinpoint error flag is false`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val product = selectedService.shippingCourierViewModelList.first()
        product.productData = product.productData.copy(error = ErrorProductData())

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration but courier error data not found Then pinpoint error flag is false`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val product = selectedService.shippingCourierViewModelList.first()
        product.productData = product.productData.copy(
            error = ErrorProductData().apply {
                errorId = "2"
                errorMessage = ""
            }
        )

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration and there is recommended courier Then send recommended courier data`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration in checkout and there is recommended courier but is hidden Then dont send recommended courier data`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }
        recommendedCourier?.apply {
            productData = productData.copy(isUiRatesHidden = true)
        }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.courierData == null)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
    }

    @Test
    fun `When select duration and there is selected shipper product Id Then send selected shipper product data`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData =
            selectedService.serviceData.copy(selectedShipperProductId = selectedShipperProductId)
        val recommendedCourier =
            selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
        assert(actual.courierData == recommendedCourier)
    }

    @Test
    fun `When select duration and there is recommended courier and selected shipper product Id Then send the selected shipper product data`() {
        // Given
        // selected shipping duration ui model
        val selectedService =
            getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData =
            selectedService.serviceData.copy(selectedShipperProductId = selectedShipperProductId)
        val selectedShipper =
            selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }

        // When
        // onChooseDuration
        viewModel.onChooseDuration(
            selectedService.shippingCourierViewModelList,
            0,
            selectedService.serviceData,
            false
        )

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.NormalShipping
        assert(actual.shippingCourierUiModelList == selectedService.shippingCourierViewModelList)
        assert(actual.selectedServiceId == 0)
        assert(actual.serviceData == selectedService.serviceData)
        assert(!actual.flagNeedToSetPinpoint)
        assert(actual.courierData == selectedShipper)
    }

    /*
     * OnLogisticPromoClicked
     */

    @Test
    fun `When select promo logistic Then send courier data and service data from promo logistic data`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        val selectedBo = shippingRecommendationData.listLogisticPromo.first()
        viewModel.shippingData = shippingRecommendationData

        // When
        viewModel.onLogisticPromoClicked(selectedBo)

        // Then
        val actual = viewModel.chosenDuration.value as ChooseShippingDurationState.FreeShipping
        assert(actual.promoCode == selectedBo.promoCode)
        assert(actual.serviceId == selectedBo.serviceId)
        assert(actual.data == selectedBo)
    }

    @Test
    fun `When select promo logistic and service data is not found Then should show courier promo error`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        val selectedBo = shippingRecommendationData.listLogisticPromo.first().copy(serviceId = 0)
        shippingRecommendationData.listLogisticPromo = listOf(selectedBo)

        viewModel.shippingData = shippingRecommendationData

        // When
        viewModel.onLogisticPromoClicked(selectedBo)

        // Then
        assert(viewModel.chosenDuration.value is ChooseShippingDurationState.CourierNotAvailable)
    }

    @Test
    fun `When select promo logistic and courier data is not found Then should show courier promo error`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        val selectedBo = shippingRecommendationData.listLogisticPromo.first()
        val serviceDataForSelectedBo =
            shippingRecommendationData.shippingDurationUiModels.find { it.serviceData.serviceId == selectedBo.serviceId }
        serviceDataForSelectedBo?.shippingCourierViewModelList = listOf()

        viewModel.shippingData = shippingRecommendationData

        // When
        viewModel.onLogisticPromoClicked(selectedBo)

        // Then
        assert(viewModel.chosenDuration.value is ChooseShippingDurationState.CourierNotAvailable)
    }

    private fun setRatesResponse(shippingRecommendationData: ShippingRecommendationData) {
        coEvery { ratesUseCase(any()) } returns shippingRecommendationData
        every {
            responseConverter.fillState(
                any(),
                shopShipments,
                any(),
                0
            )
        } returns shippingRecommendationData
    }
}
