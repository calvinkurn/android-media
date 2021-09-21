package com.tokopedia.entertainment

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventGridEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventLocationEventViewHolder
import com.tokopedia.entertainment.home.fragment.NavEventHomeFragment
import com.tokopedia.entertainment.navigation.EventNavigationActivity
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.util.ResourcePathUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationEventActivityTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule = ActivityTestRule(EventNavigationActivity::class.java, false, false)


    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_EVENT_CHILD,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_HOME),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_QUERY_PDP_V3,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_PDP),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_QUERY_CONTENT,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_PDP_CONTENT),
                    MockModelConfig.FIND_BY_CONTAINS)

            addMockResponse(
                    KEY_TRAVEL_HOLIDAY,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_TRAVEL_HOLIDAY),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventNavigationActivity::class.java)

        LocalCacheHandler(context, NavEventHomeFragment.PREFERENCES_NAME).also {
            it.putBoolean(NavEventHomeFragment.SHOW_COACH_MARK_KEY, true)
            it.applyEditor()
        }

        activityRule.launchActivity(intent)

        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateNavigationHomePDPEvent() {
        Thread.sleep(10000)
        //HOME

        impression_banner()

        click_banner()

        click_category_icon()

        impression_carousel_product_event()
        click_carousel_product_event()

        impression_taman_bermain_lokal()
        click_product_taman_bermain_lokal()
        click_see_all_taman_bermain_lokal()

        impression_location_event()
        click_location_event()

        impression_taman_bermain_mancanegara()
        click_product_taman_bermain_mancanegara()
        click_see_all_taman_bermain_mancanegara()

        impression_aktivitas()
        click_product_aktivitas()
        click_see_all_aktivitas()

        impression_festival()
        click_product_festival()
        click_see_all_festival()

        impression_event()
        click_product_event()
        click_see_all_event()

        impression_aktivitas_anak()
        click_product_aktivitas_anak()
        click_see_all_aktivitas_anak()

        //PDP

        click_lanjutkan_ticket()
        click_check_ticket()

        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_HOME_VALIDATOR_QUERY), hasAllSuccess())
        ViewMatchers.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY), hasAllSuccess())

    }

    fun impression_banner() {
        Espresso.onView(ViewMatchers.withId(R.id.banner_recyclerview)).perform(RecyclerViewActions.scrollToPosition<BannerViewPagerAdapter.BannerViewHolder>(1))
    }

    fun click_banner() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.banner_recyclerview)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<BannerViewPagerAdapter.BannerViewHolder>(0, ViewActions.click()))
    }

    fun click_category_icon() {
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(1))
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.ent_recycle_view_category)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryEventViewHolder>(0, ViewActions.click()))
        Thread.sleep(2000)
    }

    fun impression_carousel_product_event() {
        Thread.sleep(1000)
        Espresso.onView(ViewMatchers.withId(R.id.ent_recycle_view_carousel)).perform(RecyclerViewActions.scrollToPosition<EventCarouselEventViewHolder>(2))
    }

    fun click_carousel_product_event() {
        Thread.sleep(3000)
        Espresso.onView(ViewMatchers.withId(R.id.ent_recycle_view_carousel)).perform(RecyclerViewActions.actionOnItemAtPosition<EventCarouselEventViewHolder>(2, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
    }

    fun impression_taman_bermain_lokal() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(3))
    }

    fun click_product_taman_bermain_lokal() {
        Thread.sleep(3000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
        Thread.sleep(3000)
        val viewInteractions = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractions.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(3))    }

    fun click_see_all_taman_bermain_lokal() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.btn_see_all), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.ent_grid_layout)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Thread.sleep(3000)
    }

    fun impression_location_event() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(4))
        Thread.sleep(2000)
        Espresso.onView(ViewMatchers.withId(R.id.ent_recycle_view_location)).perform(RecyclerViewActions.scrollToPosition<EventLocationEventViewHolder>(3))
    }

    fun click_location_event() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_location), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventLocationEventViewHolder>(0, ViewActions.click()))
        Thread.sleep(5000)
    }

    fun impression_taman_bermain_mancanegara() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(5))
        Thread.sleep(2000)
    }

    fun click_product_taman_bermain_mancanegara() {
        Thread.sleep(3000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
        Thread.sleep(3000)
        val viewInteractions = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractions.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(5))
    }

    fun click_see_all_taman_bermain_mancanegara() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.btn_see_all), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.ent_grid_layout)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Thread.sleep(3000)
    }

    fun impression_aktivitas() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(6))
        Thread.sleep(2000)
    }

    fun click_product_aktivitas() {
        Thread.sleep(3000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Aktivitas")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
        Thread.sleep(3000)
        val viewInteractions = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractions.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(6))
    }

    fun click_see_all_aktivitas() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.btn_see_all), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.ent_grid_layout)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Aktivitas")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Thread.sleep(3000)
    }

    fun impression_festival() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(7))
        Thread.sleep(2000)
    }

    fun click_product_festival() {
        Thread.sleep(3000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Festival")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
        Thread.sleep(3000)
        val viewInteractios = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractios.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(7))
    }

    fun click_see_all_festival() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.btn_see_all), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.ent_grid_layout)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Festival")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Thread.sleep(3000)
    }


    fun impression_event() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(8))
        Thread.sleep(2000)
    }

    fun click_product_event() {
        Thread.sleep(3000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Events")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
        Thread.sleep(3000)
        val viewInteractions = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractions.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(8))
    }

    fun click_see_all_event() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.btn_see_all), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.ent_grid_layout)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Events")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Thread.sleep(3000)
    }

    fun impression_aktivitas_anak() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(9))
        Thread.sleep(2000)
    }

    fun click_product_aktivitas_anak() {
        Thread.sleep(3000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Aktivitas Anak")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(0, ViewActions.click()))
        Thread.sleep(3000)
        Espresso.onView(isRoot()).perform(ViewActions.pressBack())
        Thread.sleep(3000)
        val viewInteractions = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.recycler_view_home), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.event_home_fragment)),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractions.perform(RecyclerViewActions.scrollToPosition<AbstractViewHolder<*>>(9))
    }

    fun click_see_all_aktivitas_anak() {
        Thread.sleep(1000)
        val viewInteraction = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.btn_see_all), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.ent_grid_layout)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Aktivitas Anak")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteraction.perform(ViewActions.click())
        Thread.sleep(3000)
        val viewInteractions = Espresso.onView(AllOf.allOf(
                AllOf.allOf(ViewMatchers.withId(R.id.ent_recycle_view_grid), ViewMatchers.isDescendantOfA(ViewMatchers.withId(R.id.recycler_view_home)), ViewMatchers.hasSibling(ViewMatchers.withId(R.id.ent_title_card)), ViewMatchers.hasSibling(ViewMatchers.withText("Aktivitas Anak")),
                        ViewMatchers.isDisplayed()))).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        viewInteractions.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(0, ViewActions.click()))
        Thread.sleep(3000)
    }

    fun click_lanjutkan_ticket() {
        val viewInteraction = Espresso.onView(ViewMatchers.withId(R.id.btn_event_pdp_cek_tiket))
        viewInteraction.perform(ViewActions.click())
    }

    fun click_check_ticket() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(3000)
        Espresso.onView(CommonMatcher.getElementFromMatchAtPosition(ViewMatchers.withText("12"), 1)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Thread.sleep(3000)
        Espresso.onView(CommonMatcher.getElementFromMatchAtPosition(ViewMatchers.withText("12"), 1)).perform(ViewActions.click())
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_PDP_VALIDATOR_QUERY = "tracker/event/pdpeventcheck.json"
        private const val ENTERTAINMENT_EVENT_HOME_VALIDATOR_QUERY = "tracker/event/homeeventcheck.json"

        private const val KEY_QUERY_PDP_V3 = "EventProductDetail"
        private const val KEY_TRAVEL_HOLIDAY = "TravelGetHoliday"
        private const val KEY_QUERY_CONTENT = "EventContentById"
        private const val KEY_EVENT_CHILD = "EventCategories"

        private const val PATH_RESPONSE_PDP = "event_pdp.json"
        private const val PATH_RESPONSE_PDP_CONTENT = "event_pdp_content.json"
        private const val PATH_RESPONSE_TRAVEL_HOLIDAY = "event_travel_holiday.json"
        private const val PATH_RESPONSE_HOME = "event_home.json"

    }
}