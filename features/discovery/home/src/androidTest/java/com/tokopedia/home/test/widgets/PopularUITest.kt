package com.tokopedia.home.test.widgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.R
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PopularUITest : BaseWidgetUiTest(){
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var viewModel: HomeViewModel

    companion object{
        private val TITLE = R.id.channel_title
        private val LOADING = R.id.loading_popular
        private val RELOAD = R.id.tv_reload
    }

    @Test
    fun test_when_detail_data_not_available_the_widget_must_loading(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.get().executeOnBackground()} returns SuggestedProductReview(SuggestedProductReviewResponse())
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            val newList = data.list.toMutableList()
            newList.add(4, PopularKeywordListDataModel(
                    channel = DynamicHomeChannel.Channels(layout = "popular_keyword", name = "Popular Keyword", header = DynamicHomeChannel.Header(name = "Lagi trending, nih!"))
            ))
            emit(data.copy(list = newList))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)

        onView(withId(LOADING)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText(CoreMatchers.containsString("Lagi trending, nih!"))))
    }

    @Test
    fun test_when_data_fully_complete_the_widget_must_show_with_list(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.popular_keyword_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText(CoreMatchers.containsString("Lagi trending, nih!"))))
    }

    @Test
    fun test_when_no_data_available_the_widget_not_showing(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(TITLE)).check(doesNotExist())
    }


    @Test
    fun test_when_data_available_and_try_to_update_widget_data(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.popular_keyword_json)
        val popularData = GraphqlHelper.loadRawString(context.resources, R.raw.get_popular_keyword_query_data_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getPopularKeywordUseCase.get().executeOnBackground() } coAnswers  {
            delay(2000)
            Gson().fromJson<HomeWidget.PopularKeywordQuery>(popularData, HomeWidget.PopularKeywordQuery::class.java)
        }
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText(CoreMatchers.containsString("Lagi trending, nih!"))))

        onView(withId(RELOAD)).check(matches(isDisplayed()))
        onView(withId(RELOAD)).perform(click())
        Thread.sleep(1000)
        onView(withId(LOADING)).check(matches(isDisplayed()))
    }

}