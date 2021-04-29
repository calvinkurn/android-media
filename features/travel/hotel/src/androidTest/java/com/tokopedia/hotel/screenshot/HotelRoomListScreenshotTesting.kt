package com.tokopedia.hotel.screenshot

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.hotel.roomlist.presentation.activity.mock.HotelRoomListResponseConfig
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

class HotelRoomListScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelRoomListActivity> =
            object : IntentsTestRule<HotelRoomListActivity>(HotelRoomListActivity::class.java) {

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()
                    setupGraphqlMockResponse(HotelRoomListResponseConfig())
                }

                override fun getActivityIntent(): Intent {
                    return HotelRoomListActivity.createInstance(context, 11, "Hotel A", "2020-12-06",
                            "2020-12-16", 1, 0, 1, "Region Id", "Hotel A")
                }
            }

    @Test
    fun screenShotRoomList() {
        val activity = activityRule.activity

        Thread.sleep(3000)

        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "top")
        }

        CommonActions.screenShotFullRecyclerView(R.id.recycler_view,
                0,
                getItemCount()- 1,
                "${filePrefix()}-full")
        CommonActions.findViewHolderAndScreenshot(R.id.recycler_view, 0, filePrefix(), "item-room-list")
        activityRule.activity.finishAndRemoveTask()

    }

    private fun filePrefix(): String = "hotel-room-list"

    private fun getItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }
}