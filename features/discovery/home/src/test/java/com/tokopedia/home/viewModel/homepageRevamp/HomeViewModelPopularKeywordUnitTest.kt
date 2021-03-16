package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.domain.interactor.GetPopularKeywordUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeLoadingMoreModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelPopularKeywordUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val getPopularKeywordUseCase = mockk<GetPopularKeywordUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `Test Popular with data keyword`(){
        val popular = PopularKeywordListDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Data with popular keyword
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf(popular)
                )
        )

        // Popular keyword data
        coEvery { getPopularKeywordUseCase.executeOnBackground() } returns HomeWidget.PopularKeywordQuery(
                data = HomeWidget.PopularKeywordList(
                        keywords = listOf(
                                HomeWidget.PopularKeyword(),
                                HomeWidget.PopularKeyword(),
                                HomeWidget.PopularKeyword(),
                                HomeWidget.PopularKeyword()
                        )
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPopularKeywordUseCase = getPopularKeywordUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Load popular keyword
        homeViewModel.getPopularKeywordData()

        // Expect popular keyword will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is PopularKeywordListDataModel } != null
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is PopularKeywordListDataModel } != null
                        && (homeDataModel.list.find { it is PopularKeywordListDataModel } as PopularKeywordListDataModel).popularKeywordList.isNotEmpty()
            })
        }
        confirmVerified(observerHome)

    }

    @Test
    fun `Test Popular evaluate data with refresh`() = runBlockingTest {
        val popular = PopularKeywordListDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)
        val channel = Channel<HomeDataModel>()
        // Data with popular keyword
        coEvery { getHomeUseCase.getHomeData() } returns flow{
            channel.consumeAsFlow().collect {
                emit(it)
            }
        }

        launch {
            channel.send(HomeDataModel(
                    isCache = true,
                    list = listOf(popular)
            ))
        }

        // Popular keyword data
        coEvery { getPopularKeywordUseCase.executeOnBackground() } returns HomeWidget.PopularKeywordQuery(
                data = HomeWidget.PopularKeywordList(
                        keywords = listOf(
                                HomeWidget.PopularKeyword(),
                                HomeWidget.PopularKeyword(),
                                HomeWidget.PopularKeyword(),
                                HomeWidget.PopularKeyword()
                        )
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, getPopularKeywordUseCase = getPopularKeywordUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        launch {
            channel.send(HomeDataModel(
                    list = listOf(popular),
                    isProcessingAtf = false
            ))
        }
        // Expect popular keyword will show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is PopularKeywordListDataModel } != null
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is HomeLoadingMoreModel } != null
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is HomeRetryModel } != null
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.isNotEmpty()
            })
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find { it is PopularKeywordListDataModel } != null
                        && (homeDataModel.list.find { it is PopularKeywordListDataModel } as PopularKeywordListDataModel).popularKeywordList.isNotEmpty()
            })
        }
        confirmVerified(observerHome)
        channel.close()
    }

    @Test
    fun `Test Popular is not visible` (){
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Populate empty data
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDataModel(
                        list = listOf()
                )
        )

        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.homeLiveData.observeForever(observerHome)

        // Expect popular widget not show on user screen
        verifyOrder {
            // check on home data initial first channel is dynamic channel
            observerHome.onChanged(match { homeDataModel ->
                homeDataModel.list.find { it is PopularKeywordListDataModel } == null
            })
        }
        confirmVerified(observerHome)
    }
}