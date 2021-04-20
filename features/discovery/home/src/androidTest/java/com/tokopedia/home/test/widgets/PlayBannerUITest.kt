package com.tokopedia.home.test.widgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.datasource.default_data_source.HomeDefaultDataSource
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.HomeDynamicChannelDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeDynamicChannelVisitableFactoryImpl
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.model.Config
import com.tokopedia.home.beranda.data.model.PlayChannel
import com.tokopedia.home.beranda.data.model.PlayData
import com.tokopedia.home.beranda.data.model.VideoStream
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.helper.Result
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import com.tokopedia.trackingoptimizer.TrackingQueue
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PlayBannerUITest : BaseWidgetUiTest(){
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    override val homeVisitableFactory = HomeVisitableFactoryImpl(userSessionInterface.get(), remoteConfig, HomeDefaultDataSource())
    override val homeDynamicChannelVisitableFactory = HomeDynamicChannelVisitableFactoryImpl(userSessionInterface.get(), remoteConfig, HomeDefaultDataSource())
    override val instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    override val homeDataMapper = HomeDataMapper(instrumentationContext, homeVisitableFactory, mockk(relaxed = true),
            HomeDynamicChannelDataMapper(instrumentationContext, homeDynamicChannelVisitableFactory, TrackingQueue(instrumentationContext)))
    val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var viewModel: HomeViewModel

    companion object{
        private val CONTAINER = R.id.play_frame_layout
        private val TITLE = R.id.title
        private val TITLE_CONTENT = R.id.title_play
    }

    @Test
    fun test_when_no_data_skeleton_from_home_api_and_expect_the_widget_not_show(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(CONTAINER)).check(doesNotExist())
    }

    @Test
    fun test_given_data_play_and_the_widget_must_take_data_from_play_api_and_expect_the_widget_will_show_with_data(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.play_widget_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getPlayLiveDynamicUseCase.get().executeOnBackground() } returns PlayData(
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
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(1000)
        onView(withId(CONTAINER)).check(matches(not(isDisplayed())))
        Thread.sleep(50000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText("Play Widget")))
        onView(withId(TITLE_CONTENT)).check(matches(withText("Channel 1")))
        Thread.sleep(5000)
    }


    @Test
    fun test_given_data_play_and_the_widget_must_take_data_from_play_api_and_the_url_not_valid_the_widget_should_not_visible_into_user(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.get().executeOnBackground() } returns PlayData(
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
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(CONTAINER)).check(matches(not(isDisplayed())))
        Thread.sleep(5000)
    }

    @Test
    fun test_given_data_play_and_the_widget_must_take_data_from_play_api_but_the_return_is_empty_and_expect_the_widget_not_visible_into_user(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.get().executeOnBackground() } returns PlayData(
                listOf()
        )
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(CONTAINER)).check(matches(not(isDisplayed())))
        Thread.sleep(5000)
    }

    @Test
    fun test_given_data_play_and_the_widget_must_take_data_from_play_api_but_the_api_throw_error_and_the_widget_should_not_visible(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.get().executeOnBackground() } throws RuntimeException()
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(CONTAINER)).check(matches(not(isDisplayed())))
        Thread.sleep(5000)
    }

    @Test
    fun test_given_data_play_and_try_update_new_data_but_the_new_data_is_empty_and_expect_the_widget_will_removed(){
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {
            emit(Result.success(""))
        }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            var json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
            var homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            delay(4000)
            json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.play_widget_json)
            homeData = Gson().fromJson(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.get().executeOnBackground() } returns PlayData(
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
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(3000)
        onView(withId(CONTAINER)).check(doesNotExist())
        Thread.sleep(4000)
        onView(withId(CONTAINER)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText("Play Widget")))
        onView(withId(TITLE_CONTENT)).check(matches(withText("Channel 1")))
    }

    @Test
    fun test_given_data_play_and_try_update_into_new_data_expect_widget_show_the_new_data(){
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {
            emit(Result.success(""))
        }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            var json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.play_widget_json)
            var homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            delay(4000)
            json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.play_widget_json)
            homeData = Gson().fromJson(json, HomeData::class.java)
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        coEvery { getPlayLiveDynamicUseCase.get().executeOnBackground() } returns PlayData(
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
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(2000)
        onView(withId(CONTAINER)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText("Play Widget")))
        onView(withId(TITLE_CONTENT)).check(matches(withText("Channel 1")))
        Thread.sleep(2000)
        onView(withId(CONTAINER)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText("Play Widget")))
        onView(withId(TITLE_CONTENT)).check(matches(withText("Channel 2")))
    }
}
