package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.usecase.RefreshTokonowDataUsecase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelChooseAddressTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
    private val refreshTokonowDataUsecase = mockk<RefreshTokonowDataUsecase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    val updatedState = true
    val mockLocalCacheModel = LocalCacheModel(
            address_id = "12345",
            warehouse_id = "0"
    )
    val mockHomeChooseAddressData = HomeChooseAddressData(isActive = updatedState, localCacheModel = mockLocalCacheModel)
    @Test
    fun `When choose address state updated on updateChooseAddressData then homeDataModel choose address value should be updated`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(HomeHeaderDataModel(
                needToShowChooseAddress = false
        ))))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateChooseAddressData(mockHomeChooseAddressData)
        Assert.assertTrue(homeViewModel.homeDataModel.homeChooseAddressData == mockHomeChooseAddressData)
        Assert.assertTrue(
                (homeViewModel.homeDataModel.list[0] as? HomeHeaderDataModel)?.needToShowChooseAddress == updatedState
        )
    }

    @Test
    fun `When choose address state updated on updateChooseAddressData then get address data should return latest data`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(HomeHeaderDataModel(
                needToShowChooseAddress = false
        ))))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateChooseAddressData(mockHomeChooseAddressData)
        Assert.assertTrue(homeViewModel.getAddressData() == mockHomeChooseAddressData)
        Assert.assertTrue(
                (homeViewModel.homeDataModel.list[0] as? HomeHeaderDataModel)?.needToShowChooseAddress == updatedState
        )
    }

    @Test
    fun `When choose address removed on removeChooseAddressWidget then address data state should be false`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(HomeHeaderDataModel(
                needToShowChooseAddress = true
        ))))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.removeChooseAddressWidget()
        Assert.assertTrue(!homeViewModel.homeDataModel.homeChooseAddressData.isActive)
        Assert.assertTrue(
                (homeViewModel.homeDataModel.list[0] as? HomeHeaderDataModel)?.needToShowChooseAddress == false
        )
    }

    @Test
    fun `When tokonow data in LCA updated from refresh tokonow success then tokonow value in LCA should be updated`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(HomeHeaderDataModel(
            needToShowChooseAddress = false
        ))))
        val lcaWithNewTokonowData = mockLocalCacheModel.copy(
            warehouse_id = "111",
            shop_id = "11",
            service_type = "2h",
            tokonow_last_update = "2021-07-16T05:51:54+07:00"
        ).toRefreshTokonowDataResponse()
        homeViewModel = createHomeViewModel(refreshTokonowDataUseCase = refreshTokonowDataUsecase)
        refreshTokonowDataUsecase.givenCurrentLcaDataReturn(mockLocalCacheModel, lcaWithNewTokonowData)
        homeViewModel.refreshTokonowWarehouse(mockLocalCacheModel)
        Assert.assertTrue(homeViewModel.tokonowData.value?.data == lcaWithNewTokonowData)
    }

    @Test
    fun `When tokonow data in LCA updated from refresh tokonow error then catch exception`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(HomeHeaderDataModel(
            needToShowChooseAddress = false
        ))))
        val lcaWithNewTokonowData = mockLocalCacheModel.copy(
            warehouse_id = "111",
            shop_id = "11",
            service_type = "2h",
            tokonow_last_update = "2021-07-16T05:51:54+07:00"
        ).toRefreshTokonowDataResponse()
        homeViewModel = createHomeViewModel(refreshTokonowDataUseCase = refreshTokonowDataUsecase)
        refreshTokonowDataUsecase.givenCurrentLcaDataThrows(mockLocalCacheModel, lcaWithNewTokonowData)
        homeViewModel.refreshTokonowWarehouse(mockLocalCacheModel)
        Assert.assertTrue(homeViewModel.tokonowData.value?.status == Result.Status.ERROR)
    }
}