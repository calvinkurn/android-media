package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeListCarouselUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 07/11/20.
 */
class HomeViewModelListCarouselTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getListCarouselUseCase = mockk<HomeListCarouselUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val mockChannelGrid = ChannelGrid()
    private val mockChannel = ChannelModel(id = "1", groupId = "1", channelGrids = listOf(mockChannelGrid))
    private val mockChannelId = 1

    private val mockKey = "1"
    private val mockValue = "2"
    @ExperimentalCoroutinesApi
    @Test
    fun `When one click checkout usecase success on onOneClickCheckout then one click checkout data should triggered with response data`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel())
        getListCarouselUseCase.givenOnOneClickCheckoutReturn(mapOf(mockKey to mockValue))
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeListCarouselUseCase = getListCarouselUseCase
        )
        homeViewModel.getOneClickCheckoutHomeComponent(
                mockChannel, mockChannelGrid, mockChannelId
        )
        homeViewModel.oneClickCheckoutHomeComponent.observeOnce {
            Assert.assertTrue((it.getContentIfNotHandled() as? Map<String, String>)?.get(mockKey) == mockValue)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When one click checkout usecase failed on onOneClickCheckout then one click checkout data should triggered with exception`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel())
        getListCarouselUseCase.givenOnOneClickCheckoutError()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeListCarouselUseCase = getListCarouselUseCase
        )
        homeViewModel.getOneClickCheckoutHomeComponent(
                mockChannel, mockChannelGrid, mockChannelId
        )
        homeViewModel.oneClickCheckoutHomeComponent.observeOnce {
            Assert.assertTrue((it.getContentIfNotHandled() is Exception))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When close list carousel usecase success on onClickCloseListCarousel then homeDataModel should not contains RecommendationListCarousel`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(
                        RecommendationListCarouselDataModel(channelModel = mockChannel)
                ))
        )
        getListCarouselUseCase.givenOnClickCloseListCarouselReturn(true)
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeListCarouselUseCase = getListCarouselUseCase
        )
        homeViewModel.onCloseBuyAgain(mockChannelId.toString(), 0)
        homeViewModel.homeDataModel.findWidget<RecommendationListCarouselDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(false)
                },
                actionOnNotFound = {
                    Assert.assertTrue(true)
                }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When close list carousel usecase failed on onClickCloseListCarousel then homeDataModel still contains RecommendationListCarousel`(){
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(list = listOf(
                        RecommendationListCarouselDataModel(channelModel = mockChannel)
                ))
        )
        getListCarouselUseCase.givenOnClickCloseListCarouselReturn(false)
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeListCarouselUseCase = getListCarouselUseCase
        )
        homeViewModel.onCloseBuyAgain(mockChannelId.toString(), 0)
        homeViewModel.homeDataModel.findWidget<RecommendationListCarouselDataModel>(
                actionOnFound = { model, index ->
                    Assert.assertTrue(true)
                },
                actionOnNotFound = {
                    Assert.assertTrue(false)
                }
        )
    }
}