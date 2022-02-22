package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.gopayhomewidget.presentation.domain.data.PayLaterWidgetData
import com.tokopedia.gopayhomewidget.presentation.domain.usecase.GetPayLaterWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomePayLaterWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelPayLaterHomeWidgetTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getGetPayLaterWidgetUseCase = mockk<GetPayLaterWidgetUseCase>(relaxed = true)

    private val errorMessage = "Failed"
    private val errorMockThrowable = Throwable(message = errorMessage)

    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `PayLaterWidget must be visible if there is a dynamic channel with home_todo layout`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    HomePayLaterWidgetDataModel( null, mockChannel)
                ))
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        // PayLaterWidget must be visible
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel } != null)
    }

    @Test
    fun `PayLaterWidget must not be visible if there is no dynamic channel with home_todo layout`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf())
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        // PayLaterWidget must not be visible
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel } == null)
    }

    @Test
    fun `PayLaterWidget must be visible with data when getPayLaterWidgetData Api result is successful`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    HomePayLaterWidgetDataModel( null, mockChannel)
                ))
        )

        val result = mockk<PayLaterWidgetData>(relaxed = true)
        coEvery { getGetPayLaterWidgetUseCase.getPayLaterWidgetData( any(), any()) }
            .coAnswers {
                firstArg<(PayLaterWidgetData) -> Unit>().invoke(result)
            }

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            getPayLaterWidgetUseCase = getGetPayLaterWidgetUseCase
        )

        //initial PayLaterWidgetData should be null -> Widget is showing without data
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel }?.let {
            assert((it as HomePayLaterWidgetDataModel).payLaterWidgetData == null)
        }

        homeViewModel.getPayLaterWidgetData()

        // getPayLaterWidgetData API called -> Success -> Widget is showing with data
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel }?.let {
            assert((it as HomePayLaterWidgetDataModel).payLaterWidgetData != null)
        }
    }

    @Test
    fun `PayLaterWidget must be deleted when getPayLaterWidgetData Api result is failed`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    HomePayLaterWidgetDataModel( null, mockChannel)
                ))
        )

        coEvery { getGetPayLaterWidgetUseCase.getPayLaterWidgetData(any(), any()) }
            .coAnswers {
                secondArg<(Throwable) -> Unit>().invoke(errorMockThrowable)
            }

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            getPayLaterWidgetUseCase = getGetPayLaterWidgetUseCase
        )

        // initial HomePayLaterWidgetDataModel must be null -> Widget must be showing but without data
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel }?.let {
            assert((it as HomePayLaterWidgetDataModel).payLaterWidgetData == null)
        }

        homeViewModel.getPayLaterWidgetData()

        // getPayLaterWidgetData API called -> Failed -> Widget must be deleted
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel } == null)
    }

    @Test
    fun `PayLaterWidget must be deleted when getCMHomeWidgetData throw exception`(){
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    HomePayLaterWidgetDataModel( null, mockChannel)
                ))
        )
        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            getPayLaterWidgetUseCase = getGetPayLaterWidgetUseCase
        )
        getHomeUseCase.givenGetHomeDataReturn(HomeDynamicChannelModel())
        coEvery { getGetPayLaterWidgetUseCase.getPayLaterWidgetData(any(), any()) } throws Exception()
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel }?.let {
            assert((it as HomePayLaterWidgetDataModel).payLaterWidgetData == null)
        }

        homeViewModel.getPayLaterWidgetData()
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel } == null)
    }

    @Test
    fun `PayLaterWidget must not be deleted locally`() {
        val homePayLaterWidgetDataModel = mockk<HomePayLaterWidgetDataModel>(relaxed = true)
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    homePayLaterWidgetDataModel
                ))
        )
        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase
        )
        homeViewModel.deletePayLaterWidgetLocally()
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is HomePayLaterWidgetDataModel } == null)
    }

}