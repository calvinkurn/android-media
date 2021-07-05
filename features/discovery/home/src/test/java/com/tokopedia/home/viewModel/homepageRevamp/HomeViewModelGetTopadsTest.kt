package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsBannerDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test

class HomeViewModelGetTopadsTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private val topadsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    @Test
    fun `Test get topads image success`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        isCache = false,
                        isProcessingAtf = false,
                        list = listOf(HomeTopAdsBannerDataModel(channel = DynamicHomeChannel.Channels(), topAdsImageViewModel = null))
                )
        )
        coEvery { topadsImageViewUseCase.getImageData(any()) } returns arrayListOf(
                TopAdsImageViewModel()
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, topadsImageViewUseCase = topadsImageViewUseCase)

        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find{ it::class.java == HomeTopAdsBannerDataModel::class.java } as? HomeTopAdsBannerDataModel)?.topAdsImageViewModel != null)
        }
    }

    @Test
    fun `Test get topads image empty`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        isCache = false,
                        list = listOf(HomeTopAdsBannerDataModel(channel = DynamicHomeChannel.Channels(), topAdsImageViewModel = null))
                )
        )
        coEvery { topadsImageViewUseCase.getImageData(any()) } returns arrayListOf()

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, topadsImageViewUseCase = topadsImageViewUseCase)

        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert((homeDataModel.list.find{ it::class.java == HomeTopAdsBannerDataModel::class.java } as? HomeTopAdsBannerDataModel)?.topAdsImageViewModel == null)
        }
    }
}