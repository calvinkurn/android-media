package com.tokopedia.home.widgets

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import com.tokopedia.home.HomeActivity
import com.tokopedia.home.R
import com.tokopedia.json.HomeJson
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class PlayBannerUITest {
    @Rule
    @JvmField
    var activityRule: ActivityTestRule<HomeActivity> = ActivityTestRule(HomeActivity::class.java)

    private lateinit var mockWebServer: MockWebServer

    @Before
    @Throws(Exception::class)
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    @Before
    @After
    fun tearDown(){
        mockWebServer.shutdown()
    }

    @Test
    fun testNoSkeletonDataFromHome(){
        mockWebServer.enqueue(MockResponse().setBody(HomeJson.resultNoSkeleton))
//        activityRule?.launchActivity(Intent())
        Thread.sleep(500)
        onView(withId(R.id.bannerPlay)).check(ViewAssertions.matches(not(isDisplayed())))
    }

    @Test
    fun testHappyPathPlayBannerUI(){
        assert(true)
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
}