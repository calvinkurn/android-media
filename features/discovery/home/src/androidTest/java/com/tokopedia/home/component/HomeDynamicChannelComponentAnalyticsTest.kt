package com.tokopedia.home.component

import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.home.R
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.PopularKeywordViewHolder
import com.tokopedia.home.environment.InstrumentationHomeTestActivity
import com.tokopedia.home.mock.HomeMockResponseConfig
import com.tokopedia.home_component.viewholders.*
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass

private const val TAG = "HomeDynamicChannelComponentAnalyticsTest"
/**
 * Created by yfsx on 2/9/21.
 */
class HomeDynamicChannelComponentAnalyticsTest {
    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationHomeTestActivity>(InstrumentationHomeTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            gtmLogDBSource.deleteAll().subscribe()
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(HomeMockResponseConfig())
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun resetAll() {
        disableCoachMark(context)
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testComponentProductHighlight() {
        initTest()

        doActivityTest(ProductHighlightComponentViewHolder::class) { _: RecyclerView.ViewHolder, _: Int ->
            clickOnProductHighlightItem()
        }

        getAssertProductHighlight(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentPopularKeyword() {
        initTest()

        doActivityTest(PopularKeywordViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnPopularKeywordSection(viewHolder, i)
        }

        getAssertPopularKeyword(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentMixLeft() {
        initTest()

        doActivityTest(MixLeftComponentViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnMixLeftSection(viewHolder, i)
        }

        getAssertMixLeft(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentMixTop() {
        initTest()

        doActivityTest(MixTopComponentViewHolder::class) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnMixTopSection(viewHolder, i)
        }

        getAssertMixTop(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testComponentLegoBanner() {
        initTest()

        doActivityTest(listOf(DynamicLegoBannerViewHolder::class.simpleName!!,
                Lego4AutoBannerViewHolder::class.simpleName!!)) { viewHolder: RecyclerView.ViewHolder, i: Int ->
            clickOnLegoBannerSection(viewHolder, i)
        }

        getAssertLegoBanner(gtmLogDBSource, context)

        onFinishTest()

        addDebugEnd()
    }

    private fun initTest() {
        InstrumentationAuthHelper.clearUserSession()
        waitForData()
        hideStickyLogin()
    }

    private fun hideStickyLogin() {
        val layout = activityRule.activity.findViewById<ConstraintLayout>(R.id.layout_sticky_container)
        if (layout.visibility == View.VISIBLE) {
            layout.visibility = View.GONE
        }
    }

    private fun <T: Any> doActivityTest(viewClass : KClass<T>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount)  {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClass.simpleName == viewHolder.javaClass.simpleName) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun doActivityTest(viewClassName: List<String>, isTypeClass: (viewHolder: RecyclerView.ViewHolder, itemPosition: Int)-> Unit) {
        val homeRecyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.home_fragment_recycler_view)
        val itemCount = homeRecyclerView.adapter?.itemCount ?: 0
        countLoop@ for (i in 0 until itemCount)  {
            scrollHomeRecyclerViewToPosition(homeRecyclerView, i)
            val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)
            if (viewHolder != null && viewClassName.contains(viewHolder.javaClass.simpleName)) {
                isTypeClass.invoke(viewHolder, i)
            }
        }
        endActivityTest()
    }

    private fun endActivityTest() {
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
        waitForLoadCassavaAssert()
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as LinearLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 400) }
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }
}