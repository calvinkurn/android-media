package com.tokopedia.home.test.widgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.atc_common.domain.usecase.AddToCartOccUseCase
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ReviewDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ReviewUITest : BaseWidgetUiTest(){
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var viewModel: HomeViewModel

    companion object{
        private val CONTAINER = R.id.review_card_bg
        private val LOADING = R.id.loading_review
        private val CLOSE_REVIEW = R.id.ic_close_review
    }

    @Before
    fun setup(){
        every { userSessionInterface.get().isLoggedIn } returns false
    }

    @Test
    fun test_when_data_suggested_not_available_the_review_widget_must_loading(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.get().executeOnBackground()} returns SuggestedProductReview(SuggestedProductReviewResponse())
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            val newList = data.list.toMutableList()
            newList.add(4, ReviewDataModel(channel = DynamicHomeChannel.Channels()))
            emit(data.copy(list = newList))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(allOf(withId(CONTAINER), withEffectiveVisibility(Visibility.VISIBLE))).check(matches(isDisplayed()))
        onView(withId(LOADING)).check(matches(isDisplayed()))
    }

    @Test
    fun test_when_no_data_review_the_review_widget_must_not_show(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            val newList = data.list.toMutableList()
            newList.add(4, ReviewDataModel(channel = DynamicHomeChannel.Channels()))
            emit(data.copy(list = newList))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(CONTAINER)).check(doesNotExist())
    }

    @Test
    fun test_when_data_available_the_review_widget_try_to_click_close_button(){
        val json = GraphqlHelper.loadRawString(context.resources, com.tokopedia.home.test.R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.get().updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.get().executeOnBackground()} returns SuggestedProductReview(
                SuggestedProductReviewResponse(
                        linkURL = "KOSONG",
                        imageUrl = "https://ecs7.tokopedia.net/img/cache/200-square/product-1/2019/1/20/8744407/8744407_1bc03512-8a00-472b-8e14-560f0cb66d45_700_700.jpg"
                )
        )
        coEvery { getHomeUseCase.get().getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            val newList = data.list.toMutableList()
            newList.add(4, ReviewDataModel(
                    suggestedProductReview = SuggestedProductReview(
                            SuggestedProductReviewResponse(
                                    linkURL = "KOSONG"
                            )
                    ),
                    channel = DynamicHomeChannel.Channels()
            ))
            emit(data.copy(list = newList))
        }

        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest()

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(allOf(withId(CONTAINER), withEffectiveVisibility(Visibility.VISIBLE))).check(matches(isDisplayed()))
        onView(withId(CLOSE_REVIEW)).check(matches(isDisplayed()))
        onView(withId(CLOSE_REVIEW)).perform(click())
        Thread.sleep(1000)
        onView(allOf(withId(CONTAINER), withEffectiveVisibility(Visibility.VISIBLE))).check(doesNotExist())
    }
}