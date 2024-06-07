package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.newatf.HomeAtfUseCase
import com.tokopedia.home.beranda.data.newatf.todo.TodoWidgetRepository
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeTodoWidgetUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRemoteConfigController
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home_component.visitable.TodoWidgetDataModel
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import io.mockk.coVerify
import io.mockk.every
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
    private val homeTodoWidgetUseCase = mockk<HomeTodoWidgetUseCase>(relaxed = true)
    private val todoWidgetRepository = mockk<TodoWidgetRepository>(relaxed = true)
    private val homeRemoteConfigController = mockk<HomeRemoteConfigController>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val mockFailedTodoWidgetDc = TodoWidgetListDataModel(
        id = "123",
        status = TodoWidgetListDataModel.STATUS_ERROR,
        source = TodoWidgetListDataModel.SOURCE_DC,
    )
    private val mockFailedTodoWidgetAtf = TodoWidgetListDataModel(
        id = "123",
        status = TodoWidgetListDataModel.STATUS_ERROR,
        source = TodoWidgetListDataModel.SOURCE_ATF,
    )
    private val todoWidgetItem1 = TodoWidgetDataModel(id = 1)
    private val todoWidgetItem2 = TodoWidgetDataModel(id = 2)
    private val mockSuccessTodoWidgetDc = TodoWidgetListDataModel(
        id = "123",
        todoWidgetList = listOf(todoWidgetItem1, todoWidgetItem2),
        status = TodoWidgetListDataModel.STATUS_SUCCESS,
        source = TodoWidgetListDataModel.SOURCE_DC,
    )
    private val mockSuccessTodoWidgetAtf = TodoWidgetListDataModel(
        id = "123",
        todoWidgetList = listOf(todoWidgetItem1, todoWidgetItem2),
        status = TodoWidgetListDataModel.STATUS_SUCCESS,
        source = TodoWidgetListDataModel.SOURCE_ATF,
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
    fun `given success when refresh todo widget then homeDataModel contain todo widget`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockFailedTodoWidgetDc)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeTodoWidgetUseCase = homeTodoWidgetUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val containsTodoWidgetFailed =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_ERROR
        Assert.assertTrue(containsTodoWidgetFailed)

        homeTodoWidgetUseCase.givenOnTodoWidgetReturn(mockFailedTodoWidgetDc, mockSuccessTodoWidgetDc)
        homeViewModel.getTodoWidgetRefresh()

        val containsTodoWidgetSuccess =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_SUCCESS
        Assert.assertTrue(containsTodoWidgetSuccess)
    }

    @Test
    fun `given success when refresh todo widget when using new ATF mechanism then call refresh data`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val homeAtfUseCase = mockk<HomeAtfUseCase>(relaxed = true)

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockFailedTodoWidgetAtf)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeTodoWidgetUseCase = homeTodoWidgetUseCase,
            homeRemoteConfigController = homeRemoteConfigController,
            homeAtfUseCase = homeAtfUseCase,
        )
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val containsTodoWidgetFailed =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_ERROR
        Assert.assertTrue(containsTodoWidgetFailed)

        homeViewModel.getTodoWidgetRefresh()

        coVerify { homeAtfUseCase.refreshData(mockFailedTodoWidgetAtf.id) }
    }

    @Test
    fun `given dismiss todo widget item then remove from model`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val position = 0
        val dataSource = "r3_category"
        val param = "closeButton=user_id:data_source"

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockSuccessTodoWidgetDc)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeTodoWidgetUseCase = homeTodoWidgetUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val containsTodoWidgetSuccess =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_SUCCESS
        Assert.assertTrue(containsTodoWidgetSuccess)

        homeViewModel.dismissTodoWidget(position, dataSource, param)

        val todoWidgetOnlyContain1 =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).todoWidgetList.size == (mockSuccessTodoWidgetDc.todoWidgetList.size - 1)
        Assert.assertTrue(todoWidgetOnlyContain1)
    }

    @Test
    fun `given dismiss all todo widget items then remove model entirely`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val position = 0
        val dataSource = "r3_category"
        val param = "closeButton=user_id:data_source"

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockSuccessTodoWidgetDc)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeTodoWidgetUseCase = homeTodoWidgetUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val containsTodoWidgetSuccess =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_SUCCESS
        Assert.assertTrue(containsTodoWidgetSuccess)

        repeat(mockSuccessTodoWidgetDc.todoWidgetList.size) {
            homeViewModel.dismissTodoWidget(position, dataSource, param)
        }

        Assert.assertNull(homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel })
    }

    @Test
    fun `given failed when removing dismissed todo widget from model then do nothing`() {
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val position = 0
        val dataSource = "r3_category"
        val param = "closeButton=user_id:data_source"

        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockSuccessTodoWidgetDc)
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homeTodoWidgetUseCase = homeTodoWidgetUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        val containsTodoWidgetSuccess =
            (homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel } as TodoWidgetListDataModel).status == TodoWidgetListDataModel.STATUS_SUCCESS
        Assert.assertTrue(containsTodoWidgetSuccess)

        every { homeViewModel["deleteWidget"](any<TodoWidgetListDataModel>(), any<Int>()) } throws Exception()

        repeat(mockSuccessTodoWidgetDc.todoWidgetList.size) {
            homeViewModel.dismissTodoWidget(position, dataSource, param)
        }

        Assert.assertNotNull(homeViewModel.homeDataModel.list.find { it is TodoWidgetListDataModel })
    }

    @Test
    fun `given dismiss todo widget when using new atf mechanism then call dismiss on TodoWidgetRepository`() {
        val position = 0
        val dataSource = "r3_category"
        val param = "closeButton=user_id:data_source"
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(mockSuccessTodoWidgetAtf)
            )
        )

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeTodoWidgetUseCase = homeTodoWidgetUseCase,
            todoWidgetRepository = todoWidgetRepository,
            homeRemoteConfigController = homeRemoteConfigController,
        )

        homeViewModel.dismissTodoWidget(position, dataSource, param)

        coVerify { todoWidgetRepository.dismissItemAt(position, param) }
    }

}
