package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeRechargeRecommendationUseCase
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

class HomeViewModelRechargeRecommendationUnitTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getHomeRechargeUseCase = mockk<HomeRechargeRecommendationUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    private val mockId = "1"
    private val mockReminderModel = ReminderWidget()

    @ExperimentalCoroutinesApi
    @Test
    fun `When recharge recommendation usecase success on declineRechargeRecommendationItem then homeDataModel should not contains reminderWidgetModel`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                ReminderWidgetModel(
                        id = mockId,
                        data = mockReminderModel,
                        source = ReminderEnum.RECHARGE
                )
        )))
        getHomeRechargeUseCase.givenOnDeclineRechargeRecommendationSuccess()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeRechargeRecommendationUseCase = getHomeRechargeUseCase
        )
        homeViewModel.declineRechargeRecommendationItem(mapOf())
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
    fun `When recharge recommendation usecase failed on declineRechargeRecommendationItem then homeDataModel should not contains reminderWidgetModel`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                ReminderWidgetModel(
                        id = mockId,
                        data = mockReminderModel,
                        source = ReminderEnum.RECHARGE
                )
        )))
        getHomeRechargeUseCase.givenOnDeclineRechargeRecommendationError()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeRechargeRecommendationUseCase = getHomeRechargeUseCase
        )
        homeViewModel.declineRechargeRecommendationItem(mapOf())
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
    fun `When no recharge recommendation on declineRechargeRecommendationItem then homeDataModel should not delete any model`(){
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel(list = listOf(
                ReminderWidgetModel(
                        id = mockId,
                        data = mockReminderModel,
                        source = ReminderEnum.SALAM
                )
        )))
        getHomeRechargeUseCase.givenOnDeclineRechargeRecommendationSuccess()
        homeViewModel = createHomeViewModel(
                getHomeUseCase = getHomeUseCase,
                homeRechargeRecommendationUseCase = getHomeRechargeUseCase
        )
        homeViewModel.declineRechargeRecommendationItem(mapOf())
        Assert.assertTrue(homeViewModel.homeDataModel.list.size == 1)
    }
}

