package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.usecase.DismissCMHomeWidgetUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.home.beranda.data.usecase.HomeRevampUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CMHomeWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelCMHomeWidgetTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeRevampUseCase>(relaxed = true)
    private val getCMHomeWidgetDataUseCase = mockk<GetCMHomeWidgetDataUseCase>(relaxed = true)
    private val dismissCMHomeWidgetUseCase = mockk<DismissCMHomeWidgetUseCase>(relaxed = true)

    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `CMHomeWidget must be visible if there is a dynamic channel with home_todo layout`() {
        val cmHomeWidgetDataModel = CMHomeWidgetDataModel(null)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDataModel(
                list = listOf(cmHomeWidgetDataModel)
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        // tells that CMHomeWidget is visible
        assert(homeViewModel.homeLiveData.value?.list?.find { it is CMHomeWidgetDataModel } != null)
    }

    @Test
    fun `CMHomeWidget must not be visible if there is no dynamic channel with home_todo layout`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDataModel(
                list = listOf()
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        // tells that CMHomeWidget is not visible
        assert(homeViewModel.homeLiveData.value?.list?.find { it is CMHomeWidgetDataModel } == null)
    }

    @Test
    fun `CMHomeWidget is visible and getCMHomeWidgetData Api result is successful then data should be shown`() {
        val cmHomeWidgetDataModel = CMHomeWidgetDataModel(cmHomeWidgetData = null)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDataModel(
                list = listOf(cmHomeWidgetDataModel)
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        //initial data should be null
        homeViewModel.homeLiveData.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData == null)
        }

        val result = mockk<CMHomeWidgetDataResponse>(relaxed = true)
        coEvery { getCMHomeWidgetDataUseCase.getCMHomeWidgetData(any(), any(), any()) }
            .coAnswers {
                firstArg<(CMHomeWidgetDataResponse) -> Unit>().invoke(result)
            }
        homeViewModel.getCMHomeWidgetData()
        //after calling api -> data should not be null
        homeViewModel.homeLiveData.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData == null)
        }
    }

    @Test
    fun `CMHomeWidget must be refresh only after 60 sec`() {
        val cmHomeWidgetDataModel = mockk<CMHomeWidgetDataModel>(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDataModel(
                list = listOf(cmHomeWidgetDataModel)
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.getCMHomeWidgetData()
        homeViewModel.getCMHomeWidgetData(false)
    }
}