package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.cmhomewidget.domain.data.CMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.data.DeleteCMHomeWidgetDataResponse
import com.tokopedia.cmhomewidget.domain.usecase.DeleteCMHomeWidgetUseCase
import com.tokopedia.cmhomewidget.domain.usecase.GetCMHomeWidgetDataUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CMHomeWidgetDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelCMHomeWidgetTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val getUserSession = mockk<UserSessionInterface>(relaxed = true)
    private val getCMHomeWidgetDataUseCase = mockk<GetCMHomeWidgetDataUseCase>(relaxed = true)
    private val deleteCMHomeWidgetUseCase = mockk<DeleteCMHomeWidgetUseCase>(relaxed = true)

    private val errorMessage = "Failed"
    private val errorMockThrowable = Throwable(message = errorMessage)

    private lateinit var homeViewModel: HomeRevampViewModel

    @Test
    fun `CMHomeWidget must be visible if there is a dynamic channel with home_todo layout`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
            list = listOf(
                CMHomeWidgetDataModel( null, mockChannel)
            ))
        )
        coEvery { getUserSession.isLoggedIn } returns false
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase, userSessionInterface = getUserSession)

        // CMHomeWidget must be visible
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } != null)
    }

    @Test
    fun `CMHomeWidget must not be visible if there is no dynamic channel with home_todo layout`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf())
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        // CMHomeWidget must not be visible
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } == null)
    }

    @Test
    fun `CMHomeWidget must be visible with data when getCMHomeWidgetData Api result is successful`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    CMHomeWidgetDataModel( null, mockChannel)
                ))
        )

        val result = mockk<CMHomeWidgetDataResponse>(relaxed = true)
        coEvery { getCMHomeWidgetDataUseCase.getCMHomeWidgetData(any(), any(), any()) }
            .coAnswers {
                firstArg<(CMHomeWidgetDataResponse) -> Unit>().invoke(result)
            }

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            getCMHomeWidgetDataUseCase = getCMHomeWidgetDataUseCase
        )

        //initial cmHomeWidgetData should be null -> Widget is showing without data
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData == null)
        }

        homeViewModel.getCMHomeWidgetData()

        // getCMHomeWidgetData API called -> Success -> Widget is showing with data
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData != null)
        }
    }

    @Test
    fun `CMHomeWidget must be deleted when getCMHomeWidgetData Api result is failed`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    CMHomeWidgetDataModel( null, mockChannel)
                ))
        )

        coEvery { getCMHomeWidgetDataUseCase.getCMHomeWidgetData(any(), any(), any()) }
            .coAnswers {
                secondArg<(Throwable) -> Unit>().invoke(errorMockThrowable)
            }

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            getCMHomeWidgetDataUseCase = getCMHomeWidgetDataUseCase
        )

        // initial cmHomeWidgetData must be null -> Widget must be showing but without data
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData == null)
        }

        homeViewModel.getCMHomeWidgetData()

        // getCMHomeWidgetData API called -> Failed -> Widget must be deleted
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } == null)
    }

    @Test
    fun `CMHomeWidget must be deleted when deleteCMHomeWidgetData Api result is successful`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    CMHomeWidgetDataModel( null, mockChannel)
                ))
        )

        val result = mockk<DeleteCMHomeWidgetDataResponse>(relaxed = true)
        coEvery { deleteCMHomeWidgetUseCase.deleteCMHomeWidgetData(any(), any(), any(), any()) }
            .coAnswers {
                firstArg<(DeleteCMHomeWidgetDataResponse) -> Unit>().invoke(result)
            }

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            deleteCMHomeWidgetUseCase = deleteCMHomeWidgetUseCase
        )

        //Widget must be showing with data
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } != null)
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData != null)
        }

        homeViewModel.deleteCMHomeWidget()

        // deleteCMHomeWidgetData API called -> Success -> Widget must be deleted
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } == null)
    }

    @Test
    fun `CMHomeWidget must not be deleted when deleteCMHomeWidgetData Api result is failed`() {
        val mockChannel = DynamicHomeChannel.Channels()
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    CMHomeWidgetDataModel( null, mockChannel)
                ))
        )

        coEvery { deleteCMHomeWidgetUseCase.deleteCMHomeWidgetData(any(), any(), any(), any()) }
            .coAnswers {
                secondArg<(Throwable) -> Unit>().invoke(errorMockThrowable)
            }

        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            deleteCMHomeWidgetUseCase = deleteCMHomeWidgetUseCase
        )

        //Widget must be showing with data
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } != null)
        homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel }?.let {
            assert((it as CMHomeWidgetDataModel).cmHomeWidgetData != null)
        }

        homeViewModel.deleteCMHomeWidget()

        // deleteCMHomeWidgetData API called -> Success -> Widget must be deleted
        assert(homeViewModel.homeLiveDynamicChannel.value?.list?.find { it is CMHomeWidgetDataModel } != null)
    }
}