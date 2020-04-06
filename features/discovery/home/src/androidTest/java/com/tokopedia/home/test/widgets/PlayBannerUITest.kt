package com.tokopedia.home.test.widgets

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.model.Config
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.data.model.VideoStream
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import com.tokopedia.home.test.json.HomeJson
import com.tokopedia.home.test.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PlayBannerUITest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private val userSessionInterface = mockk<UserSessionInterface>(relaxed = true)
    private val dismissHomeReviewUseCase = mockk<DismissHomeReviewUseCase>(relaxed = true)
    private val getHomeReviewSuggestedUseCase = mockk<GetHomeReviewSuggestedUseCase>(relaxed = true)
    private val getKeywordSearchUseCase = mockk<GetKeywordSearchUseCase>(relaxed = true)
    private val getRecommendationTabUseCase = mockk<GetRecommendationTabUseCase>(relaxed = true)
    private val getHomeTokopointsDataUseCase = mockk<GetHomeTokopointsDataUseCase>(relaxed = true)
    private val getCoroutinePendingCashbackUseCase = mockk<GetCoroutinePendingCashbackUseCase> (relaxed = true)
    private val getPlayLiveDynamicUseCase = mockk<GetPlayLiveDynamicUseCase> (relaxed = true)
    private val getCoroutineWalletBalanceUseCase = mockk<GetCoroutineWalletBalanceUseCase> (relaxed = true)
    private val getHomeUseCase = mockk<HomeUseCase> (relaxed = true)
    private val getSendGeolocationInfoUseCase = mockk<SendGeolocationInfoUseCase> (relaxed = true)
    private val getStickyLoginUseCase = mockk<StickyLoginUseCase> (relaxed = true)
    private val getBusinessWidgetTab = mockk<GetBusinessWidgetTab> (relaxed = true)
    private val getBusinessUnitDataUseCase = mockk<GetBusinessUnitDataUseCase> (relaxed = true)
    private val getPopularKeywordUseCase = mockk<GetPopularKeywordUseCase> (relaxed = true)
    private val getDynamicChannelsUseCase = mockk<GetDynamicChannelsUseCase> (relaxed = true)
    private val sendTopAdsUseCase = mockk<SendTopAdsUseCase>(relaxed = true)
    private val homeDataMapper = HomeDataMapper(InstrumentationRegistry.getInstrumentation().context, HomeVisitableFactoryImpl(userSessionInterface), mockk(relaxed = true))


    private lateinit var viewModel: HomeViewModel;

    @Before
    fun setup(){
        every { userSessionInterface.isLoggedIn } returns false
    }

    @Test
    fun testNoSkeletonDataFromHome(){
        val json = HomeJson.resultNoSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            Log.d("testNoSkeleton", "Flow emit masuk")
        }
        viewModel = reInitViewModel()
        Log.d("testNoSkeleton", viewModel.toString())
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(R.id.play_frame_layout)).check(doesNotExist())
    }

    @Test
    fun testHappyPathPlayBannerUI(){
        val json = HomeJson.resultWithSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        Log.d("testNoSkeleton", "Home data init " + homeData.dynamicHomeChannel.toString())
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            Log.d("testNoSkeleton", "Flow emit masuk")
        }
        coEvery { getPlayLiveDynamicUseCase.executeOnBackground() } returns PlayData(
                listOf(
                        PlayChannel(
                                title = "Channel 1",
                                description = "Description Channel 1",
                                channelId = "1",
                                coverUrl = "https://ecs7.tokopedia.net/img/cache/1400/attachment/2020/3/26/1585203280769/1585203280769_aaa8879c-4456-4f99-a801-71c3322f941d.jpg",
                                totalView = "200rb",
                                videoStream =  VideoStream(
                                        isLive = false,
                                        config = Config(
                                                streamUrl = "http://cobastrea.com/s/1/",
                                                isAutoPlay = false
                                        )
                                )
                        )
                )
        )
        viewModel = reInitViewModel()
        Log.d("testNoSkeleton", viewModel.toString())
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(R.id.play_frame_layout)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(withText("Play Widget")))
        onView(withId(R.id.title_play)).check(matches(withText("Channel 1")))
        Thread.sleep(5000)
    }


    @Test
    fun testNotValidImageUrlFromBackend(){
        val json = HomeJson.resultWithSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        Log.d("testNoSkeleton", "Home data init " + homeData.dynamicHomeChannel.toString())
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            Log.d("testNoSkeleton", "Flow emit masuk")
        }
        coEvery { getPlayLiveDynamicUseCase.executeOnBackground() } returns PlayData(
                listOf(
                        PlayChannel(
                                title = "Channel 1",
                                description = "Description Channel 1",
                                channelId = "1",
                                coverUrl = "",
                                totalView = "200rb",
                                videoStream =  VideoStream(
                                        isLive = false,
                                        config = Config(
                                                streamUrl = "http://cobastrea.com/s/1/",
                                                isAutoPlay = false
                                        )
                                )
                        )
                )
        )
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(R.id.play_frame_layout)).check(matches(not(isDisplayed())))
        Thread.sleep(5000)
    }

    @Test
    fun testNoReturnDataPlayFromBackend(){
        val json = HomeJson.resultWithSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        Log.d("testNoSkeleton", "Home data init " + homeData.dynamicHomeChannel.toString())
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            Log.d("testNoSkeleton", "Flow emit masuk")
        }
        coEvery { getPlayLiveDynamicUseCase.executeOnBackground() } returns PlayData(
                listOf()
        )
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(R.id.play_frame_layout)).check(matches(not(isDisplayed())))
        Thread.sleep(5000)
    }

    @Test
    fun testErrorDataPlayFromBackend(){
        val json = HomeJson.resultWithSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        Log.d("testNoSkeleton", "Home data init " + homeData.dynamicHomeChannel.toString())
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            Log.d("testNoSkeleton", "Flow emit masuk")
        }
        coEvery { getPlayLiveDynamicUseCase.executeOnBackground() } throws RuntimeException()
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(R.id.play_frame_layout)).check(matches(not(isDisplayed())))
        Thread.sleep(5000)
    }

    @Test
    fun testUpdateBannerAndDataFromHomeEmpty(){
        coEvery { getHomeUseCase.updateHomeData() } returns flow {
            emit(Result.success(""))
        }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            var json = HomeJson.resultNoSkeleton
            var homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            delay(4000)
            json = HomeJson.resultWithSkeleton
            homeData = Gson().fromJson(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.executeOnBackground() } returns PlayData(
                listOf(
                        PlayChannel(
                                title = "Channel 1",
                                description = "Description Channel 1",
                                channelId = "1",
                                coverUrl = "https://ecs7.tokopedia.net/img/cache/1400/attachment/2020/3/26/1585203280769/1585203280769_aaa8879c-4456-4f99-a801-71c3322f941d.jpg",
                                totalView = "200rb",
                                videoStream =  VideoStream(
                                        isLive = false,
                                        config = Config(
                                                streamUrl = "http://cobastrea.com/s/1/",
                                                isAutoPlay = false
                                        )
                                )
                        )
                )
        )
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(3000)
        onView(withId(R.id.play_frame_layout)).check(doesNotExist())
        Thread.sleep(4000)
        onView(withId(R.id.play_frame_layout)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(withText("Play Widget")))
        onView(withId(R.id.title_play)).check(matches(withText("Channel 1")))
    }

    @Test
    fun testUpdateBannerFromPlayDataDifferent(){
        coEvery { getHomeUseCase.updateHomeData() } returns flow {
            emit(Result.success(""))
        }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            var json = HomeJson.resultWithSkeleton
            var homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            delay(4000)
            json = HomeJson.resultWithSkeleton
            homeData = Gson().fromJson(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.executeOnBackground() } returns PlayData(
                listOf(
                        PlayChannel(
                                title = "Channel 1",
                                description = "Description Channel 1",
                                channelId = "1",
                                coverUrl = "https://ecs7.tokopedia.net/img/cache/1400/attachment/2020/3/26/1585203280769/1585203280769_aaa8879c-4456-4f99-a801-71c3322f941d.jpg",
                                totalView = "200rb",
                                videoStream =  VideoStream(
                                        isLive = false,
                                        config = Config(
                                                streamUrl = "http://cobastrea.com/s/1/",
                                                isAutoPlay = false
                                        )
                                )
                        )
                )
        ) andThen PlayData(
                listOf(
                        PlayChannel(
                                title = "Channel 2",
                                description = "Description Channel 2",
                                channelId = "3",
                                coverUrl = "https://ecs7.tokopedia.net/img/cache/1400/attachment/2020/3/26/1585203280769/1585203280769_aaa8879c-4456-4f99-a801-71c3322f941d.jpg",
                                totalView = "200rb",
                                videoStream =  VideoStream(
                                        isLive = false,
                                        config = Config(
                                                streamUrl = "http://cobastrea.com/s/2/",
                                                isAutoPlay = false
                                        )
                                )
                        )
                )
        )
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(2000)
        onView(withId(R.id.play_frame_layout)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(withText("Play Widget")))
        onView(withId(R.id.title_play)).check(matches(withText("Channel 1")))
        Thread.sleep(2000)
        onView(withId(R.id.play_frame_layout)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(R.id.title)).check(matches(isDisplayed()))
        onView(withId(R.id.title)).check(matches(withText("Play Widget")))
        onView(withId(R.id.title_play)).check(matches(withText("Channel 2")))
    }

    private fun <T : ViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
                if (viewModelClass.isAssignableFrom(viewModel.javaClass)) {
                    Log.d("testNoSkeleton", "Masuk custom view model factory")
                    @Suppress("UNCHECKED_CAST")
                    return viewModel as T
                }
                throw IllegalArgumentException("Unknown view model class " + viewModelClass)
            }
        }
    }

    private fun reInitViewModel() = HomeViewModel(
            dismissHomeReviewUseCase = dismissHomeReviewUseCase,
            getBusinessUnitDataUseCase = getBusinessUnitDataUseCase,
            getBusinessWidgetTab = getBusinessWidgetTab,
            getDynamicChannelsUseCase = getDynamicChannelsUseCase,
            getHomeReviewSuggestedUseCase = getHomeReviewSuggestedUseCase,
            getHomeTokopointsDataUseCase = getHomeTokopointsDataUseCase,
            getKeywordSearchUseCase = getKeywordSearchUseCase,
            getPendingCashbackUseCase = getCoroutinePendingCashbackUseCase,
            getPlayCardHomeUseCase = getPlayLiveDynamicUseCase,
            getRecommendationTabUseCase = getRecommendationTabUseCase,
            getWalletBalanceUseCase = getCoroutineWalletBalanceUseCase,
            homeDispatcher = TestDispatcherProvider(),
            homeUseCase = getHomeUseCase,
            popularKeywordUseCase = getPopularKeywordUseCase,
            sendGeolocationInfoUseCase = getSendGeolocationInfoUseCase,
            stickyLoginUseCase = getStickyLoginUseCase,
            userSession = userSessionInterface,
            sendTopAdsUseCase = sendTopAdsUseCase
    )
}