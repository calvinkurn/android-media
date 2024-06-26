package com.tokopedia.hotel.roomlist.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.booking.presentation.activity.HotelBookingActivity
import com.tokopedia.hotel.roomlist.presentation.activity.mock.HotelRoomListResponseConfig
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test


/**
 * @author by jessica on 17/09/20
 */
class HotelRoomListActivityTest {

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

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Test
    fun checkOnRoomListTrackingEvent() {
        Intents.intending(IntentMatchers.hasComponent(HotelBookingActivity::class.java.name)).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        login()
        clickOnSeePhoto()
        clickOnChooseRoomInRoomListFragment()
        clickOnRoomViewHolder()
        assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_HOTEL_ROOM_LIST), hasAllSuccess())
    }

    private fun clickOnSeePhoto() {
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RoomListViewHolder>(0, CommonActions.clickChildViewWithId(R.id.image_banner)))

        Thread.sleep(3000)
        onView(withId(com.tokopedia.imagepreviewslider.R.id.btn_arrow_back)).perform(click())

        Thread.sleep(3000)
    }

    private fun clickOnChooseRoomInRoomListFragment() {
        Thread.sleep(3000)
        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RoomListViewHolder>(0, CommonActions.clickChildViewWithId(R.id.choose_room_button)))
    }

    private fun clickOnRoomViewHolder() {
        Thread.sleep(3000)

        if (getRoomListCount() > 0) {
            onView(withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<RoomListViewHolder>(0, click()))

            Thread.sleep(3000)
            onView(withId(R.id.room_detail_images)).perform(click())

            Thread.sleep(3000)
            onView(withId(com.tokopedia.imagepreviewslider.R.id.btn_arrow_back)).perform(click())

            Thread.sleep(3000)
            onView(withId(R.id.room_detail_button)).perform(click())
        }
    }

    private fun getRoomListCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun login() {
        Thread.sleep(3000)
        InstrumentationAuthHelper.userSession {
            /** content.prod.automation2+frontendtest@tokopedia.com */
            userId = "17211048"
            shopId = "3533069"
        }
    }

    companion object {
        const val ANALYTIC_VALIDATOR_QUERY_HOTEL_ROOM_LIST = "tracker/travel/hotel/hotel_room_list.json"
    }
}
