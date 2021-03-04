package com.tokopedia.entertainment

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.home.activity.HomeEventActivity
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventGridEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventLocationEventViewHolder
import com.tokopedia.entertainment.home.fragment.EventHomeFragment
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.ResourcePathUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.hamcrest.core.IsNot
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule = ActivityTestRule(HomeEventActivity::class.java, false, false)

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
        }
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, HomeEventActivity::class.java)

        LocalCacheHandler(context, EventHomeFragment.PREFERENCES_NAME).also {
            it.putBoolean(EventHomeFragment.SHOW_COACH_MARK_KEY, true)
            it.applyEditor()
        }

        activityRule.launchActivity(intent)

        Intents.intending(IsNot.not(IntentMatchers.isInternal())).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun validateHomeEvent() {
        Thread.sleep(5000)

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

        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_HOME_VALIDATOR_QUERY), hasAllSuccess())
    }

    fun impression_banner() {
        onView(withId(R.id.banner_recyclerview)).perform(RecyclerViewActions.scrollToPosition<BannerViewPagerAdapter.BannerViewHolder>(1))
    }

    fun click_banner() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))

        val viewInteraction = onView(withId(R.id.banner_recyclerview)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<BannerViewPagerAdapter.BannerViewHolder>(0, click()))
    }

    fun click_category_icon() {
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(1))
        Thread.sleep(2000)
        onView(withId(R.id.ent_recycle_view_category)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryEventViewHolder>(0, click()))
        Thread.sleep(2000)
    }

    fun impression_carousel_product_event() {
        Thread.sleep(1000)
        onView(withId(R.id.ent_recycle_view_carousel)).perform(RecyclerViewActions.scrollToPosition<EventCarouselEventViewHolder>(2))
    }

    fun click_carousel_product_event() {
        Thread.sleep(3000)
        onView(withId(R.id.ent_recycle_view_carousel)).perform(RecyclerViewActions.actionOnItemAtPosition<EventCarouselEventViewHolder>(2, click()))
        Thread.sleep(3000)
    }

    fun impression_taman_bermain_lokal() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(3))
    }

    fun click_product_taman_bermain_lokal() {
        Thread.sleep(3000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_grid), isDescendantOfA(withId(R.id.recycler_view)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, click()))
        Thread.sleep(3000)
    }

    fun click_see_all_taman_bermain_lokal() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.btn_see_all), isDescendantOfA(withId(R.id.ent_grid_layout)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
        Thread.sleep(3000)
    }

    fun impression_location_event() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(4))
        Thread.sleep(2000)
        onView(withId(R.id.ent_recycle_view_location)).perform(RecyclerViewActions.scrollToPosition<EventLocationEventViewHolder>(3))
    }

    fun click_location_event() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_location), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventLocationEventViewHolder>(0, click()))
        Thread.sleep(5000)
    }

    fun impression_taman_bermain_mancanegara() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(5))
        Thread.sleep(2000)
    }

    fun click_product_taman_bermain_mancanegara() {
        Thread.sleep(3000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_grid), isDescendantOfA(withId(R.id.recycler_view)), hasSibling(withId(R.id.ent_title_card)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, click()))
        Thread.sleep(3000)
    }

    fun click_see_all_taman_bermain_mancanegara() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.btn_see_all), isDescendantOfA(withId(R.id.ent_grid_layout)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
        Thread.sleep(3000)
    }

    fun impression_aktivitas() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(6))
        Thread.sleep(2000)
    }

    fun click_product_aktivitas() {
        Thread.sleep(3000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_grid), isDescendantOfA(withId(R.id.recycler_view)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Aktivitas")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, click()))
        Thread.sleep(3000)
    }

    fun click_see_all_aktivitas() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.btn_see_all), isDescendantOfA(withId(R.id.ent_grid_layout)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Aktivitas")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
        Thread.sleep(3000)
    }

    fun impression_festival() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(7))
        Thread.sleep(2000)
    }

    fun click_product_festival() {
        Thread.sleep(3000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_grid), isDescendantOfA(withId(R.id.recycler_view)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Festival")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, click()))
        Thread.sleep(3000)
    }

    fun click_see_all_festival() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.btn_see_all), isDescendantOfA(withId(R.id.ent_grid_layout)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Festival")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
        Thread.sleep(3000)
    }


    fun impression_event() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(8))
        Thread.sleep(2000)
    }

    fun click_product_event() {
        Thread.sleep(3000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_grid), isDescendantOfA(withId(R.id.recycler_view)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Events")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(1, click()))
        Thread.sleep(3000)
    }

    fun click_see_all_event() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.btn_see_all), isDescendantOfA(withId(R.id.ent_grid_layout)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Events")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
        Thread.sleep(3000)
    }

    fun impression_aktivitas_anak() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(9))
        Thread.sleep(2000)
    }

    fun click_product_aktivitas_anak() {
        Thread.sleep(3000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_grid), isDescendantOfA(withId(R.id.recycler_view)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Aktivitas Anak")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventGridEventViewHolder>(0, click()))
        Thread.sleep(3000)
    }

    fun click_see_all_aktivitas_anak() {
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.btn_see_all), isDescendantOfA(withId(R.id.ent_grid_layout)), hasSibling(withId(R.id.ent_title_card)), hasSibling(withText("Aktivitas Anak")),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(click())
        Thread.sleep(3000)
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_HOME_VALIDATOR_QUERY = "tracker/event/homeeventcheck.json"
        private const val KEY_EVENT_CHILD = "EventCategories"
        private const val PATH_RESPONSE_HOME = "event_home.json"
    }

}