package com.tokopedia.hotel.roomlist.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.hotel.R
import com.tokopedia.hotel.roomlist.presentation.activity.mock.HotelRoomListResponseConfig
import com.tokopedia.hotel.roomlist.presentation.adapter.viewholder.RoomListViewHolder
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @author by jessica on 17/09/20
 */
class HotelRoomListActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

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

    @Before
    fun setUp() {
        gtmLogDBSource.deleteAll().subscribe()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun checkOnRoomListTrackingEvent() {
        Thread.sleep(3000)
        clickOnSeePhoto()
        clickOnRoomViewHolder()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_HOTEL_ROOM_LIST),
                hasAllSuccess())
    }

    private fun clickOnSeePhoto() {
        Thread.sleep(3000)

        onView(withId(R.id.recycler_view)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RoomListViewHolder>(0, CommonActions.clickChildViewWithId(R.id.image_banner)))

        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.btn_arrow_back)).perform(click())
    }

    private fun clickOnRoomViewHolder() {
        Thread.sleep(3000)

        if (getRoomListCount() > 0) {
            Espresso.onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<RoomListViewHolder>(0, ViewActions.click()))
        }
    }

    private fun getRoomListCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.recycler_view) as RecyclerView
        return recyclerView.adapter?.itemCount ?: 0
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        const val ANALYTIC_VALIDATOR_QUERY_HOTEL_ROOM_LIST = "tracker/travel/hotel/hotel_room_list.json"
    }
}