package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomePopularKeywordUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*


/**
 * Created by Lukas on 14/05/20.
 */

class HomeViewModelPopularKeywordUnitTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val homePopularKeywordUseCase = mockk<HomePopularKeywordUseCase>(relaxed = true)
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
        val observerHome: Observer<HomeDynamicChannelModel> = mockk(relaxed = true)
        val refreshCount = 1

        // Data with popular keyword
        getHomeUseCase.givenGetHomeDataReturn(
                HomeDynamicChannelModel(
                        list = listOf(popular)
                )
        )

        val resultSuccessPopularKeyword =
            PopularKeywordListDataModel(
                popularKeywordList = mutableListOf<PopularKeywordDataModel>(
                    PopularKeywordDataModel(recommendationType = "recommendation_1")
                )
            )


        // home viewModel
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, homePopularKeywordUseCase = homePopularKeywordUseCase)
        homeViewModel.homeLiveDynamicChannel.observeForever(observerHome)

        homePopularKeywordUseCase.givenOnPopularKeywordReturn(refreshCount, popular, resultSuccessPopularKeyword)
        // Load popular keyword
        homeViewModel.getPopularKeywordOnRefresh()

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
}