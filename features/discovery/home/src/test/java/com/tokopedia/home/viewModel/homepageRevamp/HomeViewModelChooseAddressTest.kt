package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
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
    private lateinit var homeViewModel: HomeRevampViewModel

    val updatedState = true
    val mockLocalCacheModel = LocalCacheModel(
            address_id = "12345"
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
}