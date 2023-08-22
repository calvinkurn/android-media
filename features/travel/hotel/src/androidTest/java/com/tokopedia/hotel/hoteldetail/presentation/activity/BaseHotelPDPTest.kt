package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Context
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.hotel.R
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author: astidhiyaa on 16/09/21.
 */
abstract class BaseHotelPDPTest {
    protected val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val cassavaTestRule = CassavaTestRule(sendValidationResult = false)

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK, null))
    }

    @Test
    fun checkOnHotelDetailTracking() {
        clickOnSeePhoto()
        clickOnSeeAllReview()
        clickSeeRoomButton()

        Assert.assertThat(
            cassavaTestRule.validate(getTrackerFile()),
            hasAllSuccess()
        )
    }

    private fun clickOnSeePhoto() {
        Thread.sleep(1000)
        scrollView()

        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.iv_first_photo_preview)).perform(ViewActions.click())

        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(com.tokopedia.imagepreviewslider.R.id.btn_arrow_back)).perform(ViewActions.click())
    }

    private fun clickOnSeeAllReview() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.tv_hotel_detail_all_reviews)).perform(ViewActions.click())
    }

    private fun clickSeeRoomButton() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_see_room)).perform(ViewActions.click())
    }

    abstract fun getTrackerFile(): String

    abstract fun scrollView()
}