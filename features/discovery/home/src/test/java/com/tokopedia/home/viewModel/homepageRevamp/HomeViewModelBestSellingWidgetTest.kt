package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationFilterChips
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

/**
 * Created by Lukas on 06/11/20.
 */
class HomeViewModelBestSellingWidgetTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase> (relaxed = true)
    private val getRecommendationUsecase = mockk<GetRecommendationUseCase> (relaxed = true)
    private val getRecommendationFilterChips = mockk<GetRecommendationFilterChips> (relaxed = true)
    private val bestSellerMapper = mockk<BestSellerMapper> (relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `success get best selling widget from recommendation data and chip data`() = runBlocking{
        val recomItem = RecommendationItem()
        val bestSellerDataModel = BestSellerDataModel(
                recommendationItemList = listOf(recomItem)
        )
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel
        coEvery { getRecommendationUsecase.getData(any()) } returns listOf(RecommendationWidget(recommendationItemList = listOf(RecommendationItem())))
        coEvery { getRecommendationFilterChips.executeOnBackground().filterChip } returns listOf()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bestSellerDataModel)
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationUseCase = getRecommendationUsecase, getRecommendationFilterChips = getRecommendationFilterChips, bestSellerMapper = bestSellerMapper)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } != null )
        assert( (homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } as? BestSellerDataModel)?.recommendationItemList?.isNotEmpty() == true)
    }

    @Test
    fun `error get best selling widget from recommendation data and chip data`() = runBlocking{
        val recomItem = RecommendationItem()
        val bestSellerDataModel = BestSellerDataModel(
                recommendationItemList = listOf(recomItem)
        )
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel
        coEvery { getRecommendationUsecase.getData(any()) } throws TimeoutException()
        coEvery { getRecommendationFilterChips.executeOnBackground().filterChip } returns listOf()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bestSellerDataModel),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationUseCase = getRecommendationUsecase, getRecommendationFilterChips = getRecommendationFilterChips, bestSellerMapper = bestSellerMapper)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } == null )
    }

    @Test
    fun `success get best selling widget but empty from recom and filter chip`() = runBlocking{
        val recomItem = RecommendationItem()
        val bestSellerDataModel = BestSellerDataModel(
                recommendationItemList = listOf(recomItem)
        )
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel
        coEvery { getRecommendationUsecase.getData(any()) } returns listOf()
        coEvery { getRecommendationFilterChips.executeOnBackground().filterChip } returns listOf()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bestSellerDataModel),
                        isProcessingAtf = false
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationUseCase = getRecommendationUsecase, getRecommendationFilterChips = getRecommendationFilterChips, bestSellerMapper = bestSellerMapper)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } == null )
    }

    @Test
    fun `success get recommendation filtering best selling widget`() = runBlocking{
        val recomItem = RecommendationItem()
        val recomFilterChip = RecommendationFilterChipsEntity.RecommendationFilterChip()
        val bestSellerDataModel = BestSellerDataModel(
                recommendationItemList = listOf(recomItem)
        )
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel
        coEvery { getRecommendationUsecase.getData(any()) } returns listOf(RecommendationWidget(recommendationItemList = listOf(RecommendationItem())))
        coEvery { getRecommendationFilterChips.executeOnBackground().filterChip } returns listOf()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(bestSellerDataModel)
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationUseCase = getRecommendationUsecase, getRecommendationFilterChips = getRecommendationFilterChips, bestSellerMapper = bestSellerMapper)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } != null )
        assert( (homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } as? BestSellerDataModel)?.recommendationItemList?.isNotEmpty() == true)

        homeViewModel.getRecommendationWidget(recomFilterChip, bestSellerDataModel, 1)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } != null )
        assert( (homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } as? BestSellerDataModel)?.recommendationItemList?.isNotEmpty() == true)
    }

    @Test
    fun `no slotting data best selling widget`() = runBlocking{
        val recomItem = RecommendationItem()
        val bestSellerDataModel = BestSellerDataModel(
                recommendationItemList = listOf(recomItem)
        )
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel
        coEvery { getRecommendationUsecase.getData(any()) } returns listOf(RecommendationWidget(recommendationItemList = listOf(RecommendationItem())))
        coEvery { getRecommendationFilterChips.executeOnBackground().filterChip } returns listOf()

        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationUseCase = getRecommendationUsecase, getRecommendationFilterChips = getRecommendationFilterChips, bestSellerMapper = bestSellerMapper)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } == null )
    }

    @Test
    fun `null data best selling widget`() = runBlocking{
        val recomItem = RecommendationItem()
        val bestSellerDataModel = BestSellerDataModel(
                recommendationItemList = listOf(recomItem)
        )
        coEvery { bestSellerMapper.mappingRecommendationWidget(any()) } returns bestSellerDataModel
        coEvery { getRecommendationUsecase.getData(any()) } returns listOf(RecommendationWidget(recommendationItemList = listOf(RecommendationItem())))
        coEvery { getRecommendationFilterChips.executeOnBackground().filterChip } returns listOf()

        coEvery { getHomeUseCase.getHomeData() } returns flow{
            emit(null)
        }

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getRecommendationUseCase = getRecommendationUsecase, getRecommendationFilterChips = getRecommendationFilterChips, bestSellerMapper = bestSellerMapper)

        assert( homeViewModel.homeLiveData.value?.list?.find { it is BestSellerDataModel } == null )
    }
}