package com.tokopedia.home.test.widgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.model.HomeWidget
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.DynamicHomeChannel
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReview
import com.tokopedia.home.beranda.domain.model.review.SuggestedProductReviewResponse
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.PopularKeywordListDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.R
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeFragmentTest
import com.tokopedia.home.test.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PopularUITest {
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

    private val context = InstrumentationRegistry.getInstrumentation().context
    private lateinit var viewModel: HomeViewModel

    companion object{
        private val TITLE = R.id.channel_title
        private val LOADING = R.id.loading_popular
        private val RELOAD = R.id.tv_reload
    }

    @Before
    fun setup(){
        every { userSessionInterface.isLoggedIn } returns false
    }

    @Test
    fun test_when_detail_data_not_available_the_widget_must_loading(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery{getHomeReviewSuggestedUseCase.executeOnBackground()} returns SuggestedProductReview(SuggestedProductReviewResponse())
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            val newList = data.list.toMutableList()
            newList.add(4, PopularKeywordListDataModel(
                    channel = DynamicHomeChannel.Channels(layout = "popular_keyword", name = "Popular Keyword", header = DynamicHomeChannel.Header(name = "Lagi trending, nih!"))
            ))
            emit(data.copy(list = newList))
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

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
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText(CoreMatchers.containsString("Lagi trending, nih!"))))
    }

    @Test
    fun test_when_no_data_available_the_widget_not_showing(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.home_empty_dynamic_channel_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(TITLE)).check(doesNotExist())
    }


    @Test
    fun test_when_data_available_and_try_to_update_widget_data(){
        val json = GraphqlHelper.loadRawString(context.resources, R.raw.popular_keyword_json)
        val popularData = GraphqlHelper.loadRawString(context.resources, R.raw.get_popular_keyword_query_data_json)
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getPopularKeywordUseCase.executeOnBackground() } coAnswers  {
            delay(2000)
            Gson().fromJson<HomeWidget.PopularKeywordQuery>(popularData, HomeWidget.PopularKeywordQuery::class.java)
        }
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            val data = homeDataMapper.mapToHomeViewModel(homeData, false)
            emit(data)
        }
        viewModel = reInitViewModel()
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        onView(withId(TITLE)).check(matches(isDisplayed()))
        onView(withId(TITLE)).check(matches(withText(CoreMatchers.containsString("Lagi trending, nih!"))))

        onView(withId(RELOAD)).check(matches(isDisplayed()))
        onView(withId(RELOAD)).perform(click())
        Thread.sleep(1000)
        onView(withId(LOADING)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(LOADING)).check(matches(not(isDisplayed())))
    }

    private fun <T : ViewModel> createViewModelFactory(viewModel: T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(viewModelClass: Class<T>): T {
                if (viewModelClass.isAssignableFrom(viewModel.javaClass)) {
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