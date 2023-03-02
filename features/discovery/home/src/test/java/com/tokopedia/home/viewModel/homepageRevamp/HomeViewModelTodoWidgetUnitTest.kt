package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeTodoWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.usecase.todowidget.DismissTodoWidgetUseCase
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by frenzel
 */
@FlowPreview
@ExperimentalCoroutinesApi
class HomeViewModelTodoWidgetUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val dismissTodoWidgetUseCase = mockk<DismissTodoWidgetUseCase>(relaxed = true)
    private val homeTodoWidgetUseCase = mockk<HomeTodoWidgetUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val mockChannelModel = ChannelModel(id = "1", groupId = "2")
    private val mockFailedTodoWidget = TodoWidgetListDataModel(
        channelModel = mockChannelModel,
        status = TodoWidgetListDataModel.STATUS_ERROR
    )
    private val todoWidgetItem1 = TodoWidgetDataModel(id = 1)
    private val todoWidgetItem2 = TodoWidgetDataModel(id = 2)
    private val mockSuccessTodoWidget = TodoWidgetListDataModel(
        channelModel = mockChannelModel,
        todoWidgetList = listOf(todoWidgetItem1, todoWidgetItem2),
        status = TodoWidgetListDataModel.STATUS_SUCCESS
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `given failed todo widget when refresh data todo widget then get success data todo widget`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockFailedTodoWidget)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeTodoWidgetUseCase = homeTodoWidgetUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val containsTodoWidgetFailed =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_ERROR
        Assert.assertTrue(containsTodoWidgetFailed)

        homeTodoWidgetUseCase.givenOnTodoWidgetReturn(mockFailedTodoWidget, mockSuccessTodoWidget)
        homeViewModel.getTodoWidgetRefresh()

        val containsTodoWidgetSuccess =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_SUCCESS
        Assert.assertTrue(containsTodoWidgetSuccess)
    }

//    @Test
//    fun `given success when dismiss todo widget item then remove todo widget from model`(){
//        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
//        val position = 0
//        val dataSource = "r3_category"
//        val param = "closeButton=user_id:data_source"
//
//        getHomeUseCase.givenGetHomeDataReturn(
//            HomeDynamicChannelModel(
//                list = listOf(mockSuccessTodoWidget)
//            )
//        )
//
//        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeTodoWidgetUseCase = homeTodoWidgetUseCase)
//        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)
//
//        val containsTodoWidgetSuccess =
//            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_SUCCESS
//        Assert.assertTrue(containsTodoWidgetSuccess)
//
//        coEvery { dismissTodoWidgetUseCase.getTodoWidgetDismissData(
//            position, dataSource, param, any(), any()
//        ) } answers {
//            lastArg<(Throwable) -> Unit>().invoke(Throwable())
//        }
//
//        homeViewModel.dismissTodoWidget(position, dataSource, param, deleteWidget = false)
//
//        val todoWidgetOnlyContain1 =
//            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).todoWidgetList.size == 1
//        Assert.assertTrue(todoWidgetOnlyContain1)
//    }
}
