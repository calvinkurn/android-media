package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.model.HomeChooseAddressData
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderOvoDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelChooseAddressTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `When choose address rollence is active then list should contains choose address widget`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDataModel(list = listOf(HomeHeaderOvoDataModel(
                needToShowChooseAddress = false
        ))))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateChooseAddressData(HomeChooseAddressData(isActive = true))
        homeViewModel.refreshHomeData()

        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            val homeHeaderOvoDataModel = (homeDataModel.list.find{ it::class.java == HomeHeaderOvoDataModel::class.java } as? HomeHeaderOvoDataModel)
            assert(homeHeaderOvoDataModel?.needToShowChooseAddress == true)
        }
    }

    @Test
    fun `When choose address rollence is inactive then list should not contains choose address widget`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDataModel(list = listOf(HomeHeaderOvoDataModel(
                needToShowChooseAddress = true
        ))))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateChooseAddressData(HomeChooseAddressData(isActive = false))
        homeViewModel.refreshHomeData()

        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            val homeHeaderOvoDataModel = (homeDataModel.list.find{ it::class.java == HomeHeaderOvoDataModel::class.java } as? HomeHeaderOvoDataModel)
            assert(homeHeaderOvoDataModel?.needToShowChooseAddress == false)
        }
    }
}