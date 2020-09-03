package com.tokopedia.home.viewModel.homepage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.GetPopularKeywordUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeLoadingMoreModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test


/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelPopularKeywordUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeUseCase>(relaxed = true)
    private val getPopularKeywordUseCase = mockk<GetPopularKeywordUseCase>(relaxed = true)
    private lateinit var homeViewModel: HomeViewModel

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
    fun `Test Popular evaluate data with refresh`(){
        val popular = PopularKeywordListDataModel()
        val observerHome: Observer<HomeDataModel> = mockk(relaxed = true)

        // Data with popular keyword
        coEvery { getHomeUseCase.getHomeData() } returns flow{
            emit(HomeDataModel(
                    isCache = true,
                    list = listOf(popular)
            ))
            delay(5000)
            emit(HomeDataModel(
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

        Thread.sleep(8000)
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
                homeDataModel.list.isNotEmpty() && homeDataModel.list.find { it is HomeLoadingMoreModel } == null
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