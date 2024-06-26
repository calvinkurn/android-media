package com.tokopedia.home.viewModel.homepageRevamp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeBalanceWidgetUseCase
import com.tokopedia.home.beranda.domain.interactor.usecase.HomeDynamicChannelUseCase
import com.tokopedia.home.beranda.helper.Event
import com.tokopedia.home.beranda.helper.RateLimiter
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.HomeDynamicChannelModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelLoadingModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.DynamicChannelRetryModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.HomeHeaderDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.TickerDataModel
import com.tokopedia.home.beranda.presentation.view.helper.HomeRemoteConfigController
import com.tokopedia.home.beranda.presentation.viewModel.HomeRevampViewModel
import com.tokopedia.home.ext.observeOnce
import com.tokopedia.home.util.HomeRefreshType
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.visitable.BannerDataModel
import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.unit.test.rule.UnconfinedTestRule
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

@OptIn(FlowPreview::class)
@ExperimentalCoroutinesApi
class HomeViewModelDynamicChannelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = UnconfinedTestRule()

    private val dispatcher = coroutineTestRule.dispatchers
    private val testDispatcher = coroutineTestRule.coroutineDispatcher

    private val getHomeUseCase = mockk<HomeDynamicChannelUseCase>(relaxed = true)
    private val userSession = mockk<UserSessionInterface>(relaxed = true)
    private val getHomeBalanceWidgetUseCase = mockk<HomeBalanceWidgetUseCase>(relaxed = true)
    private val homeRemoteConfigController = mockk<HomeRemoteConfigController>(relaxed = true)
    private lateinit var homeViewModel: HomeRevampViewModel

    private val mockExpiredChannelModel = ChannelModel(id = "1", groupId = "1")
    private val mockExpiredVisitable = DynamicLegoBannerDataModel(mockExpiredChannelModel)

    private val mockInitialVisitableList = HomeDynamicChannelModel(
        list = listOf(
            mockExpiredVisitable
        )
    )

    private val mockNewVisitableList = listOf(
        DynamicLegoBannerDataModel(ChannelModel(id = "2", groupId = "2")),
        DynamicLegoBannerDataModel(ChannelModel(id = "3", groupId = "3")),
        DynamicLegoBannerDataModel(ChannelModel(id = "4", groupId = "4"))
    )

    @ExperimentalCoroutinesApi
    @Test
    fun `When dynamic channel usecase return another list on onDynamicChannelExpired then homeDataModel should contains new data`() {
        getHomeUseCase.givenGetHomeDataReturn(mockInitialVisitableList)
        getHomeUseCase.givenOnDynamicChannelExpiredReturns(mockNewVisitableList)

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.getDynamicChannelDataOnExpired(mockExpiredVisitable, mockExpiredChannelModel, 0)
        Assert.assertTrue(
            homeViewModel.homeDataModel.list.size == mockNewVisitableList.size
        )
        Assert.assertTrue(
            (homeViewModel.trackingLiveData.value as Event).getContentIfNotHandled()
                ?.isEmpty() == true
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When dynamic channel usecase return empty list on onDynamicChannelExpired then homeDataModel should not contains any data`() {
        getHomeUseCase.givenGetHomeDataReturn(mockInitialVisitableList)
        getHomeUseCase.givenOnDynamicChannelExpiredReturns(listOf())

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.getDynamicChannelDataOnExpired(mockExpiredVisitable, mockExpiredChannelModel, 0)
        Assert.assertTrue(homeViewModel.homeDataModel.list.isEmpty())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When dynamic channel usecase throws error on onDynamicChannelExpired then homeDataModel should not contains any data`() {
        getHomeUseCase.givenGetHomeDataReturn(mockInitialVisitableList)
        getHomeUseCase.givenOnDynamicChannelExpiredError()

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.getDynamicChannelDataOnExpired(mockExpiredVisitable, mockExpiredChannelModel, 0)
        Assert.assertTrue(homeViewModel.homeDataModel.list.isEmpty())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When dynamic channel usecase throws error on updateHomeData then updateNetworkLiveData should triggered with exception`() {
        getHomeUseCase.givenGetHomeDataError()
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.updateNetworkLiveData.observeOnce {
            Assert.assertTrue(it.error is Throwable)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When homeDataModel contains DynamicChannelLoadingModel and error pagination on updateHomeData then homeDataModel should contains DynamicChannelRetryModel with state not loading`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelLoadingModel()
                )
            )
        )
        getHomeUseCase.givenUpdateHomeDataReturn(Result.errorPagination(error = Throwable()))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.refreshHomeData(HomeRefreshType.FIRST_OPEN)
        homeViewModel.homeDataModel.findWidget<DynamicChannelRetryModel>(
            actionOnFound = { model, index ->
                Assert.assertTrue(!model.isLoading)
            },
            actionOnNotFound = {
                Assert.assertTrue(true)
            }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When homeDataModel contains DynamicChannelRetryModel and error pagination on updateHomeData then homeDataModel should contains DynamicChannelRetryModel with state not loading`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelRetryModel(true)
                )
            )
        )
        getHomeUseCase.givenUpdateHomeDataReturn(Result.errorPagination(error = Throwable()))
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.refreshHomeData(HomeRefreshType.FIRST_OPEN)
        homeViewModel.homeDataModel.findWidget<DynamicChannelRetryModel>(
            actionOnFound = { model, index ->
                Assert.assertTrue(!model.isLoading)
            },
            actionOnNotFound = {
                Assert.assertTrue(true)
            }
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When catch error on updateHomeData then homeDataModel should contains DynamicChannelRetryModel with state not loading`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    HomeHeaderDataModel(),
                    DynamicChannelRetryModel(true)
                )
            )
        )
        every { userSession.isLoggedIn } returns true
        getHomeUseCase.givenUpdateHomeDataError()
        getHomeBalanceWidgetUseCase.givenGetLoadingStateReturn()
        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            userSessionInterface = userSession,
            homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        homeViewModel.refreshHomeData(HomeRefreshType.FIRST_OPEN)
        homeViewModel.updateNetworkLiveData.observeOnce {
            Assert.assertTrue(it.error is Throwable)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When catch error null message on updateHomeData then homeDataModel should contains DynamicChannelRetryModel with state not loading`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelRetryModel(true)
                )
            )
        )
        getHomeUseCase.givenUpdateHomeDataErrorNullMessage()
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.refreshHomeData(HomeRefreshType.FIRST_OPEN)
        homeViewModel.updateNetworkLiveData.observeOnce {
            Assert.assertTrue(it.error is Throwable)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When selected position = 0 on removeViewHolderAtPosition then homeDataModel should remove model on position 0`() {
        val selectedPosition = 0
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "0", groupId = "")),
                    DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "1", groupId = "")),
                    DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "2", groupId = ""))
                )
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.removeViewHolderAtPosition(selectedPosition)
        homeViewModel.homeDataModel.findWidget<DynamicLegoBannerDataModel>(
            predicate = {
                it?.channelModel?.id == selectedPosition.toString()
            },
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
    fun `When selected position = -1 on removeViewHolderAtPosition then homeDataModel should not changed`() {
        val selectedPosition = -1
        val mockList = listOf(
            DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "0", groupId = "")),
            DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "1", groupId = "")),
            DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "2", groupId = ""))
        )
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = mockList
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.removeViewHolderAtPosition(selectedPosition)
        Assert.assertTrue(homeViewModel.homeDataModel.list.size == mockList.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `When selected position more than mockSizeList on removeViewHolderAtPosition then homeDataModel should not changed`() {
        val selectedPosition = 4
        val mockList = listOf(
            DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "0", groupId = "")),
            DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "1", groupId = "")),
            DynamicLegoBannerDataModel(channelModel = ChannelModel(id = "2", groupId = ""))
        )
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = mockList
            )
        )

        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.removeViewHolderAtPosition(selectedPosition)
        Assert.assertTrue(homeViewModel.homeDataModel.list.size == mockList.size)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given job still active when refresh home data then hide pull refresh`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelLoadingModel()
                )
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        val mockGetHomeDataJob = mockk<Job>(relaxed = true)
        homeViewModel.getHomeDataJob = mockGetHomeDataJob
        every { homeViewModel.getHomeDataJob?.isActive } returns true
        homeViewModel.refreshHomeData(HomeRefreshType.PULL_TO_REFRESH)
        homeViewModel.hideShowLoading.observeOnce {
            Assert.assertTrue(it.getContentIfNotHandled() ?: false)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given job not active when refresh home data then pull refresh still animate`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelLoadingModel()
                )
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        val mockGetHomeDataJob = mockk<Job>(relaxed = true)
        homeViewModel.getHomeDataJob = mockGetHomeDataJob
        every { homeViewModel.getHomeDataJob?.isActive } returns false
        homeViewModel.refreshHomeData(HomeRefreshType.PULL_TO_REFRESH)
        Assert.assertNull(homeViewModel.hideShowLoading.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given flow home data completed when refresh home data then hide pull refresh`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelLoadingModel()
                )
            )
        )
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        val mockHomeDataModel = mockk<HomeDynamicChannelModel>(relaxed = true)
        homeViewModel.homeDataModel = mockHomeDataModel
        every { homeViewModel.homeDataModel.flowCompleted } returns false
        homeViewModel.refreshHomeData(HomeRefreshType.PULL_TO_REFRESH)
        homeViewModel.hideShowLoading.observeOnce {
            Assert.assertTrue(it.getContentIfNotHandled() ?: false)
        }
    }

    @Test
    fun `given above three mins rule when refresh then refreshHomeData`() {
        every { userSession.isLoggedIn } returns true

        homeViewModel = createHomeViewModel(
            userSessionInterface = userSession,
            getHomeUseCase = getHomeUseCase,
            homeBalanceWidgetUseCase = getHomeBalanceWidgetUseCase
        )
        every { (homeViewModel.getProperty("homeRateLimit") as RateLimiter<String>).shouldFetch(any()) } returns true
        homeViewModel.refreshWithThreeMinsRules(false, refreshType = HomeRefreshType.AUTO_REFRESH)

        verify { homeViewModel.refreshHomeData(HomeRefreshType.AUTO_REFRESH) }
        homeViewModel.isNeedRefresh.observeOnce {
            Assert.assertTrue(it.getContentIfNotHandled() == true)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `given error atf when refresh data should update error atf to updateNetworkLiveData`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                list = listOf(
                    DynamicChannelLoadingModel()
                )
            )
        )
        val mockErrorAtf = Result.errorAtf<Any>(error = Throwable())
        getHomeUseCase.givenUpdateHomeDataReturn(mockErrorAtf)
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)
        homeViewModel.refreshHomeData(HomeRefreshType.PULL_TO_REFRESH)
        homeViewModel.updateNetworkLiveData.observeOnce {
            Assert.assertEquals(it, mockErrorAtf)
        }
    }

    @Test
    fun `given cache data exists when init flow then update home data`() {
        val mockExistingData = HomeDynamicChannelModel(
            list = listOf(
                HomeHeaderDataModel(),
                TickerDataModel(),
                BannerDataModel()
            ),
            isCache = true
        )
        getHomeUseCase.givenGetHomeDataReturn(mockExistingData)
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        justRun { homeViewModel invokeNoArgs "initFlow" }
        Assert.assertEquals(homeViewModel.homeDataModel, mockExistingData)
        homeViewModel.homeLiveDynamicChannel.observeOnce {
            Assert.assertEquals(it, mockExistingData)
        }
    }

    @Test
    fun `given non-cache data exists when init flow then update home data`() {
        val mockExistingData = HomeDynamicChannelModel(
            list = listOf(
                HomeHeaderDataModel(),
                TickerDataModel(),
                BannerDataModel()
            ),
            isCache = false
        )
        getHomeUseCase.givenGetHomeDataReturn(mockExistingData)
        homeViewModel = createHomeViewModel(getHomeUseCase = getHomeUseCase)

        justRun { homeViewModel invokeNoArgs "initFlow" }
        Assert.assertEquals(homeViewModel.homeDataModel, mockExistingData)
        homeViewModel.homeLiveDynamicChannel.observeOnce {
            Assert.assertEquals(it, mockExistingData)
        }
        homeViewModel.isRequestNetworkLiveData.observeOnce {
            Assert.assertTrue(it.getContentIfNotHandled() == false)
        }
    }

    @Test
    fun `given flag isAtfError is true when collecting HomeDynamicChannelModel then show error general`() {
        getHomeUseCase.givenGetHomeDataReturn(
            HomeDynamicChannelModel(
                listOf(HomeHeaderDataModel()),
                isCache = false,
                isAtfError = true
            )
        )
        homeViewModel = createHomeViewModel(
            getHomeUseCase = getHomeUseCase,
            homeRemoteConfigController = homeRemoteConfigController
        )
        homeViewModel.updateNetworkLiveData.observeOnce {
            Assert.assertTrue(it.error is Throwable)
        }
    }
}
