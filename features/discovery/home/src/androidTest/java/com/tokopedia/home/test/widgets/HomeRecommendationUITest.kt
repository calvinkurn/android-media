package com.tokopedia.home.test.widgets

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.tokopedia.home.R
import com.tokopedia.home.beranda.data.mapper.HomeRecommendationMapper
import com.tokopedia.home.beranda.domain.gql.feed.HomeFeedContentGqlResponse
import com.tokopedia.home.beranda.domain.interactor.GetHomeRecommendationUseCase
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation.HomeRecommendationDataModel
import com.tokopedia.home.beranda.presentation.viewModel.HomeRecommendationViewModel
import com.tokopedia.home.test.activity.HomeActivityTest
import com.tokopedia.home.test.fragment.HomeRecommendationFragmentTest
import com.tokopedia.home.test.json.HomeRecommendationJson
import com.tokopedia.home.test.matchers.CustomAssertions.Companion.hasItemCount
import com.tokopedia.test.application.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeoutException

@ExperimentalCoroutinesApi
class HomeRecommendationUITest{
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(HomeActivityTest::class.java, true, true)

    @Rule
    @JvmField
    val taskExecutorRule = InstantTaskExecutorRule()

    private val getHomeRecommendationUseCase = mockk<GetHomeRecommendationUseCase>(relaxed = true)
    private val topAdsImageViewUseCase = mockk<TopAdsImageViewUseCase>()
    private lateinit var viewModel: HomeRecommendationViewModel

    @Test
    fun testSuccessDataFromBackend(){
        Log.d("testHomeRecom", "start test")
        val json = HomeRecommendationJson.successData
        val data = Gson().fromJson<HomeFeedContentGqlResponse>(json, HomeFeedContentGqlResponse::class.java)
        val mockData = HomeRecommendationMapper().mapToHomeRecommendationDataModel(data, "", 1)
        Log.d("testHomeRecom", mockData.toString())
        coEvery { getHomeRecommendationUseCase.executeOnBackground() } returns mockData
        Log.d("testHomeRecom", "success")
        viewModel = HomeRecommendationViewModel(getHomeRecommendationUseCase, topAdsImageViewUseCase, CoroutineTestDispatchersProvider)
        val homeRecommendationTest = HomeRecommendationFragmentTest(createViewModelFactory(viewModel))
        activityRule.activity.setupFragment(homeRecommendationTest)
        Log.d("testHomeRecom", "Activity set fragment")
        Thread.sleep(5000)
        //check view is rendered
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).check(hasItemCount(mockData.homeRecommendations.size))
    }

    @Test
    fun testErrorDataFromBackend(){
        Log.d("testHomeRecom", "start test")
        coEvery { getHomeRecommendationUseCase.executeOnBackground() } throws TimeoutException()
        Log.d("testHomeRecom", "success")
        viewModel = HomeRecommendationViewModel(getHomeRecommendationUseCase, topAdsImageViewUseCase, CoroutineTestDispatchersProvider)
        val homeRecommendationTest = HomeRecommendationFragmentTest(createViewModelFactory(viewModel))
        activityRule.activity.setupFragment(homeRecommendationTest)
        Log.d("testHomeRecom", "Activity set fragment")
        Espresso.onView(ViewMatchers.withId(R.id.loading)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(5000)
        //check view is rendered
        Espresso.onView(ViewMatchers.withId(R.id.global_unify_error)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testEmptyDataFromBackend(){
        Log.d("testHomeRecom", "start test")
        coEvery { getHomeRecommendationUseCase.executeOnBackground() } coAnswers {
            delay(2000)
            HomeRecommendationDataModel()
        }
        Log.d("testHomeRecom", "success")
        viewModel = HomeRecommendationViewModel(getHomeRecommendationUseCase, topAdsImageViewUseCase, CoroutineTestDispatchersProvider)
        val homeRecommendationTest = HomeRecommendationFragmentTest(createViewModelFactory(viewModel))
        activityRule.activity.setupFragment(homeRecommendationTest)
        Log.d("testHomeRecom", "Activity set fragment")
        Espresso.onView(ViewMatchers.withId(R.id.loading)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(5000)
        //check view is rendered
        Espresso.onView(ViewMatchers.withId(R.id.main_retry)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun testImpressionTopAds(){
        Log.d("testHomeRecom", "start test")
        val capturingUrl = slot<String>()
        var impressionUrl = ""
        val json = HomeRecommendationJson.successWithOneData
        val data = Gson().fromJson<HomeFeedContentGqlResponse>(json, HomeFeedContentGqlResponse::class.java)
        val mockData = HomeRecommendationMapper().mapToHomeRecommendationDataModel(data, "", 1)
        var productsImpression = 0

        Log.d("testHomeRecom", mockData.toString())
        coEvery { getHomeRecommendationUseCase.executeOnBackground() } returns mockData
        Log.d("testHomeRecom", "success")
        viewModel = HomeRecommendationViewModel(getHomeRecommendationUseCase, topAdsImageViewUseCase, CoroutineTestDispatchersProvider)
        val homeRecommendationTest = HomeRecommendationFragmentTest(createViewModelFactory(viewModel))
        activityRule.activity.setupFragment(homeRecommendationTest)
        Log.d("testHomeRecom", "Activity set fragment")
        Thread.sleep(1000)
        //check view is rendered
        Assert.assertTrue(productsImpression == mockData.homeRecommendations.size)
        Assert.assertTrue(impressionUrl == data.homeRecommendation.recommendationProduct.product.first().trackerImageUrl)
    }

    @Test
    fun testImpressionAndClickTopAds(){
        Log.d("testHomeRecom", "start test")
        val json = HomeRecommendationJson.successWithOneData
        val capturingUrl = slot<String>()
        var clickUrl = ""
        val data = Gson().fromJson<HomeFeedContentGqlResponse>(json, HomeFeedContentGqlResponse::class.java)
        val mockData = HomeRecommendationMapper().mapToHomeRecommendationDataModel(data, "", 1)

        var productsImpression = 0


        Log.d("testHomeRecom", mockData.toString())
        coEvery { getHomeRecommendationUseCase.executeOnBackground() } returns mockData
        Log.d("testHomeRecom", "success")
        viewModel = HomeRecommendationViewModel(getHomeRecommendationUseCase, topAdsImageViewUseCase, CoroutineTestDispatchersProvider)
        val homeRecommendationTest = HomeRecommendationFragmentTest(createViewModelFactory(viewModel))
        activityRule.activity.setupFragment(homeRecommendationTest)
        Log.d("testHomeRecom", "Activity set fragment")
        Thread.sleep(1000)
        //check view is rendered
        Assert.assertTrue(productsImpression == mockData.homeRecommendations.size)
        Assert.assertTrue(clickUrl == data.homeRecommendation.recommendationProduct.product.first().trackerImageUrl)
        Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Assert.assertTrue(clickUrl == data.homeRecommendation.recommendationProduct.product.first().clickUrl)
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
}