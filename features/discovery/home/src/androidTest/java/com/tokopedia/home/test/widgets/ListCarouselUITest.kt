package com.tokopedia.home.test.widgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class ListCarouselUITest : BaseWidgetUiTest(){
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var viewModel: HomeViewModel

    companion object{
        private val CONTAINER = R.id.list_carousel_view
        private val TITLE = R.id.list_carousel_title
        private val RECYCLERVIEW = R.id.recycleList
        private val CLOSE_CHANNEL = R.id.buy_again_close_image_view
    }

    @Before
    fun setup(){
        every { userSessionInterface.get().isLoggedIn } returns false
    }

    @Test
    fun test_when_data_list_carousel_available_the_review_widget_show(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.list_carousel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.get().executeOnBackground()} returns SuggestedProductReview(SuggestedProductReviewResponse())
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(CONTAINER), ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(TITLE)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(CLOSE_CHANNEL)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun test_when_data_list_carousel_available_the_review_widget_show_try_close_channel(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.list_carousel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.get().executeOnBackground()} returns SuggestedProductReview(SuggestedProductReviewResponse())
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(CONTAINER), ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(TITLE)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(CLOSE_CHANNEL)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(CLOSE_CHANNEL)).perform(click())
        Thread.sleep(2000)
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(CONTAINER), ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(ViewAssertions.doesNotExist())
    }

    @Test
    fun test_when_data_list_carousel_available_the_review_widget_show_with_out_close_btn(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.list_carousel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.get().executeOnBackground()} returns SuggestedProductReview(SuggestedProductReviewResponse())
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        Espresso.onView(CoreMatchers.allOf(ViewMatchers.withId(CONTAINER), ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(TITLE)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(CLOSE_CHANNEL)).check(ViewAssertions.matches(not(ViewMatchers.isDisplayed())))
    }
}