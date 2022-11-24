package com.tokopedia.editshipping.ui.customproductlogistic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.editshipping.util.CPLDataProvider
import com.tokopedia.logisticCommon.data.mapper.CustomProductLogisticMapper
import com.tokopedia.logisticCommon.data.repository.CustomProductLogisticUseCase
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CustomProductLogisticViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: CustomProductLogisticUseCase = mockk(relaxed = true)
    private val mapper = CustomProductLogisticMapper()

    private val cplListObserver: Observer<CPLState> =
        mockk(relaxed = true)

    private lateinit var customProductLogisticViewModel: CustomProductLogisticViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        customProductLogisticViewModel = CustomProductLogisticViewModel(repo, mapper)
        customProductLogisticViewModel.cplState.observeForever(cplListObserver)
    }

    @After
    fun setDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Get CPL List success`() {
        val shipperServicesId = 1L
        val cplParam = listOf<Long>(6, 22)
        val mockResponse = CPLDataProvider.provideCPLResponse()

        coEvery { repo(any()) } returns mockResponse
        customProductLogisticViewModel.getCPLList(
            1234,
            9876,
            arrayListOf(shipperServicesId),
            cplParam
        )
        verify { cplListObserver.onChanged(match { it is CPLState.FirstLoad }) }
    }

    @Test
    fun `Get CPL List failed`() {
        coEvery { repo(any()) } throws defaultThrowable
        customProductLogisticViewModel.getCPLList(1234, 9876, null, null)
        verify { cplListObserver.onChanged(match { it is CPLState.Failed }) }
    }

    @Test
    fun `WHEN check shipper checkbox THEN set shipper and shipper service selected to checkbox active state`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        loadMockCplData(mockResponse)
        val selectedShipperId = 11L

        customProductLogisticViewModel.setAllShipperServiceState(true, selectedShipperId)

        val result =
            customProductLogisticViewModel.cplData.shipperList.getOrNull(0)?.shipper?.find { s -> s.shipperId == selectedShipperId }!!
        assertTrue(result.isActive)
        assertTrue(result.shipperProduct.all { it.isActive })
    }

    @Test
    fun `WHEN check shipper checkbox but cpl is failed THEN do nothing`() {
        loadMockCplData(null)
        val selectedShipperId = 11L

        customProductLogisticViewModel.setAllShipperServiceState(true, selectedShipperId)

        assertFalse(customProductLogisticViewModel.cplState.value is CPLState.Update)
    }

    @Test
    fun `WHEN check shipper checkbox but shipper id not found THEN do nothing`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        mockResponse.response.data.shipperList[0].shipper[0].shipperId = 0
        loadMockCplData(mockResponse)
        val selectedShipperId = 11L

        customProductLogisticViewModel.setAllShipperServiceState(true, selectedShipperId)

        val result = customProductLogisticViewModel.cplData.shipperList[0].shipper[1]
        assertTrue(result.shipperName == mockResponse.response.data.shipperList[0].shipper[0].shipperName)
        assertFalse(result.isActive)
    }

    @Test
    fun `WHEN check shipper service checkbox THEN set shipper service selected to checkbox active state`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        loadMockCplData(mockResponse)
        val selectedShipperServiceId = 18L

        customProductLogisticViewModel.setShipperServiceState(true, selectedShipperServiceId)

        val shipper =
            customProductLogisticViewModel.cplData.shipperList.getOrNull(0)?.shipper?.find { s -> s.shipperProduct.find { it.shipperProductId == selectedShipperServiceId } != null }!!
        val result =
            shipper.shipperProduct.find { sp -> sp.shipperProductId == selectedShipperServiceId }!!
        assertTrue(result.isActive)
        assertTrue(shipper.isActive)
    }

    @Test
    fun `WHEN uncheck shipper service checkbox and none shipper service is selected THEN set shipper selected to false`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        loadMockCplData(mockResponse)
        val selectedShipperServiceId = 18L

        customProductLogisticViewModel.setShipperServiceState(false, selectedShipperServiceId)

        val shipper =
            customProductLogisticViewModel.cplData.shipperList.getOrNull(0)?.shipper?.find { s -> s.shipperProduct.find { it.shipperProductId == selectedShipperServiceId } != null }!!
        val result =
            shipper.shipperProduct.find { sp -> sp.shipperProductId == selectedShipperServiceId }!!
        assertFalse(result.isActive)
        assertFalse(shipper.isActive)
    }

    @Test
    fun `WHEN check shipper service checkbox and none shipper service has the same shipper id THEN do nothing`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        loadMockCplData(mockResponse)
        val selectedShipperServiceId = 19L

        customProductLogisticViewModel.setShipperServiceState(false, selectedShipperServiceId)

        val shipper =
            customProductLogisticViewModel.cplData.shipperList.getOrNull(0)?.shipper?.find { s -> s.shipperProduct.find { it.shipperProductId == selectedShipperServiceId } != null }
        assertNull(shipper)
    }

    @Test
    fun `WHEN check whitelabel service checkbox THEN set shipper and shipper service selected to true`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        loadMockCplData(mockResponse)
        val selectedShipperIds = listOf<Long>(28, 37)

        customProductLogisticViewModel.setWhitelabelServiceState(selectedShipperIds, false)

        val result =
            customProductLogisticViewModel.cplData.shipperList.getOrNull(0)?.shipper?.find { s -> s.isWhitelabel }!!
        assertFalse(result.isActive)
        assertFalse(result.shipperProduct.all { it.isActive })
    }

    @Test
    fun `WHEN check whitelabel service checkbox but no whitelabel service with same list of shipper ids THEN do nothing`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        mockResponse.response.data.shipperList[0].whitelabelShipper[0].isActive = true
        loadMockCplData(mockResponse)
        val selectedShipperIds = listOf<Long>(28, 38)

        customProductLogisticViewModel.setWhitelabelServiceState(selectedShipperIds, false)

        val result =
            customProductLogisticViewModel.cplData.shipperList.getOrNull(0)?.shipper?.find { s -> s.isWhitelabel }!!
        assertTrue(result.isActive)
        assertTrue(result.shipperProduct.all { it.isActive })
    }

    @Test
    fun `WHEN check whitelabel service checkbox but failed to get CPL data THEN do nothing`() {
        loadMockCplData(null)
        val selectedShipperIds = listOf<Long>(28, 38)

        customProductLogisticViewModel.setWhitelabelServiceState(selectedShipperIds, false)

        assertFalse(customProductLogisticViewModel.cplState.value is CPLState.Update)
    }

    @Test
    fun `WHEN already show on boarding THEN should set onboarding flag to false`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        loadMockCplData(mockResponse)

        customProductLogisticViewModel.setAlreadyShowOnBoarding()

        val result = customProductLogisticViewModel.cplData
        assertFalse(result.shouldShowOnBoarding)
    }

    @Test
    fun `WHEN shipper products are all hidden THEN dont show shipper`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val indexShipperGroup = 0
        val indexShipper = 1
        val indexShipperProduct = 0
        val hiddenShipper = mockResponse.response.data.shipperList[indexShipperGroup].shipper[indexShipper]
        val hiddenShipperService = hiddenShipper.shipperProduct[indexShipperProduct]
        hiddenShipperService.uiHidden = true
        loadMockCplData(mockResponse)

        val result = customProductLogisticViewModel.cplData
        assertNull(result.shipperList[indexShipperGroup].shipper.find { shipperCPLModel -> shipperCPLModel.shipperId == hiddenShipper.shipperId })
    }

    @Test
    fun `WHEN shipper products hidden THEN dont show shipper product`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val indexShipperGroup = 0
        val indexShipper = 2
        val indexShipperProduct = 1
        val hiddenShipper = mockResponse.response.data.shipperList[indexShipperGroup].shipper[indexShipper]
        val hiddenShipperService = hiddenShipper.shipperProduct[indexShipperProduct]
        hiddenShipperService.uiHidden = true
        loadMockCplData(mockResponse)

        val result = customProductLogisticViewModel.cplData
        assertNull(result.shipperList[indexShipperGroup].shipper[indexShipper].shipperProduct.find { product -> product.shipperProductId == hiddenShipperService.shipperProductId })
    }

    @Test
    fun `WHEN whitelabel service available THEN the products active state is according the service active state`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val indexShipperGroup = 0
        val indexShipper = 0
        val whitelabelShipper = mockResponse.response.data.shipperList[indexShipperGroup].whitelabelShipper[indexShipper]
        loadMockCplData(mockResponse)

        val result = customProductLogisticViewModel.cplData
        assertTrue(result.shipperList[indexShipperGroup].shipper[indexShipper].isWhitelabel)
        assertTrue(result.shipperList[indexShipperGroup].shipper[indexShipper].shipperProduct[indexShipper].isActive == whitelabelShipper.isActive)
    }

    @Test
    fun `WHEN whitelabel service chosen as draft THEN active state should be true`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val indexShipperGroup = 0
        val indexShipper = 0
        val whitelabelShipper = mockResponse.response.data.shipperList[indexShipperGroup].whitelabelShipper[indexShipper]
        whitelabelShipper.isActive = false
        val draft = whitelabelShipper.shipperProductIds
        loadMockCplData(mockResponse, draft)

        val result = customProductLogisticViewModel.cplData
        assertTrue(result.shipperList[indexShipperGroup].shipper[indexShipper].isWhitelabel)
        assertFalse(result.shipperList[indexShipperGroup].shipper[indexShipper].shipperProduct[indexShipper].isActive == whitelabelShipper.isActive)
        assertTrue(result.shipperList[indexShipperGroup].shipper[indexShipper].shipperProduct[indexShipper].isActive)
    }

    @Test
    fun `WHEN product service chosen as draft THEN active state should be true`() {
        val mockResponse = CPLDataProvider.provideCPLResponse()
        val indexShipperGroup = 0
        val indexShipper = 2
        val indexShipperProduct = 0
        val shipperProduct = mockResponse.response.data.shipperList[indexShipperGroup].shipper[indexShipper].shipperProduct[indexShipperProduct]
        shipperProduct.isActive = false
        val draft = listOf(shipperProduct.shipperProductId)
        loadMockCplData(mockResponse, draft)

        val result = customProductLogisticViewModel.cplData
        assertTrue(result.shipperList[indexShipperGroup].shipper[indexShipper].shipperProduct[indexShipperProduct].isActive)
    }

    private fun loadMockCplData(mockData: OngkirGetCPLQGLResponse?, draft: List<Long>? = null) {
        if (mockData != null) {
            val uiModel = mapper.mapCPLData(mockData.response.data, draft, true)
            customProductLogisticViewModel.cplData = uiModel
        }
    }
}
