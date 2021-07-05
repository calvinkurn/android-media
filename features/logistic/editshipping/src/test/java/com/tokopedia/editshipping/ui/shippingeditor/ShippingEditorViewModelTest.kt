package com.tokopedia.editshipping.ui.shippingeditor

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.editshipping.data.repository.ShippingEditorRepository
import com.tokopedia.editshipping.domain.mapper.ShipperDetailMapper
import com.tokopedia.editshipping.domain.mapper.ShippingEditorMapper
import com.tokopedia.editshipping.domain.mapper.ValidateShippingNewMapper
import com.tokopedia.editshipping.domain.model.shippingEditor.*
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shippingeditor.*
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocWhitelist
import com.tokopedia.logisticCommon.data.response.shoplocation.ShopLocationWhitelistResponse
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
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

    private val shopWhitelistObserver: Observer<ShippingEditorState<ShopLocWhitelist>> = mockk(relaxed = true)
    private val shipperListObserver: Observer<ShippingEditorState<ShipperListModel>> = mockk(relaxed = true)
    private val shipperTickerListObserver: Observer<ShippingEditorState<ShipperTickerModel>> = mockk(relaxed = true)
    private val shipperDetailsObserver: Observer<ShippingEditorState<ShipperDetailModel>> = mockk(relaxed = true)
    private val validateDataShipperObserver: Observer<ShippingEditorState<ValidateShippingEditorModel>> = mockk(relaxed = true)
    private val saveShippingDataObserver: Observer<ShippingEditorState<SaveShippingResponse>> = mockk(relaxed = true)

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setup() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        shippingEditorViewModel = ShippingEditorViewModel(shopRepo, shippingEditorRepo, shippingEditorMapper, validateShippingMapper, detailMapper)
        shippingEditorViewModel.shopWhitelist.observeForever(shopWhitelistObserver)
        shippingEditorViewModel.shipperList.observeForever(shipperListObserver)
        shippingEditorViewModel.shipperTickerList.observeForever(shipperTickerListObserver)
        shippingEditorViewModel.shipperDetail.observeForever(shipperDetailsObserver)
        shippingEditorViewModel.validateDataShipper.observeForever(validateDataShipperObserver)
        shippingEditorViewModel.saveShippingData.observeForever(saveShippingDataObserver)
    }

    @Test
    fun `Get shop location whitelist success`() {
        coEvery { shopRepo.getShopLocationWhitelist(any()) } returns ShopLocationWhitelistResponse()
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
        shippingEditorViewModel.getShipperTickerList(1234)
        verify { shipperTickerListObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Get shipper ticker list failed`() {
        coEvery { shippingEditorRepo.getShippingEditorShipperTicker(any()) } throws defaultThrowable
        shippingEditorViewModel.getShipperTickerList(1234)
        verify { shipperTickerListObserver.onChanged(match { it is ShippingEditorState.Fail }) }
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
        coEvery { shippingEditorRepo.validateShippingEditor(any(), any()) } returns ValidateShippingEditorResponse()
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
        coEvery { shippingEditorRepo.saveShippingEditor(any(), any(), any()) } returns SaveShippingEditorResponse()
        shippingEditorViewModel.saveShippingData(1234, "1,2,3,4", "1,3,5")
        verify { saveShippingDataObserver.onChanged(match { it is ShippingEditorState.Success }) }
    }

    @Test
    fun `Save shipping editor failed`() {
        coEvery { shippingEditorRepo.saveShippingEditor(any(), any(), any()) } throws defaultThrowable
        shippingEditorViewModel.saveShippingData(1234, "1,2,3,4", "1,3,5")
        verify { saveShippingDataObserver.onChanged(match { it is ShippingEditorState.Fail }) }
    }
}