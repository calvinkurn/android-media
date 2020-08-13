import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.pressBack
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.analyticsdebugger.validator.core.getAnalyticsWithQuery
import com.tokopedia.analyticsdebugger.validator.core.hasAllSuccess
import com.tokopedia.banner.BannerViewPagerAdapter
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.home.activity.HomeEventActivity
import com.tokopedia.entertainment.home.adapter.HomeEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.CategoryEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventCarouselEventViewHolder
import com.tokopedia.entertainment.home.adapter.viewholder.EventLocationEventViewHolder
import org.hamcrest.core.AllOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeEventActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @get:Rule
    var activityRule: ActivityTestRule<HomeEventActivity> = ActivityTestRule(HomeEventActivity::class.java)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun validateHomeEvent() {
        Thread.sleep(5000)
        //banner
//        impression_banner()
          click_banner()
//        //category
          click_category_icon()
//        //carousel
//        impression_carousel_product_event()
          click_carousel_product_event()
//
//        //taman bermain lokal
//        impression_taman_bermain_lokal()
//
//        //location event
          impression_location_event()
          click_location_event()
//
//        //taman bermain mancanegara
//        impression_taman_bermain_mancanegara()
//
//        //aktivitas
//        impression_aktivitas()
//
//        //festival
//        impression_festival()
//
//        //event
//        impression_event()
//
//        //aktivitas anak
//        impression_aktivitas_anak()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ENTERTAINMENT_EVENT_HOME_VALIDATOR_QUERY), hasAllSuccess())
    }

    fun impression_banner() {
        onView(withId(R.id.banner_recyclerview)).perform(RecyclerViewActions.scrollToPosition<BannerViewPagerAdapter.BannerViewHolder>(0))
    }

    fun click_banner() {
        val viewInteraction = onView(withId(R.id.banner_recyclerview)).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<BannerViewPagerAdapter.BannerViewHolder>(0, click()))
    }

    fun click_category_icon(){
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(1))
        Thread.sleep(2000)
        onView(withId(R.id.ent_recycle_view_category)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoryEventViewHolder>(0, click()))
        Thread.sleep(2000)
        onView(isRoot()).perform(pressBack())
    }

    fun impression_carousel_product_event() {
        Thread.sleep(1000)
        onView(withId(R.id.ent_recycle_view_carousel)).perform(RecyclerViewActions.scrollToPosition<EventCarouselEventViewHolder>(2))
    }

    fun click_carousel_product_event(){
        Thread.sleep(3000)
        onView(withId(R.id.ent_recycle_view_carousel)).perform(RecyclerViewActions.actionOnItemAtPosition<EventCarouselEventViewHolder>(2, click()))
        Thread.sleep(3000)
        onView(isRoot()).perform(pressBack())
    }

    fun impression_taman_bermain_lokal(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(3))
    }

    fun impression_location_event(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(4))
        Thread.sleep(2000)
        onView(withId(R.id.ent_recycle_view_location)).perform(RecyclerViewActions.scrollToPosition<EventLocationEventViewHolder>(3))
    }

    fun click_location_event(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.ent_recycle_view_location), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition<EventLocationEventViewHolder>(0, click()))
        Thread.sleep(5000)
        onView(isRoot()).perform(pressBack())
    }

    fun impression_taman_bermain_mancanegara(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(5))
        Thread.sleep(2000)
    }

    fun impression_aktivitas(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(6))
        Thread.sleep(2000)
    }

    fun impression_festival(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(7))
        Thread.sleep(2000)
    }

    fun impression_event(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(8))
        Thread.sleep(2000)
    }

    fun impression_aktivitas_anak(){
        Thread.sleep(1000)
        val viewInteraction = onView(AllOf.allOf(
                AllOf.allOf(withId(R.id.recycler_view), isDescendantOfA(withId(R.id.event_home_fragment)),
                        isDisplayed()))).check(matches(isDisplayed()))
        viewInteraction.perform(RecyclerViewActions.scrollToPosition<HomeEventViewHolder<*>>(9))
        Thread.sleep(2000)
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_HOME_VALIDATOR_QUERY = "tracker/event/homeeventcheck.json"
    }

}