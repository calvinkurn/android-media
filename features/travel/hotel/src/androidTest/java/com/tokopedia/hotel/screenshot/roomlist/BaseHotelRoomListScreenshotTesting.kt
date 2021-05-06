package com.tokopedia.hotel.screenshot.roomlist

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.presentation.activity.HotelRoomListActivity
import com.tokopedia.hotel.roomlist.presentation.activity.mock.HotelRoomListResponseConfig
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupDarkModeTest
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Rule
import org.junit.Test

/**
 * @author by astidhiyaa on 29/04/21
 */
abstract class BaseHotelRoomListScreenshotTesting {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelRoomListActivity> =
            object : IntentsTestRule<HotelRoomListActivity>(HotelRoomListActivity::class.java) {

                override fun beforeActivityLaunched() {
                    super.beforeActivityLaunched()
                    setupDarkModeTest(forceDarkMode())
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

        //SS testing for hotel and guest bottom sheet
        Espresso.onView(ViewMatchers.withId(R.id.hotel_room_and_guest_layout)).perform(ViewActions.click())
        CommonActions.findViewAndScreenShot(R.id.bottom_sheet_hotel_room_and_guest, filePrefix(), "bs-hotel-and-guest")
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())

        //SS testing for calendar
        Espresso.onView(ViewMatchers.withId(R.id.hotel_date_layout)).perform(ViewActions.click())
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "bs-calendar")
        }
        Espresso.onView(ViewMatchers.withId(R.id.bottom_sheet_close)).perform(ViewActions.click())

        //SS testing for room detail
        if(getItemCount() > 0){
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<RoomListViewHolder>(0, ViewActions.click()))

            Thread.sleep(3000)

            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "top-room-detail")
            }

            Thread.sleep(3000)

            Espresso.onView(ViewMatchers.withId(R.id.hotelRoomDetailView)).perform(ViewActions.swipeUp())

            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                CommonActions.takeScreenShotVisibleViewInScreen(activity.window.decorView, filePrefix(), "bottom-room-detail")
            }
        }

        activityRule.activity.finishAndRemoveTask()

    }

    abstract fun filePrefix(): String

    abstract fun forceDarkMode(): Boolean

    private fun getItemCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }
}