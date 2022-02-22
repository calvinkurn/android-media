package com.tokopedia.digital_product_detail.presentation.viewmodel

import com.tokopedia.digital_product_detail.data.mapper.DigitalDenomMapper
import com.tokopedia.digital_product_detail.presentation.data.DataPlanDataFactory
import com.tokopedia.digital_product_detail.presentation.data.PulsaDataFactory
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
class DigitalPDPDataPlanViewModelTest: DigitalPDPDataPlanViewModelTestFixture() {

    private val dataFactory = DataPlanDataFactory()
    private val mapperFactory = DigitalDenomMapper()

    @Test
    fun `when getting catalogInputMultitab should run and give success result and updated data filter`() = testCoroutineRule.runBlockingTest {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = true
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        val filterResponse = mappedResponse.filterTagComponents
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoGetCalled()
        verifyGetCatalogInputMultitabSuccess(mappedResponse)
        verifyGetFilterTagComponentSuccess(filterResponse)
    }

    @Test
    fun `when getting catalogInputMultitab should run and give success result and empty data filter`() = testCoroutineRule.runBlockingTest {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = false
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        onGetCatalogInputMultitabisFiltered_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "", isRefreshedFilter)
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoIsRefreshedGetCalled()
        verifyGetCatalogInputMultitabSuccess(mappedResponse)
        verifyGetFilterTagComponentEmpty()
    }

    @Test
    fun `given catalogInputMultitab loading state then should get loading state`() {
        val loadingResponse = RechargeNetworkResult.Loading

        viewModel.setRechargeCatalogInputMultiTabLoading()
        verifyGetCatalogInputMultitabLoading(loadingResponse)
    }

    @Test
    fun `when getting catalogInputMultitab should run and give error result`() = testCoroutineRule.runBlockingTest {
        val errorResponse = MessageErrorException("")
        onGetCatalogInputMultitab_thenReturn(errorResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        skipMultitabDelay()
        verifyGetProductInputMultiTabRepoGetCalled()
        verifyGetCatalogInputMultitabError(errorResponse)
    }

    @Test
    fun `given CancellationException to catalogInputMultitab and should return empty result`() {
        val errorResponse = CancellationException()
        onGetCatalogInputMultitab_thenReturn(errorResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        viewModel.cancelCatalogProductJob()
        verifyGetProductInputMultiTabRepoWasNotCalled()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

    @Test
    fun `when cancelCatalogProductJob called the job should be cancelled and live data should not emit value`() {
        val response = dataFactory.getCatalogInputMultiTabData()
        val isRefreshedFilter = true
        val mappedResponse = mapperFactory.mapMultiTabFullDenom(response, isRefreshedFilter)
        onGetCatalogInputMultitab_thenReturn(mappedResponse)

        viewModel.getRechargeCatalogInputMultiTab(MENU_ID, "", "")
        viewModel.cancelCatalogProductJob()
        verifyCatalogProductJobIsCancelled()
        verifyGetProductInputMultiTabRepoWasNotCalled()
        verifyGetCatalogInputMultitabErrorCancellation()
    }

    @Test
    fun `given filterData with some isSelected and should updated filterDataParams` () {
        val initialFilter = dataFactory.getCatalogInputMultiTabData().multitabData.productInputs.first().filterTagComponents
        viewModel.updateFilterData(initialFilter)
        verifyGetFilterTagComponentSuccess(initialFilter)
        verifyGetFilterParamEmpty(dataFactory.getFilterParamsEmpty())

        val selectedFilter = dataFactory.getFilterTagListSelectedData()
        viewModel.updateFilterData(selectedFilter)
        verifyGetFilterTagComponentSuccess(selectedFilter)
        verifyGetFilterParam(dataFactory.getFilterParams())
    }

    companion object {
        const val MENU_ID = 290
    }
}