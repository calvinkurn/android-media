package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ProductData
import com.tokopedia.logisticcart.datamock.DummyProvider
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import rx.Observable

class ShippingDurationPresenterTest {

    private val ratesUseCase: GetRatesUseCase = mockk(relaxed = true)
    private val ratesApiUseCase: GetRatesApiUseCase = mockk(relaxed = true)
    private val responseConverter: RatesResponseStateConverter = mockk()
    private val courierConverter: ShippingCourierConverter = mockk(relaxed = true)
    val view: ShippingDurationContract.View = mockk(relaxed = true)
    lateinit var presenter: ShippingDurationPresenter

    private val shipmentDetailData: ShipmentDetailData = DummyProvider.getShipmentDetailData()
    private val shopShipments: List<ShopShipment> = DummyProvider.getShopShipments()
    val products: List<Product> = DummyProvider.getProducts()
    val address: RecipientAddressModel = DummyProvider.getAddress()

    @Before
    fun setup() {
        presenter = ShippingDurationPresenter(ratesUseCase, ratesApiUseCase,
                responseConverter, courierConverter)
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
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
                shopShipments, -1, false, false, "",
                products, "1479278-30-740525-99367774", false, address, false, 0, "", "")

        // Then
        verify {
            view.showData(any(), any(), any())
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
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", true, addressData, false, 0, "", "")

        // Then
        verify {
            view.showData(any(), any(), any())
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
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
                shopShipments, -1, false, false, "",
                products, "1479278-30-740525-99367774", false, address, false, 0, "", "")

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
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
                shopShipments, -1, false, false, "",
                products, "1479278-30-740525-99367774", false, address, false, 0, "", "")

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
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
                shopShipments, -1, false, false, "",
                products, "1479278-30-740525-99367774", false, address, false, 0, "", "")

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
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
                shopShipments, -1, false, false, "",
                products, "1479278-30-740525-99367774", false, address, false, 0, "", "")

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
        every {view.isDisableCourierPromo()} returns true

        val shipmentDetailData = DummyProvider.getShipmentDetailData()
        shipmentDetailData.selectedCourier = null

        // When
        presenter.loadCourierRecommendation(shipmentDetailData, 0,
            shopShipments, -1, false, false, "",
            products, "1479278-30-740525-99367774", true, address, false, 0, "", "")

        // Then
        assertEquals(shippingDurationUIModels.filter { it.serviceData.isPromo == 0 }.size, shippingDurationUIModels.size)
        assertEquals(productsShipping.filter { product -> product.any { item -> item.promoCode.isEmpty() } }.size, productsShipping.size)
    }

    @Test
    fun `When get courier item data trigger courier converter Then courier converter is called`() {
        // Given
        val courierModelWithOneRecc: List<ShippingCourierUiModel> = listOf(
                ShippingCourierUiModel().apply {
                    productData = ProductData().apply {
                        isRecommend = true
                    }
                }
        )

        // When
        presenter.getCourierItemData(courierModelWithOneRecc)

        // Then
        verify {
            courierConverter.convertToCourierItemData(any())
        }
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
    fun `When get recommendation courier item data but product is hidden Then null is returned`() {
        // Given
        val courierWithNoRecc: List<ShippingCourierUiModel> = listOf(
                ShippingCourierUiModel().apply {
                    productData = ProductData().apply {
                        isRecommend = true
                        isUiRatesHidden = true
                    }
                }
        )

        // When
        val actual = presenter.getCourierItemData(courierWithNoRecc)

        // Then
        assertNull(actual)
    }

    @Test
    fun `When get recommendation courier item data but product is error Then null is returned`() {
        // Given
        val courierWithNoRecc: List<ShippingCourierUiModel> = listOf(
                ShippingCourierUiModel().apply {
                    productData = ProductData().apply {
                        isRecommend = true
                        error = ErrorProductData().apply {
                            errorId = ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED
                            errorMessage = "error"
                        }
                    }
                }
        )

        // When
        val actual = presenter.getCourierItemData(courierWithNoRecc)

        // Then
        assertNull(actual)
    }

    @Test
    fun `When get recommendation courier item data but product error message is empty Then return courier`() {
        // Given
        val courierData = ShippingCourierUiModel().apply {
            productData = ProductData().apply {
                isRecommend = true
                error = ErrorProductData().apply {
                    errorMessage = ""
                }
            }
        }
        val courierWithNoRecc: List<ShippingCourierUiModel> = listOf(
                courierData
        )

        // When
        val actual = presenter.getCourierItemData(courierWithNoRecc)

        // Then
        assertNotNull(actual)
    }

    @Test
    fun `When get courier item data with id trigger courier converter Then courier converter is called`() {
        // Given
        val spId = 24
        val courierModelWithId: List<ShippingCourierUiModel> = listOf(
                ShippingCourierUiModel().apply {
                    productData = ProductData().apply {
                        shipperProductId = spId
                    }
                }
        )

        // When
        presenter.getCourierItemDataById(spId, courierModelWithId)

        // Then
        verify {
            courierConverter.convertToCourierItemData(any())
        }
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
