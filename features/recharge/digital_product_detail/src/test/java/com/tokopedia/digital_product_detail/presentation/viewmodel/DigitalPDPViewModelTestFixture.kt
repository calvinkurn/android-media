package com.tokopedia.digital_product_detail.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberData
import com.tokopedia.common.topupbills.favorite.data.TopupBillsPersoFavNumberItem
import com.tokopedia.digital_product_detail.domain.repository.DigitalPDPTelcoRepository
import com.tokopedia.recharge_component.model.denom.DenomMCCMModel
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert
import org.junit.Before
import org.junit.Rule

abstract class DigitalPDPViewModelTestFixture {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    protected lateinit var viewModel: DigitalPDPPulsaViewModel

    @RelaxedMockK
    lateinit var repo: DigitalPDPTelcoRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel = DigitalPDPPulsaViewModel(repo, CoroutineTestDispatchersProvider)
    }

    protected fun onGetFavoriteNumber_thenReturn(response: TopupBillsPersoFavNumberData) {
        coEvery {
            repo.getFavoriteNumber(any())
        } returns response
    }

    protected fun onGetPrefixOperator_thenReturn(response: TelcoCatalogPrefixSelect) {
        coEvery {
            repo.getOperatorList(any())
        } returns response
    }

    protected fun onGetCatalogInputMultitab_thenReturn(response: DenomMCCMModel) {
        coEvery {
            repo.getProductInputMultiTabDenomGrid(any(), any(), any())
        } returns response
    }

    protected fun onGetCatalogInputMultitab_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.getProductInputMultiTabDenomGrid(any(), any(), any())
        } throws errorThrowable
    }

    protected fun onGetAddToCart_thenReturn(response: String) {
        coEvery {
            repo.addToCart(any(), any(), any(), any())
        } returns response
    }

    protected fun onGetAddToCart_thenReturn(errorThrowable: Throwable) {
        coEvery {
            repo.addToCart(any(), any(), any(), any())
        } throws errorThrowable
    }

    protected fun verifyGetFavoriteNumberSuccess(expectedResponse: List<TopupBillsPersoFavNumberItem>) {
        val actualResponse = viewModel.favoriteNumberData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetPrefixOperatorSuccess(expectedResponse: TelcoCatalogPrefixSelect) {
        val actualResponse = viewModel.catalogPrefixSelect.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetCatalogInputMultitabSuccess(expectedResponse: DenomMCCMModel) {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyGetCatalogInputMultitabLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }

    protected fun verifyGetCatalogInputMultitabError(expectedResponse: Throwable) {
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Fail).error)
    }

    protected fun verifyGetCatalogInputMultitabErrorCancellation(){
        val actualResponse = viewModel.observableDenomMCCMData.value
        Assert.assertNull(actualResponse)
    }

    protected fun verifyAddToCartSuccess(expectedResponse: String) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Success).data)
    }

    protected fun verifyAddToCartError(expectedResponse: Throwable) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse.message, (actualResponse as RechargeNetworkResult.Fail).error.message)
    }

    protected fun verifyAddToCartErrorExceptions(expectedResponse: Throwable) {
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, (actualResponse as RechargeNetworkResult.Fail).error)
    }

    protected fun verifyAddToCartErrorLoading(expectedResponse: RechargeNetworkResult.Loading){
        val actualResponse = viewModel.addToCartResult.value
        Assert.assertEquals(expectedResponse, actualResponse)
    }
}