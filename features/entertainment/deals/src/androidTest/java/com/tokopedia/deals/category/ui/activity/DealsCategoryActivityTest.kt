package com.tokopedia.deals.category.ui.activity

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
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.deals.R
import com.tokopedia.deals.category.ui.activity.mock.DealsCategoryMockResponse
import com.tokopedia.deals.location_picker.model.response.Location
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.withTagStringValue
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.core.AllOf
import org.junit.*

/**
 * @author by jessica on 08/10/20
 */
class DealsCategoryActivityTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)
    private lateinit var localCacheHandler: LocalCacheHandler

    private val TAB_NAME = "Relaksasi"

    @get:Rule
    var activityRule: IntentsTestRule<DealsCategoryActivity> = object : IntentsTestRule<DealsCategoryActivity>(DealsCategoryActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            gtmLogDBSource.deleteAll().subscribe()
            localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
            localCacheHandler.apply {
                putBoolean(SHOW_COACH_MARK_KEY, false)
                applyEditor()
            }
            setupGraphqlMockResponse(DealsCategoryMockResponse())
        }

        override fun getActivityIntent(): Intent {
            return DealsCategoryActivity.getCallingIntent(context)
        }
    }

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @Test
    fun testCategoryPageFlow() {
        clickOnRelaksasiTab()
        swipeUpOnCategoryTab()
        onChangeLocation()
        clickOnSearchBar()
        actionOnBrandViewHolder()
        filterProducts()

        Assert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_DEALS_CATEGORY_PAGE),
                hasAllSuccess())
    }

    private fun actionOnBrandViewHolder() {
        Thread.sleep(1000)
        val recyclerView = onView(AllOf.allOf(withId(R.id.deals_category_recycler_view), withTagStringValue(TAB_NAME)))
        recyclerView.perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(0))

        Thread.sleep(1000)
        recyclerView.perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                CommonActions.clickChildViewWithId(R.id.txt_brand_see_all)))
        Thread.sleep(1000)

        val childRv = onView(AllOf.allOf(withId(R.id.rv_brands), withTagStringValue("24")))
        childRv.perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                CommonActions.clickChildViewWithId(R.id.brand_view_holder_layout)))
    }

    private fun filterProducts() {
        Thread.sleep(5000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText("Makanan"))).perform(click())

        Thread.sleep(3000)
        onView(CommonMatcher.firstView(withText("Travel Entertainment Widget"))).perform(click())

        Thread.sleep(2000)
        onView(CommonMatcher.firstView(withText("Filters"))).perform(click())
        Thread.sleep(1000)
        onView(CommonMatcher.firstView(withText("Baju Baru"))).perform(click())
        Thread.sleep(1000)
        onView(CommonMatcher.firstView(withText("Terapkan"))).perform(click())

        Thread.sleep(1000)
    }

    private fun onChangeLocation() {
        Thread.sleep(2000)
        val id = activityRule.activity.currentLoc.id++
        activityRule.activity.setCurrentLocation(Location(id))
        Thread.sleep(2000)
    }

    private fun clickOnRelaksasiTab() {
        Thread.sleep(5000)
        onView(AllOf.allOf(withId(R.id.tab_item_text_id), withText(TAB_NAME))).perform(click())
    }

    private fun swipeUpOnCategoryTab() {
        Thread.sleep(3000)
        onView(AllOf.allOf(withId(R.id.deals_category_recycler_view), withTagStringValue(TAB_NAME))).perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(3))
    }

    private fun clickOnSearchBar() {
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        Thread.sleep(2000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
    }

    @After
    fun tearDown() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    companion object {
        private const val PREFERENCES_NAME = "deals_home_preferences"
        private const val SHOW_COACH_MARK_KEY = "show_coach_mark_key"

        private const val ANALYTIC_VALIDATOR_QUERY_DEALS_CATEGORY_PAGE = "tracker/entertainment/deals/deals_category_tracking.json"

    }
}