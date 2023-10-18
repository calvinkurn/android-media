package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import com.tokopedia.home_component.visitable.BestSellerDataModel as BestSellerRevampDataModel

/**
 * Created by Lukas on 06/11/20.
 */
class HomeViewModelBestSellingWidgetTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase> (relaxed = true)
    private val getHomeRecommendationUseCase = mockk<HomeRecommendationUseCase> (relaxed = true)

    private val mockInitialBestSellerDataModel = BestSellerDataModel()
    private val mockInitialBestSellerRevampDataModel = BestSellerRevampDataModel(channelModel = ChannelModel("", ""))
    private val mockInitialBestSellerChipProductDataModel = BestSellerChipProductDataModel()
    private val mockSuccessBestSellerDataModel = BestSellerDataModel(
        recommendationItemList = listOf(
            RecommendationItem(),
            RecommendationItem()
        )
    )
    private val mockSuccessBestSellerRevampDataModel = BestSellerRevampDataModel(
        chipProductList = listOf(BestSellerChipProductDataModel(), BestSellerChipProductDataModel()),
        channelModel = ChannelModel("123", "234")
    )

    @ExperimentalCoroutinesApi
    private lateinit var homeViewModel: HomeRevampViewModel

    @ExperimentalCoroutinesApi
    @Test
    fun `when homeRecommendationUseCase success on onHomeBestSellerFilterClick then currentHomeDataModel should be updated with latest data`() = runBlocking {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(list = listOf(mockInitialBestSellerDataModel))
        )
        getHomeRecommendationUseCase.givenOnHomeBestSellerFilterClickReturn(
            mockSuccessBestSellerDataModel
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeRecommendationUseCase = getHomeRecommendationUseCase
        )
        homeViewModel.getRecommendationWidget(
            filterChip = RecommendationFilterChipsEntity.RecommendationFilterChip(),
            bestSellerDataModel = mockInitialBestSellerDataModel,
            selectedChipsPosition = 0
        )
        Assert.assertTrue(
            (homeViewModel.homeDataModel.list[0] as? BestSellerDataModel)?.recommendationItemList?.isNotEmpty() == true
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when homeRecommendationUseCase failed on onHomeBestSellerFilterClick then currentHomeDataModel should be updated with latest data`() = runBlocking {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(list = listOf(mockInitialBestSellerDataModel))
        )
        getHomeRecommendationUseCase.givenOnHomeBestSellerFilterClickReturn(
            mockInitialBestSellerDataModel
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeRecommendationUseCase = getHomeRecommendationUseCase
        )
        homeViewModel.getRecommendationWidget(
            filterChip = RecommendationFilterChipsEntity.RecommendationFilterChip(),
            bestSellerDataModel = mockInitialBestSellerDataModel,
            selectedChipsPosition = 0
        )
        Assert.assertTrue(
            (homeViewModel.homeDataModel.list[0] as? BestSellerDataModel)?.recommendationItemList?.isEmpty() == true
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when homeRecommendationUseCase success on onHomeBestSellerFilterClick then currentHomeDataModel should updated with latest data`() = runBlocking {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(list = listOf(mockInitialBestSellerRevampDataModel))
        )
        getHomeRecommendationUseCase.givenOnHomeBestSellerFilterClickReturn(
            mockSuccessBestSellerRevampDataModel
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeRecommendationUseCase = getHomeRecommendationUseCase
        )
        homeViewModel.getRecommendationWidget(
            selectedChipProduct = mockInitialBestSellerChipProductDataModel,
            currentDataModel = mockInitialBestSellerRevampDataModel
        )
        Assert.assertTrue(
            (homeViewModel.homeDataModel.list[0] as? BestSellerRevampDataModel)?.chipProductList?.isNotEmpty() == true
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `when homeRecommendationUseCase failed on onHomeBestSellerFilterClick then currentHomeDataModel should updated with latest data`() = runBlocking {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(list = listOf(mockInitialBestSellerRevampDataModel))
        )
        getHomeRecommendationUseCase.givenOnHomeBestSellerFilterClickReturn(
            mockInitialBestSellerRevampDataModel
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeRecommendationUseCase = getHomeRecommendationUseCase
        )
        homeViewModel.getRecommendationWidget(
            selectedChipProduct = mockInitialBestSellerChipProductDataModel,
            currentDataModel = mockInitialBestSellerRevampDataModel
        )
        Assert.assertTrue(
            (homeViewModel.homeDataModel.list[0] as? BestSellerRevampDataModel)?.chipProductList?.isEmpty() == true
        )
    }
}
