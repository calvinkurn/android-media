package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeSalamRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ReminderEnum
import com.tokopedia.home_component.model.ReminderWidget
import com.tokopedia.home_component.visitable.ReminderWidgetModel
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelSalamWidgetUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getHomeSalamRecommendationUseCase = mockk<HomeSalamRecommendationUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val mockId = "1"
    private val mockReminderModel = ReminderWidget()

    @ExperimentalCoroutinesApi
    @Test
    fun `When salam recommendation usecase success on declineRechargeRecommendationItem then homeDataModel should not contains reminderWidgetModel`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                ReminderWidgetModel(
                        id = mockId,
                        data = mockReminderModel,
                        source = ReminderEnum.SALAM
                )
        )))
        getHomeSalamRecommendationUseCase.givenOnDeclineSalamRecommendationSuccess()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeSalamRecommendationUseCase = getHomeSalamRecommendationUseCase
        )
        homeViewModel.declineSalamItem(mapOf())
        homeViewModel.homeDataModel.findWidget<ReminderWidgetModel>(
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
    fun `When salam recommendation usecase failed on declineRechargeRecommendationItem then homeDataModel should not contains reminderWidgetModel`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                ReminderWidgetModel(
                        id = mockId,
                        data = mockReminderModel,
                        source = ReminderEnum.SALAM
                )
        )))
        getHomeSalamRecommendationUseCase.givenOnDeclineSalamRecommendationError()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeSalamRecommendationUseCase = getHomeSalamRecommendationUseCase
        )
        homeViewModel.declineSalamItem(mapOf())
        homeViewModel.homeDataModel.findWidget<ReminderWidgetModel>(
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
    fun `When no salam recommendation on declineRechargeRecommendationItem then homeDataModel should not delete any model`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                ReminderWidgetModel(
                        id = mockId,
                        data = mockReminderModel,
                        source = ReminderEnum.RECHARGE
                )
        )))
        getHomeSalamRecommendationUseCase.givenOnDeclineSalamRecommendationSuccess()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeSalamRecommendationUseCase = getHomeSalamRecommendationUseCase
        )
        homeViewModel.declineSalamItem(mapOf())
        Assert.assertTrue(homeViewModel.homeDataModel.list.size == 1)
    }
}