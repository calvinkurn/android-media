package com.tokopedia.home.test.widgets

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
import com.tokopedia.home.beranda.data.usecase.HomeUseCase
import com.tokopedia.home.beranda.domain.interactor.*
import com.tokopedia.home.beranda.domain.model.HomeData
import com.tokopedia.home.beranda.presentation.view.fragment.HomeFragment
import com.tokopedia.home.beranda.presentation.viewModel.HomeViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.json.HomeJson
import com.tokopedia.home.test.rules.TestDispatcherProvider
import com.tokopedia.stickylogin.domain.usecase.coroutine.StickyLoginUseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    private val homeDataMapper = HomeDataMapper(InstrumentationRegistry.getInstrumentation().context, HomeVisitableFactoryImpl(userSessionInterface), mockk(relaxed = true))


    private val viewModel: HomeViewModel = HomeViewModel(
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
            userSession = userSessionInterface
    )

    @Before
    fun setup(){
        every { userSessionInterface.isLoggedIn } returns false
    }

    @Test
    fun testNoSkeletonDataFromHome(){
        val homeFragment = HomeFragment()
        val json = HomeJson.resultNoSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        homeFragment.viewModelFactory = createViewModelFactory(viewModel)
        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(2000)
        onView(withId(R.id.play_frame_layout)).check(doesNotExist())
    }

    @Test
    fun testHappyPathPlayBannerUI(){
        assert(true)
        val homeFragment = HomeFragment()
        val json = HomeJson.resultWithSkeleton
        val homeData = Gson().fromJson<HomeData>(json, HomeData::class.java)
        coEvery { getHomeUseCase.getHomeData() } returns flow {
            emit(homeDataMapper.mapToHomeViewModel(homeData, false))
        }
        homeFragment.viewModelFactory = createViewModelFactory(viewModel)
        activityRule.activity.setupFragment(homeFragment)
        Thread.sleep(2000)
        onView(withId(R.id.play_frame_layout)).check(matches(not(isDisplayed())))
//        onView(withId(R.id.title)).check(matches(isDisplayed()))
//        onView(withId(R.id.title)).check(matches(withText("Play Widget")))
        Thread.sleep(5000)
    }


    @Test
    fun testNotValidImageUrlFromBackend(){

    }

    @Test
    fun testNoReturnDataPlayFromBackend(){

    }

    @Test
    fun testErrorDataPlayFromBackend(){

    }

    @Test
    fun testUpdateBannerAndDataFromHomeEmpty(){

    }

    @Test
    fun testUpdateBannerFromPlayDataDifferent(){

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
}