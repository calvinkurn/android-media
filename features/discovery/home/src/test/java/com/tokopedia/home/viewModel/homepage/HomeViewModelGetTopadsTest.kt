package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRecommendationTabUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeTopAdsBannerDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

class HomeViewModelGetTopadsTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeUseCase> (relaxed = true)
    private val topadsImageViewUseCase: TopAdsImageViewUseCase = mockk(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel
    @Test
    fun `Test get topads image success`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        isCache = false,
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