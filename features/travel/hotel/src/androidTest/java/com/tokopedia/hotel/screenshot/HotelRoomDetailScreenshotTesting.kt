package com.tokopedia.hotel.screenshot

import android.content.Intent
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.appbar.AppBarLayout
import com.tokopedia.hotel.roomdetail.presentation.activity.HotelRoomDetailActivity
import org.junit.Rule
import org.junit.Test
import com.tokopedia.hotel.R

class HotelRoomDetailScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    lateinit var appBar: AppBarLayout
    lateinit var roomDetail: NestedScrollView

    @get:Rule
    var activityRule: IntentsTestRule<HotelRoomDetailActivity> =
            object : IntentsTestRule<HotelRoomDetailActivity>(HotelRoomDetailActivity::class.java) {

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()
                }

                override fun getActivityIntent(): Intent {
                    return HotelRoomDetailActivity.getCallingIntent(context, "1619672690985", 0)
                }
            }

    @Test
    fun screenShot(){

        activityRule.activity.finishAndRemoveTask()
    }

    private fun scrollToMiddle(){

    }

    private fun filePrefix(): String = "hotel-room-detail"
}