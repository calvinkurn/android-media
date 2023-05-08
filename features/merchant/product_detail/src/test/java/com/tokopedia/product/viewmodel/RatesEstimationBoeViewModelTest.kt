package com.tokopedia.product.viewmodel

import com.tokopedia.product.estimasiongkir.data.model.DeliveryService
import com.tokopedia.product.estimasiongkir.data.model.RatesEstimateRequest
import com.tokopedia.product.estimasiongkir.data.model.ScheduledDeliveryRatesModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingHeaderDataModel
import com.tokopedia.product.estimasiongkir.data.model.shipping.ProductShippingServiceDataModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesEstimationModel
import com.tokopedia.product.estimasiongkir.data.model.v3.RatesModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceModel
import com.tokopedia.product.estimasiongkir.data.model.v3.ServiceProduct
import com.tokopedia.product.estimasiongkir.usecase.GetRatesEstimateUseCase
import com.tokopedia.product.estimasiongkir.usecase.GetScheduledDeliveryRatesUseCase
import com.tokopedia.product.estimasiongkir.view.viewmodel.RatesEstimationBoeViewModel
import com.tokopedia.product.util.BaseProductViewModelTest
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yehezkiel on 03/03/21
 */
class RatesEstimationBoeViewModelTest : BaseProductViewModelTest() {

    @RelaxedMockK
    private lateinit var ratesUseCase: GetRatesEstimateUseCase

    @RelaxedMockK
    private lateinit var userSessionInterface: UserSessionInterface

    @RelaxedMockK
    private lateinit var scheduledDeliveryUseCase: GetScheduledDeliveryRatesUseCase

    private val viewModel: RatesEstimationBoeViewModel by lazy {
        RatesEstimationBoeViewModel(
            ratesUseCase,
            scheduledDeliveryUseCase,
            userSessionInterface,
            CoroutineTestDispatchersProvider
        )
    }

    private val service = listOf(
        ServiceModel(
            status = 200,
            products = listOf(ServiceProduct(status = 200))
        )
    )

    private val serviceWithHiddenFlag = listOf(
        ServiceModel(
            status = 200,
            products = listOf(ServiceProduct(status = 200, uiRatesHidden = true))
        )
    )

    private val ratesResponse = RatesEstimationModel(rates = RatesModel(services = service))

    private val ratesResponse2 =
        RatesEstimationModel(rates = RatesModel(services = serviceWithHiddenFlag))

    private val scheduledDeliveryResponseEmpty = ScheduledDeliveryRatesModel()

    private val scheduledDeliveryResponse = ScheduledDeliveryRatesModel(
        deliveryServices = listOf(DeliveryService())
    )

    @Test
    fun `test request params non bo`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        val mockRequest = RatesEstimateRequest(
            productWeight = 2F,
            shopDomain = "shopDomain",
            origin = "origin",
            shopId = "123",
            productId = "321",
            productWeightUnit = "weightunit",
            isFulfillment = true,
            destination = "destination",
            poTime = 1L,
            boType = 0,
            shopTier = 1,
            addressId = "addressid",
            warehouseId = "111",
            orderValue = 1,
            boMetadata = "123"
        )

        viewModel.setRatesRequest(mockRequest)

        val slotRequestParams = slot<Map<String, Any?>>()
        coVerify {
            ratesUseCase.executeOnBackground(capture(slotRequestParams), any())
        }

