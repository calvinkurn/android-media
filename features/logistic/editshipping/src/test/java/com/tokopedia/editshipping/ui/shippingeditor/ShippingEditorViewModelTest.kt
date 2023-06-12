package com.tokopedia.editshipping.ui.shippingeditor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.editshipping.data.repository.ShippingEditorRepository
import com.tokopedia.editshipping.domain.mapper.ShipperDetailMapper
import com.tokopedia.editshipping.domain.mapper.ShippingEditorMapper
import com.tokopedia.editshipping.domain.mapper.ValidateShippingNewMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperDetailModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperListModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperTickerModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorState
import com.tokopedia.editshipping.domain.model.shippingEditor.ValidateShippingEditorModel
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.editshipping.util.MultiLocShippingEditorDataProvider
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperDetailResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperListResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperTickerResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.SaveShippingEditorResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.SaveShippingResponse
import com.tokopedia.logisticCommon.data.response.shippingeditor.ValidateShippingEditorResponse
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibility
import com.tokopedia.logisticCommon.data.response.shoplocation.KeroGetRolloutEligibilityResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShippingEditorViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val shopRepo: ShopLocationRepository = mockk(relaxed = true)
    private val shippingEditorRepo: ShippingEditorRepository = mockk(relaxed = true)
    private val shippingEditorMapper = ShippingEditorMapper()
    private val validateShippingMapper = ValidateShippingNewMapper()
    private val detailMapper = ShipperDetailMapper()

    private lateinit var shippingEditorViewModel: ShippingEditorViewModel

    private val shopWhitelistObserver: Observer<ShippingEditorState<KeroGetRolloutEligibility>> =
        mockk(relaxed = true)
    private val shipperListObserver: Observer<ShippingEditorState<ShipperListModel>> =
        mockk(relaxed = true)
    private val shipperTickerListObserver: Observer<ShippingEditorState<ShipperTickerModel>> =
        mockk(relaxed = true)
    private val shipperDetailsObserver: Observer<ShippingEditorState<ShipperDetailModel>> =
        mockk(relaxed = true)
    private val validateDataShipperObserver: Observer<ShippingEditorState<ValidateShippingEditorModel>> =
        mockk(relaxed = true)
    private val saveShippingDataObserver: Observer<ShippingEditorState<SaveShippingResponse>> =
        mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        shippingEditorViewModel = ShippingEditorViewModel(
            shopRepo,
            shippingEditorRepo,
            shippingEditorMapper,
            validateShippingMapper,
            detailMapper
        )
        shippingEditorViewModel.shopWhitelist.observeForever(shopWhitelistObserver)
        shippingEditorViewModel.shipperList.observeForever(shipperListObserver)
        shippingEditorViewModel.shipperTickerList.observeForever(shipperTickerListObserver)
        shippingEditorViewModel.shipperDetail.observeForever(shipperDetailsObserver)
        shippingEditorViewModel.validateDataShipper.observeForever(validateDataShipperObserver)
        shippingEditorViewModel.saveShippingData.observeForever(saveShippingDataObserver)
    }

    @Test
    fun `Get shop location whitelist success`() {
        coEvery { shopRepo.getShopLocationWhitelist(any()) } returns KeroGetRolloutEligibilityResponse()
        shippingEditorViewModel.getWhitelistData(1234)
        verify { shopWhitelistObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Get shop location whitelist failed`() {
        coEvery { shopRepo.getShopLocationWhitelist(any()) } throws defaultThrowable
        shippingEditorViewModel.getWhitelistData(1234)
        verify { shopWhitelistObserver.onChanged(match { it is ShippingEditorState.Fail }) }
    }

    @Test
    fun `Get shipper list success`() {
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns GetShipperListResponse()
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns GetShipperTickerResponse()
        shippingEditorViewModel.getShipperList(1234)
        verify { shipperListObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Get shipper list failed`() {
        coEvery { shippingEditorRepo.getShippingEditor(any()) } throws defaultThrowable
        shippingEditorViewModel.getShipperList(1234)
        verify { shipperListObserver.onChanged(match { it is ShippingEditorState.Fail }) }
    }

    @Test
    fun `Get shipper ticker list success`() {
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns GetShipperTickerResponse()
        shippingEditorViewModel.getShipperTickerList(1234, ShipperListModel())
        verify { shipperTickerListObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Get shipper ticker list failed`() {
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } throws defaultThrowable
        shippingEditorViewModel.getShipperTickerList(1234, ShipperListModel())
        verify { shipperTickerListObserver.onChanged(match { it is ShippingEditorState.Fail }) }
        verify { shipperListObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Get shipper detail list success`() {
        coEvery { shippingEditorRepo.getShipperDetails() } returns GetShipperDetailResponse()
        shippingEditorViewModel.getShipperDetail()
        verify { shipperDetailsObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Get shipper detail list failed`() {
        coEvery { shippingEditorRepo.getShipperDetails() } throws defaultThrowable
        shippingEditorViewModel.getShipperDetail()
        verify { shipperDetailsObserver.onChanged(match { it is ShippingEditorState.Fail }) }
    }

    @Test
    fun `Validate shipping editor success`() {
        coEvery {
            shippingEditorRepo.validateShippingEditor(
                any(),
                any()
            )
        } returns ValidateShippingEditorResponse()
        shippingEditorViewModel.validateShippingEditor(1234, "1,2,3,4")
        verify { validateDataShipperObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Validate shipping editor failed`() {
        coEvery { shippingEditorRepo.validateShippingEditor(any(), any()) } throws defaultThrowable
        shippingEditorViewModel.validateShippingEditor(1234, "1,2,3,4")
        verify { validateDataShipperObserver.onChanged(match { it is ShippingEditorState.Fail }) }
    }

    @Test
    fun `Save shipping editor success`() {
        coEvery {
            shippingEditorRepo.saveShippingEditor(
                any(),
                any(),
                any()
            )
        } returns SaveShippingEditorResponse()
        shippingEditorViewModel.saveShippingData(1234, "1,2,3,4", "1,3,5")
        verify { saveShippingDataObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Save shipping editor failed`() {
        coEvery {
            shippingEditorRepo.saveShippingEditor(
                any(),
                any(),
                any()
            )
        } throws defaultThrowable
        shippingEditorViewModel.saveShippingData(1234, "1,2,3,4", "1,3,5")
        verify { saveShippingDataObserver.onChanged(match { it is ShippingEditorState.Fail }) }
    }

    @Test
    fun `WHEN shipper ticker state not found THEN shipper and all products should be available`() {
        val tickerModel = MultiLocShippingEditorDataProvider.provideShipperTickerState()
        val shipperModel = MultiLocShippingEditorDataProvider.provideShipperList()
        val mockShipperId = 1986L
        shipperModel.ongkirShippingEditor.data.shippers.conventional.first().shipperId = mockShipperId
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns shipperModel
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns tickerModel

        shippingEditorViewModel.getShipperList(12345)

        val result = (shippingEditorViewModel.shipperList.value as ShippingEditorState.Success).data
        val shipperResult = result.shippers.conventional.find { it.shipperId == mockShipperId }
        Assert.assertTrue(shipperResult!!.isAvailable)
        Assert.assertTrue(shipperResult.shipperProduct.all { it.isAvailable })
    }

    @Test
    fun `WHEN shipper ticker state is unavailable THEN shipper should be disabled and not active`() {
        val tickerModel = MultiLocShippingEditorDataProvider.provideShipperTickerState()
        val shipperModel = MultiLocShippingEditorDataProvider.provideShipperList()
        val unavailableShipperId = 1L
        val unavailableShipperTickerState =
            tickerModel.ongkirShippingEditorGetShipperTicker.data.courierTicker.find { courierTickerModel -> courierTickerModel.shipperId == unavailableShipperId }
        unavailableShipperTickerState?.tickerState = EditShippingConstant.TICKER_STATE_UNAVAILABLE
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns shipperModel
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns tickerModel

        shippingEditorViewModel.getShipperList(12345)

        val result = (shippingEditorViewModel.shipperList.value as ShippingEditorState.Success).data
        val shipperResult = result.shippers.conventional.find { it.shipperId == unavailableShipperId }
        Assert.assertFalse(shipperResult!!.isActive)
        Assert.assertFalse(shipperResult.isAvailable)
    }

    @Test
    fun `WHEN shipper product state not found THEN shipper product state should be from shipper state`() {
        val tickerModel = MultiLocShippingEditorDataProvider.provideShipperTickerState()
        val shipperModel = MultiLocShippingEditorDataProvider.provideShipperList()
        val unavailableShipperId = 1L
        val unavailableShipperTickerState =
            tickerModel.ongkirShippingEditorGetShipperTicker.data.courierTicker.find { courierTickerModel -> courierTickerModel.shipperId == unavailableShipperId }
        unavailableShipperTickerState?.tickerState = EditShippingConstant.TICKER_STATE_UNAVAILABLE
        unavailableShipperTickerState?.shipperProduct = listOf()
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns shipperModel
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns tickerModel

        shippingEditorViewModel.getShipperList(12345)

        val result = (shippingEditorViewModel.shipperList.value as ShippingEditorState.Success).data
        val shipperResult = result.shippers.conventional.find { it.shipperId == unavailableShipperId }?.shipperProduct
        Assert.assertFalse(shipperResult!!.all { it.isActive })
        Assert.assertFalse(shipperResult.all { it.isAvailable })
    }

    @Test
    fun `WHEN shipper ticker state is available but shipper product not available THEN shipper product state should be unabled`() {
        val tickerModel = MultiLocShippingEditorDataProvider.provideShipperTickerState()
        val shipperModel = MultiLocShippingEditorDataProvider.provideShipperList()
        val shipperId = 1L
        val unavailableShipperProductId = 6L
        val shipperTickerState =
            tickerModel.ongkirShippingEditorGetShipperTicker.data.courierTicker.find { courierTickerModel -> courierTickerModel.shipperId == shipperId }
        val unavailableShipperProductTicker = shipperTickerState?.shipperProduct?.find { it -> it.shipperProductId == unavailableShipperProductId }
        unavailableShipperProductTicker?.isAvailable = false
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns shipperModel
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns tickerModel

        shippingEditorViewModel.getShipperList(12345)

        val result = (shippingEditorViewModel.shipperList.value as ShippingEditorState.Success).data
        val shipperResult = result.shippers.conventional.find { it.shipperId == shipperId }
        val shipperProductResult = shipperResult?.shipperProduct?.find { it.shipperProductId == unavailableShipperProductId.toString() }
        Assert.assertTrue(shipperResult!!.isAvailable)
        Assert.assertFalse(shipperProductResult!!.isAvailable)
        Assert.assertFalse(shipperProductResult.isActive)
    }

    @Test
    fun `WHEN unavailable shipper product is the only one activated THEN shipper should be inactive`() {
        val tickerModel = MultiLocShippingEditorDataProvider.provideShipperTickerState()
        val shipperModel = MultiLocShippingEditorDataProvider.provideShipperList()
        val shipperId = 10L
        val unavailableShipperProductId = 20L
        val shipperData = shipperModel.ongkirShippingEditor.data.shippers.onDemand.find { it.shipperId == shipperId }
        shipperData?.isActive = true
        shipperData?.shipperProduct?.forEach { it.isActive = false }
        val unavailableShipperProduct = shipperData?.shipperProduct?.find { it.shipperProductId == unavailableShipperProductId.toString() }
        unavailableShipperProduct?.isActive = true
        val shipperTickerState =
            tickerModel.ongkirShippingEditorGetShipperTicker.data.courierTicker.find { courierTickerModel -> courierTickerModel.shipperId == shipperId }
        val unavailableShipperProductTicker = shipperTickerState?.shipperProduct?.find { it -> it.shipperProductId == unavailableShipperProductId }
        unavailableShipperProductTicker?.isAvailable = false
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns shipperModel
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns tickerModel

        shippingEditorViewModel.getShipperList(12345)

        val result = (shippingEditorViewModel.shipperList.value as ShippingEditorState.Success).data
        val shipperResult = result.shippers.onDemand.find { it.shipperId == shipperId }
        val shipperProductResult = shipperResult?.shipperProduct?.find { it.shipperProductId == unavailableShipperProductId.toString() }
        Assert.assertFalse(shipperResult!!.isActive)
        Assert.assertFalse(shipperProductResult!!.isAvailable)
        Assert.assertFalse(shipperProductResult.isActive)
    }

    @Test
    fun `WHEN theres unavailable activated shipper product and other available shipper product activated THEN shipper should be still active`() {
        val tickerModel = MultiLocShippingEditorDataProvider.provideShipperTickerState()
        val shipperModel = MultiLocShippingEditorDataProvider.provideShipperList()
        val shipperId = 10L
        val unavailableShipperProductId = 20L
        val shipperData = shipperModel.ongkirShippingEditor.data.shippers.onDemand.find { it.shipperId == shipperId }
        shipperData?.isActive = true
        shipperData?.shipperProduct?.forEach { it.isActive = true }
        val shipperTickerState =
            tickerModel.ongkirShippingEditorGetShipperTicker.data.courierTicker.find { courierTickerModel -> courierTickerModel.shipperId == shipperId }
        val unavailableShipperProductTicker = shipperTickerState?.shipperProduct?.find { it -> it.shipperProductId == unavailableShipperProductId }
        unavailableShipperProductTicker?.isAvailable = false
        coEvery { shippingEditorRepo.getShippingEditor(any()) } returns shipperModel
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } returns tickerModel

        shippingEditorViewModel.getShipperList(12345)

        val result = (shippingEditorViewModel.shipperList.value as ShippingEditorState.Success).data
        val shipperResult = result.shippers.onDemand.find { it.shipperId == shipperId }
        val shipperProductResult = shipperResult?.shipperProduct?.find { it.shipperProductId == unavailableShipperProductId.toString() }
        Assert.assertTrue(shipperResult!!.isActive)
        Assert.assertFalse(shipperProductResult!!.isAvailable)
        Assert.assertFalse(shipperProductResult.isActive)
    }
}
