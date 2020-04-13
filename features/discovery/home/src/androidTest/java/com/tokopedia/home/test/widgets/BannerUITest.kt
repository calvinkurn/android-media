package com.tokopedia.home.test.widgets

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeDataMapper
import com.tokopedia.home.beranda.data.mapper.factory.HomeVisitableFactoryImpl
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.HomeData
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
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class BannerUITest {

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

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup(){
        every { userSessionInterface.isLoggedIn } returns false
    }

    @Test
    fun testDataFromHome(){
        val json = HomeJson.resultNoSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            Log.d("testLukas", "Flow emit masuk")
        }
        viewModel = reInitViewModel()
        Log.d("testLukas", viewModel.toString())
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(5000)
        Espresso.onView(withId(R.id.circular_view_pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
    @Test
    fun testUpdateDataFromHome(){
        val json = HomeJson.resultNoSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        val json2 = HomeJson.resultWithSkeleton
        val homeData2 = Gson().fromJson(json2, HomeData::class.java)
        coEvery { getHomeUseCase.updateHomeData() } returns flow {  }
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
            delay(4000)
            emit(homeDataMapper.mapToHomeViewModel(homeData2, false))
        }
        viewModel = reInitViewModel()
        Log.d("testLukas", viewModel.toString())
        Log.d("testLukas", "HomeData: $homeData")
        val homeFragment = HomeFragmentTest(createViewModelFactory(viewModel))

        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.circular_view_pager)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(allOf(withId(R.id.image_banner_homepage), withTagValue(`is`(homeData.banner.slides!!.first().imageUrl)))).check(ViewAssertions.matches(isDisplayed()))
        Thread.sleep(4000)
        // check banner updated or not
        Espresso.onView(allOf(withId(R.id.image_banner_homepage), withTagValue(`is`(homeData2.banner.slides!!.first().imageUrl as Any)))).check(ViewAssertions.matches(isDisplayed()))
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