package com.tokopedia.home.test.widgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BannerUITest : BaseWidgetUiTest(){

    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup(){
        every { userSessionInterface.get().isLoggedIn } returns false
    }

    @Test
    fun test_given_data_from_api_and_expect_the_widget_is_displayed(){
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
        Espresso.onView(withId(R.id.circular_view_pager)).check(ViewAssertions.matches(isDisplayed()))
    }
    @Test
    fun test_given_data_from_home_and_try_update_with_new_data_expect_the_widget_will_render_new_data(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        val json2 = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.play_widget_json)
        val homeData2 = Gson().fromJson(json2, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            delay(4000)
            emit(homeDataMapper.mapToHomeViewModel(homeData2, false))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.circular_view_pager)).check(ViewAssertions.matches(isDisplayed()))
        Espresso.onView(allOf(withId(R.id.image_banner_homepage), withTagValue(`is`(homeData.banner.slides!!.first().imageUrl)))).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(4000)
        // check banner updated or not
        Espresso.onView(allOf(withId(R.id.image_banner_homepage), withTagValue(`is`(homeData2.banner.slides!!.first().imageUrl as Any)))).check(ViewAssertions.matches(isDisplayed()))
    }
}