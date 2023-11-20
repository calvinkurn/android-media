package com.tokopedia.hotel.homepage.presentation.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.util.HumanReadables
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.carousel.CarouselUnify
import com.tokopedia.hotel.R
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_NAME
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_ID
import com.tokopedia.hotel.destination.view.activity.HotelDestinationActivity.Companion.HOTEL_DESTINATION_SEARCH_TYPE
import com.tokopedia.hotel.homepage.presentation.activity.mock.HotelHomepageMockResponseConfig
import com.tokopedia.hotel.homepage.presentation.adapter.viewholder.HotelLastSearchViewHolder
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher
import org.hamcrest.core.AllOf
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * @author by jessica on 04/08/20
 */

class HotelHomepageActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    var activityRule: IntentsTestRule<HotelHomepageActivity> = object : IntentsTestRule<HotelHomepageActivity>(HotelHomepageActivity::class.java) {

        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HotelHomepageMockResponseConfig())
        }

        override fun getActivityIntent(): Intent {
            return HotelHomepageActivity.getCallingIntent(context)
        }
    }

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setUp() {
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun createDummyDestination(): Instrumentation.ActivityResult {
        val resultData = Intent()
        resultData.putExtra(HOTEL_DESTINATION_NAME, "Jakarta")
        resultData.putExtra(HOTEL_DESTINATION_SEARCH_TYPE, "regionOrigin")
        resultData.putExtra(HOTEL_DESTINATION_SEARCH_ID, "835")
        return Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
    }

    @Test
    fun testHomeLayout() {
        clickOnChangeDestination()
        changeDate()
        modifyGuestAndRoomCount()
        clickSubmitButton()

        slidePromoBanner()

        clickRecentSearchWidget()

        Assert.assertThat(cassavaRule.validate(ANALYTIC_VALIDATOR_QUERY_HOTEL_HOMEPAGE), hasAllSuccess())
    }

    private fun clickSubmitButton() {
        onView(withId(R.id.btn_hotel_homepage_search)).check(matches(isDisplayed())).perform(ViewActions.click())
        Thread.sleep(3000)
    }

    private fun clickOnChangeDestination() {
        Thread.sleep(4000)
        intending(hasComponent(HotelDestinationActivity::class.java.name)).respondWith(createDummyDestination())
        onView(withTagStringValue(R.id.tv_hotel_homepage_destination.toString())).perform(ViewActions.click())
        intended(AllOf.allOf(hasComponent(HotelDestinationActivity::class.java.name)))

        onView(withTagStringValue(R.id.tv_hotel_homepage_destination.toString())).check(matches(withText("Jakarta")))

        Thread.sleep(2000)
    }

    private fun modifyGuestAndRoomCount() {
        Thread.sleep(3000)
        onView(withTagStringValue(R.id.tv_hotel_homepage_guest_info.toString())).perform(ViewActions.click())
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(com.tokopedia.common.travel.R.id.image_button_plus),
                isDescendantOfA(AllOf.allOf(withId(R.id.spv_hotel_room),
                        hasDescendant(AllOf.allOf(withId(com.tokopedia.common.travel.R.id.textview_title), withText("Kamar")))))))
                .perform(click())
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(com.tokopedia.common.travel.R.id.image_button_plus),
                isDescendantOfA(AllOf.allOf(withId(R.id.spv_hotel_adult),
                        hasDescendant(AllOf.allOf(withId(com.tokopedia.common.travel.R.id.textview_title), withText("Tamu")))))))
                .perform(click())
                .check(matches(isDisplayed()))
        Thread.sleep(1000)

        onView(AllOf.allOf(withId(R.id.btn_hotel_save_guest))).perform(ViewActions.click())
        Thread.sleep(2000)
    }

    private fun slidePromoBanner() {
        Thread.sleep(2000)
        if (getBannerItemCount() > 0) {
            onView(withId(R.id.banner_hotel_homepage_promo)).perform(nestedScrollTo())
            onView(withId(R.id.banner_hotel_homepage_promo)).check(matches(isDisplayed()))
            onView(withId(R.id.banner_hotel_homepage_promo)).perform(click())
            Thread.sleep(1000)
        } else {
            Thread.sleep(1000)
            onView(withId(R.id.banner_hotel_homepage_promo)).check(matches(CoreMatchers.not(isDisplayed())))
        }
    }

    private fun getBannerItemCount(): Int {
        val carouselUnify: CarouselUnify = activityRule.activity.findViewById(R.id.banner_hotel_homepage_promo)
        return carouselUnify.indicatorCount.toInt()
    }

    private fun getLastSearchCount(): Int {
        val recyclerView: RecyclerView = activityRule.activity.findViewById(R.id.rv_hotel_homepage_last_search)
        return recyclerView.adapter?.itemCount ?: 0
    }

    private fun clickRecentSearchWidget() {
        Thread.sleep(4000)

        if (getLastSearchCount() > 0) {
            onView(withId(R.id.rv_hotel_homepage_last_search)).perform(RecyclerViewActions
                    .actionOnItemAtPosition<HotelLastSearchViewHolder>(0, click()))
        }
    }

    private fun changeDate() {
        Thread.sleep(2000)
        onView(withTagStringValue(R.id.tv_hotel_homepage_checkin_date.toString())).perform(click())

        Thread.sleep(5000)

        // select static date - 8
        onView(CommonMatcher.getElementFromMatchAtPosition(withText("8"), 1)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withText("8"), 1)).perform(click())
        Thread.sleep(1000)

        // select static date - 9
        onView(CommonMatcher.getElementFromMatchAtPosition(withText("9"), 1)).check(matches(isDisplayed()))
        Thread.sleep(1000)
        onView(CommonMatcher.getElementFromMatchAtPosition(withText("9"), 1)).perform(click())
        Thread.sleep(1000)
    }

    private fun nestedScrollTo(): ViewAction? {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return CoreMatchers.allOf(
                        isDescendantOfA(isAssignableFrom(NestedScrollView::class.java)),
                        withEffectiveVisibility(Visibility.VISIBLE))
            }

            override fun getDescription(): String {
                return "View is not NestedScrollView"
            }

            override fun perform(uiController: UiController, view: View) {
                try {
                    val nestedScrollView = findFirstParentLayoutOfClass(view, NestedScrollView::class.java) as NestedScrollView?
                    if (nestedScrollView != null) {
                        nestedScrollView.scrollTo(0, view.top + view.measuredHeight + 250)
                    } else {
                        throw java.lang.Exception("Unable to find NestedScrollView parent.")
                    }
                } catch (e: java.lang.Exception) {
                    throw PerformException.Builder()
                            .withActionDescription(this.description)
                            .withViewDescription(HumanReadables.describe(view))
                            .withCause(e)
                            .build()
                }
                uiController.loopMainThreadUntilIdle()
            }
        }
    }

    private fun findFirstParentLayoutOfClass(view: View, parentClass: Class<out View>): View? {
        var parent: ViewParent = FrameLayout(view.context)
        var incrementView: ViewParent? = null
        var i = 0
        while (parent.javaClass != parentClass) {
            parent = if (i == 0) {
                findParent(view)
            } else {
                findParent(incrementView)
            }
            incrementView = parent
            i++
        }
        return parent as View
    }

    private fun findParent(view: View): ViewParent = view.parent
    private fun findParent(view: ViewParent?): ViewParent = view!!.parent

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_HOTEL_HOMEPAGE = "tracker/travel/hotel/hotel_homepage.json"
    }
}
