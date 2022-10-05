package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData.ERROR_PINPOINT_NEEDED
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.datamock.DummyProvider
import com.tokopedia.logisticcart.datamock.DummyProvider.getRatesResponseWithPromo
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithPromoAndPreOrderModel
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithPromoEtaError
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithServiceError
import com.tokopedia.logisticcart.datamock.DummyProvider.getShippingDataWithServiceUiRatesHidden
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import rx.Observable

class ShippingDurationPresenterTest {

    private val ratesUseCase: GetRatesUseCase = mockk(relaxed = true)
    private val ratesApiUseCase: GetRatesApiUseCase = mockk(relaxed = true)
    private val responseConverter: RatesResponseStateConverter = mockk()
    val view: ShippingDurationContract.View = mockk(relaxed = true)
    lateinit var presenter: ShippingDurationPresenter

    private val shipmentDetailData: ShipmentDetailData = DummyProvider.getShipmentDetailData()
    private val shopShipments: List<ShopShipment> = DummyProvider.getShopShipments()
    val products: List<Product> = DummyProvider.getProducts()
    val address: RecipientAddressModel = DummyProvider.getAddress()

    @Before
    fun setup() {
        presenter = ShippingDurationPresenter(
            ratesUseCase, ratesApiUseCase,
            responseConverter
        )
    }

