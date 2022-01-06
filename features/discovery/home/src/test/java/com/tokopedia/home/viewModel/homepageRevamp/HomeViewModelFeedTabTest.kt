package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.viewmodel.HomeRecommendationFeedDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.mockk
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class HomeViewModelFeedTabTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    @Test
    fun `Test tab available and value does not change after feed tab data again`(){
        val homeRecommendationFeedDataModel = HomeRecommendationFeedDataModel()
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(homeRecommendationFeedDataModel)
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.getFeedTabData()
        homeViewModel.homeDataModel.findWidget<HomeRecommendationFeedDataModel>(
            actionOnFound = { widget, _ ->
                Assert.assertEquals(widget, homeRecommendationFeedDataModel)
            },
            actionOnNotFound = {}
        )
    }
}