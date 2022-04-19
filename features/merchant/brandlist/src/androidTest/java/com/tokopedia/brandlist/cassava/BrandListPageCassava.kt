package com.tokopedia.brandlist.cassava

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_category.presentation.activity.BrandlistActivity
import com.tokopedia.brandlist.brandlist_category.presentation.adapter.BrandlistContainerAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.BrandlistPageAdapter
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder.*
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.officialstore.extension.selectTabAtPosition
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.espresso_component.CommonMatcher
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

/**
 * Created by Lukas on 1/28/21.
 */
class BrandListPageCassava {

    companion object{
        private const val TAG = "BrandListPageAnalyticsTest"
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME = "tracker/official_store/brandlist_page.json"
    }

    @get:Rule
    var activityRule = ActivityTestRule(BrandlistActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun setup() {
        gtmLogDBSource.deleteAll().subscribe()
        setupGraphqlMockResponse(BrandListPageMockResponse())
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, BrandlistActivity::class.java))
    }

    @After
    fun dispose(){
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testBrandList() {
        initTest()
        // 1. click category OS
        Espresso.onView(withId(R.id.tablayout)).perform(selectTabAtPosition(0))

        Espresso.onView(CommonMatcher.firstView(withId(R.id.recycler_view))).perform(ViewActions.swipeDown())

        doActivityTestByModelClass(dataModelClass = FeaturedBrandUiModel::class) { holder, position ->
            logTestMessage("Captured is FeaturedBrandViewHolder")
            Espresso.onView(withId(R.id.tv_expand_button)).perform(click())
            Thread.sleep(5000)
            CommonActions.clickOnEachItemRecyclerView(holder.itemView, R.id.rv_featured_brand, 2)
        }
        doActivityTestByModelClass(dataModelClass = PopularBrandUiModel::class) { holder, position ->
            logTestMessage("Captured is PopularBrandViewHolder")
            CommonActions.clickOnEachItemRecyclerView(holder.itemView, R.id.rv_popular_brand, 2)
        }
        doActivityTestByModelClass(dataModelClass = NewBrandUiModel::class) { holder, position ->
            logTestMessage("Captured is NewBrandViewHolder")
            CommonActions.clickOnEachItemRecyclerView(holder.itemView, R.id.rv_new_brand, 2)
        }
        doActivityTestByModelClass(dataModelClass = AllBrandGroupHeaderUiModel::class) { holder, position ->
            logTestMessage("Captured is AllBrandGroupHeaderViewHolder")
//            Espresso.onView(firstView(withId(R.id.chip_alphabet_header))).perform(click())
            CommonActions.clickOnEachItemRecyclerView(holder.itemView, R.id.rv_groups_chip, 4)
        }
        doActivityTestByModelClass(dataModelClass = AllBrandUiModel::class) { holder, position ->
            logTestMessage("Captured is AllBrandViewHolder")
            InstrumentationRegistry.getInstrumentation().runOnMainSync {
                holder.itemView.performClick()
            }
        }
        // 3. Click Searchbar
        Espresso.onView(withId(R.id.layout_search)).perform(click())

        doBrandlistCassavaTest()
        addDebugEnd()
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        InstrumentationAuthHelper.loginInstrumentationTestUser1()
        waitForData()
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun doActivityTest() {
        // 2. scroll and click item at OS
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0

        waitForData()

        // 1. click category OS
        Espresso.onView(withId(R.id.tablayout)).perform(selectTabAtPosition(0))

        Espresso.onView(CommonMatcher.firstView(withId(R.id.recycler_view))).perform(ViewActions.swipeDown())
        Thread.sleep(5000)

        for (i in 0 until itemCount) {
            logTestMessage("Scolled to "+i)
            scrollRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
            Thread.sleep(5000)
        }

        logTestMessage("Done iterating dynamic channel")
        // 3. Click Searchbar
        Espresso.onView(withId(R.id.layout_search)).perform(click())

        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun <T: Any> doActivityTestByModelClass(delayBeforeRender: Long = 2000L, dataModelClass : KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemClickLimit: Int)-> Unit) {
        val brandListRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val brandListAdapter = brandListRecyclerView.adapter as? BrandlistPageAdapter

        val visitableList = brandListAdapter?.list?: listOf()
        val targetModel = visitableList.find { it.javaClass.simpleName == dataModelClass.simpleName }
        val targetModelIndex = visitableList.indexOf(targetModel)

        targetModelIndex.let { targetModelIndex->
            scrollRecyclerViewToPosition(brandListRecyclerView, targetModelIndex)
            if (delayBeforeRender > 0) Thread.sleep(delayBeforeRender)
            val targetModelViewHolder = brandListRecyclerView.findViewHolderForAdapterPosition(targetModelIndex)
            targetModelViewHolder?.let { targetModelViewHolder-> isTypeClass.invoke(targetModelViewHolder, targetModelIndex) }
        }
    }

    private fun doBrandlistCassavaTest() {
        waitForData()
        //worked
        MatcherAssert.assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME),
                hasAllSuccess())
    }

    private fun scrollRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as GridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position,0) }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }

    private fun checkProductOnDynamicChannel(officialStoreRecyclerView: RecyclerView, i: Int) {
        logTestMessage("Check dynamic channel")

        val viewHolder = officialStoreRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewHolder) {
            is FeaturedBrandViewHolder -> {
                logTestMessage("Captured is FeaturedBrandViewHolder")
                Espresso.onView(withId(R.id.tv_expand_button)).perform(click())
                Thread.sleep(5000)
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_featured_brand, 2)
            }
            is PopularBrandViewHolder -> {
                logTestMessage("Captured is PopularBrandViewHolder")
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_popular_brand, 2)
            }
            is NewBrandViewHolder -> {
                logTestMessage("Captured is NewBrandViewHolder")
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_new_brand, 2)
            }
            is AllBrandGroupHeaderViewHolder -> {
                logTestMessage("Captured is AllBrandGroupHeaderViewHolder")
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_groups_chip, 0)
            }
            is AllBrandViewHolder -> {
                logTestMessage("Captured is AllBrandViewHolder")
                Espresso.onView(CoreMatchers.allOf(ViewMatchers.isDisplayed(), withId(R.id.recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, click()))
            }
        }
    }
}