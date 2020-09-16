package com.tokopedia.hotel.hoteldetail.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.hotel.hoteldetail.presentation.activity.mock.HotelDetailMockResponseConfig
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * @author by jessica on 16/09/20
 */
class HotelDetailActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: IntentsTestRule<HotelDetailActivity> = object : IntentsTestRule<HotelDetailActivity>(HotelDetailActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelDetailMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelDetailActivity.getCallingIntent(context, "2023-10-10",
            "2023-10-11", 11, 1, 1, "",
                    "", true, "")
        }
    }

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }


    @Test
    fun checkOnHotelDetailTracking() {

    }

}