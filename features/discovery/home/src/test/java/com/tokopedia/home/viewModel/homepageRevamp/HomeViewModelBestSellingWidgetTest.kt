package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BestSellerChipProductDataModel
import com.tokopedia.home_component.visitable.BestSellerProductDataModel
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import io.mockk.coVerify
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
    private val mockInitialBestSellerRevampDataModel = BestSellerRevampDataModel(channelModel = ChannelModel("123", "234"))
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
    private val mockSuccessBestSellerChipProductDataModel = BestSellerChipProductDataModel(
        productModelList = listOf(
            BestSellerProductDataModel(
                ProductCardModel(), "", "", "", false, "",
                1L, 1, false, "", "", "", "", ""
            ),
            BestSellerProductDataModel(
                ProductCardModel(), "", "", "", false, "",
                1L, 1, false, "", "", "", "", ""
            )
        )
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

    @ExperimentalCoroutinesApi
    @Test
    fun `getRecommendationWidget should not update widget when selectedChipProduct is empty`() {
        // Arrange
        val viewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeRecommendationUseCase = getHomeRecommendationUseCase
        )
        val selectedChipProduct = BestSellerChipProductDataModel(productModelList = emptyList())
        val currentDataModel = mockInitialBestSellerRevampDataModel

        // Act
        viewModel.getRecommendationWidget(selectedChipProduct, mockInitialBestSellerRevampDataModel)

        // Assert
        coVerify {
            (viewModel).updateWidget(
                visitable = any(),
                visitableToChange = eq(mockSuccessBestSellerRevampDataModel),
                position = any()
            )
        }
    }

//    @ExperimentalCoroutinesApi
//    @Test
//    fun `getRecommendationWidget should not update widget when selectedChipProduct is not empty`() {
//        // Arrange
//        val viewModel = createHomeViewModel(
//            getHomeUseCase = getHomeUseCase,
//            homeRecommendationUseCase = getHomeRecommendationUseCase
//        )
//        val selectedChipProduct = mockSuccessBestSellerChipProductDataModel
//        val currentDataModel = mockSuccessBestSellerRevampDataModel
//        val scrollDirection = CarouselPagingGroupChangeDirection.NO_DIRECTION
//
//        // Act
//        viewModel.getRecommendationWidget(selectedChipProduct, currentDataModel, scrollDirection)
//
//        // Assert
//        coVerify(inverse = false) {
//            viewModel.updateWidget(
//                visitable = getHomeRecommendationUseCase.onHomeBestSellerFilterClick(
//                    currentBestSellerDataModel = currentDataModel,
//                    selectedFilterChip = selectedChipProduct.chip,
//                    scrollDirection = scrollDirection,
//                ),
//                visitableToChange = eq(currentDataModel),
//                position = any()
//            )
//        }
//
//    }
}
