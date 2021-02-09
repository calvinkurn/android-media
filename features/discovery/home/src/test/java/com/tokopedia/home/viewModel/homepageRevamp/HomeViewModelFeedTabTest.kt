package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetRecommendationTabUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.RecommendationTabDataModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

class HomeViewModelFeedTabTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRecommendationTabUseCase: GetRecommendationTabUseCase = mockk(relaxed = true)
    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    @Test
    fun `Test get feed tab success`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )
        coEvery { getRecommendationTabUseCase.executeOnBackground() } returns listOf(
                RecommendationTabDataModel(id="1", imageUrl = "", position = 1, name = "")
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationTabUseCase = getRecommendationTabUseCase)
        homeViewModel.getFeedTabData()
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == HomeRecommendationFeedDataModel::class.java } != null)
        }
    }

    @Test
    fun `Test get recommendation feed section`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.size -1 == homeViewModel.getRecommendationFeedSectionPosition())
        }
    }

    @Test
    fun `Test tab is available and send retry model`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        isCache = true,
                        list = listOf(HomeRecommendationFeedDataModel())
                )
        )
        coEvery { getRecommendationTabUseCase.executeOnBackground() } throws TimeoutException()

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationTabUseCase = getRecommendationTabUseCase)
        homeViewModel.getFeedTabData()
        homeViewModel.homeLiveData.observeOnce { homeDataModel ->
            assert(homeDataModel.list.find{ it::class.java == HomeRetryModel::class.java } != null)
        }
    }
}