    @Test
    fun `When load courier recommendation return success data Then view shows positive data`() {

        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        presenter.attachView(view)

        // Given
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            view.showData(any())
        }
    }

    @Test
    fun `When load courier recommendation using rates api return success data Then view shows positive data`() {

        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        val addressData = address.apply {
            locationDataModel = LocationDataModel()
        }
        presenter.attachView(view)

        // Given
        every { ratesApiUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", true, addressData, false, 0, "", false, false
        )

        // Then
        verify {
            view.showData(any())
        }
    }

    @Test
    fun `When load courier recommendation return 504 error id Then view shows no courier page from errorMessage`() {

        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        shippingData.errorId = "504"
        shippingData.errorMessage = "Error test"
        presenter.attachView(view)

        // Given
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            view.showNoCourierAvailable(shippingData.errorMessage)
        }
    }

    @Test
    fun `When load courier recommendation return empty services Then view shows no courier page`() {
        val shippingData = ShippingRecommendationData()
        presenter.attachView(view)

        // Given
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, any(), 0)
        } returns shippingData

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            view.showNoCourierAvailable(any())
        }
    }

    @Test
    fun `When load courier recommendation return error error id Then view shows error page`() {
        val err = Throwable("Unexpected error")
        presenter.attachView(view)

        // Given
        every { ratesUseCase.execute(any()) } returns Observable.error(err)

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            view.showErrorPage(any())
        }
    }

    @Test
    fun `When load courier recommendation without selected courier Then response converter fill state without selected spId`() {
        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        presenter.attachView(view)

        // Given
        every { ratesUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, 0, 0)
        } returns shippingData

        val shipmentDetailData = DummyProvider.getShipmentDetailData()
        shipmentDetailData.selectedCourier = null

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            responseConverter.fillState(any(), shopShipments, 0, 0)
        }
    }

    @Test
    fun `When load courier recommendation and promo courier is disabled Then isPromo flag is zero and promocode in every product is empty`() {
        val shippingData = DummyProvider.getShippingRecommendationDataWithState()
        val shippingDurationUIModels = shippingData.shippingDurationUiModels
        val productsShipping = shippingDurationUIModels.map { it.serviceData.products }
        presenter.attachView(view)

        // Given
        every { ratesApiUseCase.execute(any()) } returns Observable.just(ShippingRecommendationData())
        every {
            responseConverter.fillState(any(), shopShipments, 0, 0)
        } returns shippingData

        val shipmentDetailData = DummyProvider.getShipmentDetailData()
        shipmentDetailData.selectedCourier = null

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", true, address, false, 0, "", false, true
        )

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
    fun `When get courier item data and product is recommended Then return courier Ui model`() {
        // Given
        val expected = ShippingCourierUiModel().apply {
            productData = ProductData().apply {
                isRecommend = true
            }
        }
        val courierModelWithOneRecc: List<ShippingCourierUiModel> = listOf(expected)

        // When
        val actual = presenter.getCourierItemData(courierModelWithOneRecc)

        // Then
        assertEquals(actual, expected)
    }

    @Test
    fun `When get courier item data return null Then null is returned`() {
        // Given
        val courierWithNoRecc: List<ShippingCourierUiModel> = listOf(
            ShippingCourierUiModel().apply {
                productData = ProductData()
            }
        )

        // When
        val actual = presenter.getCourierItemData(courierWithNoRecc)

        // Then
        assertNull(actual)
    }

    @Test
    fun `When get courier item data with id Then return the courier Ui model with the same shipper product id`() {
        // Given
        val spId = 24
        val expectedCourier = ShippingCourierUiModel().apply {
            productData = ProductData().apply {
                shipperProductId = spId
            }
        }
        val courierModelWithId: List<ShippingCourierUiModel> = listOf(expectedCourier)

        // When
        val actual = presenter.getCourierItemDataById(spId, courierModelWithId)

        // Then
        assertEquals(actual, expectedCourier)
    }

    @Test
    fun `When get courier item data with id return null Then null is returned`() {
        // Given
        val sId = 38
        val courierModelNoId: List<ShippingCourierUiModel> = listOf(
            ShippingCourierUiModel().apply {
                productData = ProductData().apply {
                    shipperProductId = 24
                }
            }
        )

        // When
        val actual = presenter.getCourierItemDataById(sId, courierModelNoId)

        // Then
        assertNull(actual)
    }

    @Test
    fun `When rates v3 response has eligible courier for promo Then should hit analytic`() {
        // Given
        val ratesV3Response = getRatesResponseWithPromo()
        val shippingRecommendationData =
            ShippingDurationConverter().convertModel(ratesV3Response.ratesData)
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        every {
            responseConverter.fillState(
                any(),
                shopShipments,
                shipmentDetailData.selectedCourier!!.shipperProductId,
                0
            )
        } returns shippingRecommendationData
        presenter.attachView(view)

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            view.sendAnalyticCourierPromo(shippingRecommendationData.shippingDurationUiModels)
        }
    }

    @Test
    fun `When rates v3 response Then should hit promo analytic`() {
        // Given
        val ratesV3Response = getRatesResponseWithPromo()
        val shippingRecommendationData =
            ShippingDurationConverter().convertModel(ratesV3Response.ratesData)
        every { ratesUseCase.execute(any()) } returns Observable.just(shippingRecommendationData)
        every {
            responseConverter.fillState(
                any(),
                shopShipments,
                shipmentDetailData.selectedCourier!!.shipperProductId,
                0
            )
        } returns shippingRecommendationData
        presenter.attachView(view)

        // When
        presenter.loadCourierRecommendation(
            shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", false, address, false, 0, "", false, false
        )

        // Then
        verify {
            view.sendAnalyticPromoLogistic(shippingRecommendationData.listLogisticPromo)
        }
    }

    /*
    * convertServiceListToUiModel
    */

    @Test
    fun `When service list has service with ui rates hidden Then dont show in bottomsheet`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceUiRatesHidden()

        // When
        val actual = presenter.convertServiceListToUiModel(
            shippingRecommendationData.shippingDurationUiModels,
            shippingRecommendationData.listLogisticPromo,
            shippingRecommendationData.preOrderModel,
            false
        )

        // Then
        assertFalse(actual.any { it is ShippingDurationUiModel && it.serviceData.isUiRatesHidden })
    }

    @Test
    fun `When rates v3 response has promo logistic Then show divider in bottomsheet`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceUiRatesHidden()

        // When
        val actual = presenter.convertServiceListToUiModel(
            shippingRecommendationData.shippingDurationUiModels,
            shippingRecommendationData.listLogisticPromo,
            shippingRecommendationData.preOrderModel,
            false
        )
        val lastIndexOfPromoModel = actual.indexOfLast { it is LogisticPromoUiModel }

        // Then
        assertTrue(actual.any { it is LogisticPromoUiModel })
        assertTrue(actual[lastIndexOfPromoModel + 1] is DividerModel)
    }

    @Test
    fun `When pre order model display is true Then show pre order in bottomsheet`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()

        // When
        val actual = presenter.convertServiceListToUiModel(
            shippingRecommendationData.shippingDurationUiModels,
            shippingRecommendationData.listLogisticPromo,
            shippingRecommendationData.preOrderModel,
            false
        )

        // Then
        assertTrue(actual.first() is PreOrderModel)
    }

    @Test
    fun `When should display pre order model and has promo Then show pre order model before promo`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()

        // When
        val actual = presenter.convertServiceListToUiModel(
            shippingRecommendationData.shippingDurationUiModels,
            shippingRecommendationData.listLogisticPromo,
            shippingRecommendationData.preOrderModel,
            false
        )

        // Then
        assertTrue(actual.first() is PreOrderModel)
    }

    @Test
    fun `When in checkout and promo has error code Then initiate showcase`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoEtaError()
        presenter.shippingData = shippingRecommendationData

        // When
        val actual = presenter.convertServiceListToUiModel(
            shippingRecommendationData.shippingDurationUiModels,
            shippingRecommendationData.listLogisticPromo,
            shippingRecommendationData.preOrderModel,
            false
        )

        // Then
        val firstDuration = actual.find { it is ShippingDurationUiModel } as ShippingDurationUiModel
        assertTrue(firstDuration.isShowShowCase)
    }

    @Test
    fun `When in checkout and duration has error code Then show notifier model`() {
        // Given
        val shippingRecommendationData = getShippingDataWithServiceError()

        // When
        val actual = presenter.convertServiceListToUiModel(
            shippingRecommendationData.shippingDurationUiModels,
            shippingRecommendationData.listLogisticPromo,
            shippingRecommendationData.preOrderModel,
            false
        )

        // Then
        assertTrue(actual.first() is NotifierModel)
    }

    /*
    OnChooseDuration
    */

    @Test
    fun `When year end promotion toggle is on and service is pinpoint error Then set pinpoint error flag`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        selectedService.serviceData.error = ErrorServiceData().apply {
            errorId = ERROR_PINPOINT_NEEDED
            errorMessage = "error pinpoint"
        }
        every { view.isToogleYearEndPromotionOn() } returns true
        presenter.attachView(view)

        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        verify {
            view.onShippingDurationAndRecommendCourierChosen(selectedService.shippingCourierViewModelList,
                any(), any(), selectedService.serviceData.serviceId, selectedService.serviceData,
                true)
        }
    }

    @Test
    fun `When select duration and there is recommended courier Then select the recommended courier`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val recommendedCourier = selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }


        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        assert(recommendedCourier?.isSelected == true)
    }

    @Test
    fun `When select duration and there is selected shipper product Id Then select the selected shipper product`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData.selectedShipperProductId = selectedShipperProductId
        val recommendedCourier = selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }


        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        assert(recommendedCourier?.isSelected == true)
    }

    @Test
    fun `When select duration and there are recommended courier and selected shipper product Id Then select the selected shipper product`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData.selectedShipperProductId = selectedShipperProductId
        val selectedShipper = selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }
        val recommendCourier = selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }

        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        assert(recommendCourier?.isSelected == false)
        assert(selectedShipper?.isSelected == true)
    }

    @Test
    fun `When select duration and get selected courier and courier is pinpoint error Then set pinpoint error flag`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val product = selectedService.shippingCourierViewModelList.first()
        product.productData.error = ErrorProductData().apply {
            errorId = ERROR_PINPOINT_NEEDED
            errorMessage = "error pinpoint"
        }
        presenter.attachView(view)

        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        verify {
            view.onShippingDurationAndRecommendCourierChosen(selectedService.shippingCourierViewModelList,
                any(), any(), selectedService.serviceData.serviceId, selectedService.serviceData,
                true)
        }
    }

    @Test
    fun `When select duration and get selected courier and courier is pinpoint error Then set range price to error message`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val product = selectedService.shippingCourierViewModelList.first()
        product.productData.error = ErrorProductData().apply {
            errorId = ERROR_PINPOINT_NEEDED
            errorMessage = "error pinpoint"
        }
        presenter.attachView(view)

        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        assertTrue(product.serviceData.texts.textRangePrice == product.productData.error.errorMessage)
    }

    @Test
    fun `When select duration and there is recommended courier Then send recommended courier data`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val recommendedCourier = selectedService.shippingCourierViewModelList.find { it.productData.isRecommend }
        presenter.attachView(view)


        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        verify {
            view.onShippingDurationAndRecommendCourierChosen(selectedService.shippingCourierViewModelList,
                recommendedCourier, any(), 0, selectedService.serviceData,
                false)
        }
    }

    @Test
    fun `When select duration and there is selected shipper product Id Then send selected shipper product data`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData.selectedShipperProductId = selectedShipperProductId
        val recommendedCourier = selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }
        presenter.attachView(view)


        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        verify {
            view.onShippingDurationAndRecommendCourierChosen(selectedService.shippingCourierViewModelList,
                recommendedCourier, any(), 0, selectedService.serviceData,
                false)
        }
    }

    @Test
    fun `When select duration and there is recommended courier and selected shipper product Id Then send the selected shipper product data`() {
        // Given
        // selected shipping duration ui model
        val selectedService = getShippingDataWithPromoAndPreOrderModel().shippingDurationUiModels.first()
        val selectedShipperProductId = 24
        selectedService.serviceData.selectedShipperProductId = selectedShipperProductId
        val selectedShipper = selectedService.shippingCourierViewModelList.find { it.productData.shipperProductId == selectedShipperProductId }
        presenter.attachView(view)

        // When
        // onChooseDuration
        presenter.onChooseDuration(selectedService.shippingCourierViewModelList, 0, selectedService.serviceData)

        // Then
        verify {
            view.onShippingDurationAndRecommendCourierChosen(selectedService.shippingCourierViewModelList,
                selectedShipper, any(), 0, selectedService.serviceData,
                false)
        }
    }

    /*
    * OnLogisticPromoClicked
    */

    @Test
    fun `When select promo logistic Then send courier data and service data from promo logistic data`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        val selectedBo = shippingRecommendationData.listLogisticPromo.first()
        presenter.shippingData = shippingRecommendationData
        presenter.attachView(view)

        // When
        presenter.onLogisticPromoClicked(selectedBo)

        // Then
        verify {
            view.onLogisticPromoChosen(any(), any(), any(), any(), selectedBo.promoCode, selectedBo.serviceId, selectedBo)
        }
    }

    @Test
    fun `When select promo logistic and service data is not found Then should show courier promo error`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        val selectedBo = shippingRecommendationData.listLogisticPromo.first().copy(serviceId = 0)
        shippingRecommendationData.listLogisticPromo = listOf(selectedBo)

        presenter.shippingData = shippingRecommendationData
        presenter.attachView(view)

        // When
        presenter.onLogisticPromoClicked(selectedBo)

        // Then
        verify {
            view.showPromoCourierNotAvailable()
        }
    }

    @Test
    fun `When select promo logistic and courier data is not found Then should show courier promo error`() {
        // Given
        val shippingRecommendationData = getShippingDataWithPromoAndPreOrderModel()
        val selectedBo = shippingRecommendationData.listLogisticPromo.first()
        val serviceDataForSelectedBo = shippingRecommendationData.shippingDurationUiModels.find { it.serviceData.serviceId == selectedBo.serviceId }
        serviceDataForSelectedBo?.shippingCourierViewModelList = listOf()

        presenter.shippingData = shippingRecommendationData
        presenter.attachView(view)

        // When
        presenter.onLogisticPromoClicked(selectedBo)

        // Then
        verify {
            view.showPromoCourierNotAvailable()
        }
    }

    @Test
    fun `When presenter detached Then all usecases is unsubscribed`() {
        // When
        presenter.detachView()

        // Then
        verify {
            ratesUseCase.unsubscribe()
            ratesApiUseCase.unsubscribe()
        }
    }
}