        val requestParams = slotRequestParams.captured
        Assert.assertEquals(requestParams["weight"], 0.002F)
        Assert.assertEquals(requestParams["domain"], "shopDomain")
        Assert.assertEquals(requestParams["origin"], "origin")
        Assert.assertEquals(requestParams["shop_id"], "123")
        Assert.assertEquals(requestParams["product_id"], "321")
        Assert.assertEquals(requestParams["is_fulfillment"], true)
        Assert.assertEquals(requestParams["destination"], "destination")
        Assert.assertEquals(requestParams["po_time"], 1L)
        Assert.assertEquals(requestParams["free_shipping_flag"], 0)
        Assert.assertEquals(requestParams["shop_tier"], 1)
        Assert.assertEquals(requestParams["unique_id"], "addressid-123-1-111")
        Assert.assertEquals(requestParams["order_value"], 1)
        Assert.assertEquals(requestParams["bo_metadata"], "123")
    }

    @Test
    fun `test request params bo tokonow`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponseEmpty

        val expectedBoMetaData =
            "{\"bo_metadata\":{\"bo_type\":3,\"bo_eligibilities\":[{\"key\":\"is_tokonow\",\"value\":\"true\"},{\"key\":\"campaign_ids\",\"value\":\"123\"}]}}"
        val mockRequest = RatesEstimateRequest(
            productWeight = 2F,
            shopDomain = "shopDomain",
            origin = "origin",
            shopId = "123",
            productId = "321",
            productWeightUnit = "weightunit",
            isFulfillment = true,
            destination = "destination",
            poTime = 1L,
            boType = 3,
            shopTier = 1,
            addressId = "addressid",
            warehouseId = "111",
            orderValue = 1,
            boMetadata = expectedBoMetaData
        )

        viewModel.setRatesRequest(mockRequest)

        val slotRequestParams = slot<Map<String, Any?>>()
        coVerify {
            ratesUseCase.executeOnBackground(capture(slotRequestParams), any())
        }

        val requestParams = slotRequestParams.captured
        Assert.assertEquals(requestParams["weight"], 0.002F)
        Assert.assertEquals(requestParams["domain"], "shopDomain")
        Assert.assertEquals(requestParams["origin"], "origin")
        Assert.assertEquals(requestParams["shop_id"], "123")
        Assert.assertEquals(requestParams["product_id"], "321")
        Assert.assertEquals(requestParams["is_fulfillment"], true)
        Assert.assertEquals(requestParams["destination"], "destination")
        Assert.assertEquals(requestParams["po_time"], 1L)
        Assert.assertEquals(requestParams["free_shipping_flag"], 3)
        Assert.assertEquals(requestParams["shop_tier"], 1)
        Assert.assertEquals(requestParams["unique_id"], "addressid-123-1-111")
        Assert.assertEquals(requestParams["order_value"], 1)
        Assert.assertEquals(requestParams["bo_metadata"], expectedBoMetaData)
    }

    @Test
    fun `test request params now15`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponseEmpty

        val expectedBoMetaData =
            "{\"bo_metadata\":{\"bo_type\":4,\"bo_eligibilities\":[{\"key\":\"is_tokonow\",\"value\":\"true\"},{\"key\":\"campaign_ids\",\"value\":\"123\"}]}}"
        val mockRequest = RatesEstimateRequest(
            productWeight = 2F,
            shopDomain = "shopDomain",
            origin = "origin",
            shopId = "123",
            productId = "321",
            productWeightUnit = "weightunit",
            isFulfillment = true,
            destination = "destination",
            poTime = 1L,
            boType = 4,
            shopTier = 1,
            addressId = "addressid",
            warehouseId = "111",
            orderValue = 1,
            boMetadata = expectedBoMetaData
        )

        viewModel.setRatesRequest(mockRequest)

        val slotRequestParams = slot<Map<String, Any?>>()
        coVerify {
            ratesUseCase.executeOnBackground(capture(slotRequestParams), any())
        }

        val requestParams = slotRequestParams.captured
        Assert.assertEquals(requestParams["weight"], 0.002F)
        Assert.assertEquals(requestParams["domain"], "shopDomain")
        Assert.assertEquals(requestParams["origin"], "origin")
        Assert.assertEquals(requestParams["shop_id"], "123")
        Assert.assertEquals(requestParams["product_id"], "321")
        Assert.assertEquals(requestParams["is_fulfillment"], true)
        Assert.assertEquals(requestParams["destination"], "destination")
        Assert.assertEquals(requestParams["po_time"], 1L)
        Assert.assertEquals(requestParams["free_shipping_flag"], 4)
        Assert.assertEquals(requestParams["shop_tier"], 1)
        Assert.assertEquals(requestParams["unique_id"], "addressid-123-1-111")
        Assert.assertEquals(requestParams["order_value"], 1)
        Assert.assertEquals(requestParams["bo_metadata"], expectedBoMetaData)
    }

    @Test
    fun `test request params bo plus`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponseEmpty

        val expectedBoMetaData =
            "{\"bo_metadata\":{\"bo_type\":5,\"bo_eligibilities\":[{\"key\":\"is_tokonow\",\"value\":\"false\"},{\"key\":\"campaign_ids\",\"value\":\"123\"}]}}"
        val mockRequest = RatesEstimateRequest(
            productWeight = 2F,
            shopDomain = "shopDomain",
            origin = "origin",
            shopId = "123",
            productId = "321",
            productWeightUnit = "weightunit",
            isFulfillment = true,
            destination = "destination",
            poTime = 1L,
            boType = 5,
            shopTier = 1,
            addressId = "addressid",
            warehouseId = "111",
            orderValue = 1,
            boMetadata = expectedBoMetaData
        )

        viewModel.setRatesRequest(mockRequest)

        val slotRequestParams = slot<Map<String, Any?>>()
        coVerify {
            ratesUseCase.executeOnBackground(capture(slotRequestParams), any())
        }

        val requestParams = slotRequestParams.captured
        Assert.assertEquals(requestParams["weight"], 0.002F)
        Assert.assertEquals(requestParams["domain"], "shopDomain")
        Assert.assertEquals(requestParams["origin"], "origin")
        Assert.assertEquals(requestParams["shop_id"], "123")
        Assert.assertEquals(requestParams["product_id"], "321")
        Assert.assertEquals(requestParams["is_fulfillment"], true)
        Assert.assertEquals(requestParams["destination"], "destination")
        Assert.assertEquals(requestParams["po_time"], 1L)
        Assert.assertEquals(requestParams["free_shipping_flag"], 5)
        Assert.assertEquals(requestParams["shop_tier"], 1)
        Assert.assertEquals(requestParams["unique_id"], "addressid-123-1-111")
        Assert.assertEquals(requestParams["order_value"], 1)
        Assert.assertEquals(requestParams["bo_metadata"], expectedBoMetaData)
    }

    @Test
    fun `test request params bo plus dt`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponseEmpty

        val expectedBoMetaData =
            "{\"bo_metadata\":{\"bo_type\":6,\"bo_eligibilities\":[{\"key\":\"is_tokonow\",\"value\":\"false\"},{\"key\":\"campaign_ids\",\"value\":\"123\"}]}}"
        val mockRequest = RatesEstimateRequest(
            productWeight = 2F,
            shopDomain = "shopDomain",
            origin = "origin",
            shopId = "123",
            productId = "321",
            productWeightUnit = "weightunit",
            isFulfillment = true,
            destination = "destination",
            poTime = 1L,
            boType = 6,
            shopTier = 1,
            addressId = "addressid",
            warehouseId = "111",
            orderValue = 1,
            boMetadata = expectedBoMetaData
        )

        viewModel.setRatesRequest(mockRequest)

        val slotRequestParams = slot<Map<String, Any?>>()
        coVerify {
            ratesUseCase.executeOnBackground(capture(slotRequestParams), any())
        }

        val requestParams = slotRequestParams.captured
        Assert.assertEquals(requestParams["weight"], 0.002F)
        Assert.assertEquals(requestParams["domain"], "shopDomain")
        Assert.assertEquals(requestParams["origin"], "origin")
        Assert.assertEquals(requestParams["shop_id"], "123")
        Assert.assertEquals(requestParams["product_id"], "321")
        Assert.assertEquals(requestParams["is_fulfillment"], true)
        Assert.assertEquals(requestParams["destination"], "destination")
        Assert.assertEquals(requestParams["po_time"], 1L)
        Assert.assertEquals(requestParams["free_shipping_flag"], 6)
        Assert.assertEquals(requestParams["shop_tier"], 1)
        Assert.assertEquals(requestParams["unique_id"], "addressid-123-1-111")
        Assert.assertEquals(requestParams["order_value"], 1)
        Assert.assertEquals(requestParams["bo_metadata"], expectedBoMetaData)
    }

    @Test
    fun `on success get rates data`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponseEmpty

        viewModel.setRatesRequest(RatesEstimateRequest())

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value)
        Assert.assertTrue(viewModel.ratesVisitableResult.value is Success)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.first() is ProductShippingHeaderDataModel)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data[1] is ProductShippingServiceDataModel)
        print((viewModel.ratesVisitableResult.value as Success).data.size)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.size == 2)
    }

    @Test
    fun `on fail get rates data`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } throws Throwable()

        viewModel.setRatesRequest(RatesEstimateRequest())

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value is Fail)
    }

    @Test
    fun `on success rates with ui rates hidden true should not included in list`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse2

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponseEmpty

        viewModel.setRatesRequest(RatesEstimateRequest())

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value)
        Assert.assertTrue(viewModel.ratesVisitableResult.value is Success)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.first() is ProductShippingHeaderDataModel)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.size == 1)
    }

    @Test
    fun `on scheduled delivery success`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse2

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponse

        viewModel.setRatesRequest(RatesEstimateRequest(isScheduled = true))

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value)
        Assert.assertTrue(viewModel.ratesVisitableResult.value is Success)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.first() is ProductShippingHeaderDataModel)
        Assert.assertTrue((viewModel.ratesVisitableResult.value as Success).data.size == 2)
    }

    @Test
    fun `on fail get scheduled delivery data`() {
        viewModel.ratesVisitableResult.observeForever { }

        coEvery {
            ratesUseCase.executeOnBackground(any(), any())
        } returns ratesResponse

        coEvery {
            scheduledDeliveryUseCase.execute(any(), any(), true)
        } returns scheduledDeliveryResponse

        viewModel.setRatesRequest(RatesEstimateRequest(isScheduled = true))

        coVerify {
            ratesUseCase.executeOnBackground(any(), any())
        }

        Assert.assertNotNull(viewModel.ratesVisitableResult.value is Fail)
    }
}
