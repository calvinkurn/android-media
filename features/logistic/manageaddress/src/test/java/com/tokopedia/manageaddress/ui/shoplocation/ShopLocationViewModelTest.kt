package com.tokopedia.manageaddress.ui.shoplocation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.logisticCommon.data.entity.shoplocation.ShopLocationModel
import com.tokopedia.logisticCommon.data.repository.ShopLocationRepository
import com.tokopedia.logisticCommon.data.response.shoplocation.*
import com.tokopedia.manageaddress.domain.mapper.ShopLocationMapper
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationState
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
class ShopLocationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repo: ShopLocationRepository = mockk(relaxed = true)
    private val mapper: ShopLocationMapper = ShopLocationMapper()

    private val shopLocationObserver: Observer<ShopLocationState<ShopLocationModel>> = mockk(relaxed = true)
    private val resultObserver: Observer<ShopLocationState<ShopLocationSetStatusResponse>> = mockk(relaxed = true)
    private val shopWhitelistObserver: Observer<ShopLocationState<ShopLocWhitelist>> = mockk(relaxed = true)

    private lateinit var shopLocationViewModel: ShopLocationViewModel

    private val defaultThrowable = Throwable("test error")

    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        shopLocationViewModel = ShopLocationViewModel(repo, mapper)
        shopLocationViewModel.shopLocation.observeForever(shopLocationObserver)
        shopLocationViewModel.result.observeForever(resultObserver)
        shopLocationViewModel.shopWhitelist.observeForever(shopWhitelistObserver)
    }

    @Test
    fun `Get Shop Location Warehouse success`() {
        coEvery { repo.getShopLocation(any()) } returns GetShopLocationResponse()
        shopLocationViewModel.getShopLocationList(123)
        verify { shopLocationObserver.onChanged(match { it is ShopLocationState.Success}) }
    }

    @Test
    fun `Get Shop Location Warehouse failed`() {
        coEvery { repo.getShopLocation(any()) } throws defaultThrowable
        shopLocationViewModel.getShopLocationList(123)
        verify { shopLocationObserver.onChanged(match { it is ShopLocationState.Fail }) }
    }

    @Test
    fun `Set Shop Location Status success`() {
        coEvery { repo.setShopLocationStatus(any(), any()) } returns SetShopLocationStatusResponse()
        shopLocationViewModel.setShopLocationState(1234, 1)
        verify { resultObserver.onChanged(match { it is ShopLocationState.Success }) }
    }

    @Test
    fun `Set Shop Location Status failed`() {
        coEvery { repo.setShopLocationStatus(any(), any()) } throws defaultThrowable
        shopLocationViewModel.setShopLocationState(123, 1)
        verify { resultObserver.onChanged(match { it is ShopLocationState.Fail }) }
    }

    @Test
    fun `Get Whitelisted User success`() {
        coEvery { repo.getShopLocationWhitelist(any()) } returns ShopLocationWhitelistResponse()
        shopLocationViewModel.getWhitelistData(123)
        verify { shopWhitelistObserver.onChanged(match { it is ShopLocationState.Success }) }
    }

    @Test
    fun `Get Whitelisted User failed`() {
        coEvery { repo.getShopLocationWhitelist(any()) } throws defaultThrowable
        shopLocationViewModel.getWhitelistData(123)
        verify { shopWhitelistObserver.onChanged(match { it is ShopLocationState.Fail }) }
    }


}