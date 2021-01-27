package com.tokopedia.home_wishlist.activity

import android.content.Context
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.PerformException
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.analyticsdebugger.debugger.data.source.GtmLogDBSource
import com.tokopedia.cassavatest.getAnalyticsWithQuery
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.home_wishlist.mock.WishlistMockData
import com.tokopedia.home_wishlist.test.R
import com.tokopedia.home_wishlist.view.fragment.WishlistFragment.Companion.COACH_MARK_TAG
import com.tokopedia.home_wishlist.view.viewholder.WishlistItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsVerificationTestReportUtil
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.InstrumentationAuthHelper.clearUserSession
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
private const val TAG = "CassavaWishlistTest"


private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_ITEM = "tracker/wishlist/wishlist_item.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_ADD_TO_CART = "tracker/wishlist/wishlist_addtocart.json"
private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_DELETE_ITEM = "tracker/wishlist/wishlist_item_action_delete.json"

class CassavaWishlistTest {

    private val SHOWCASE_PREFERENCES = "show_case_pref"

    @get:Rule
    var activityRule = object: ActivityTestRule<InstrumentationWishlistTestActivity>(InstrumentationWishlistTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            gtmLogDBSource.deleteAll().subscribe()
            disableCoachMark()
            super.beforeActivityLaunched()
            setupGraphqlMockResponse(WishlistMockData())
        }
    }

    private fun login() {
        InstrumentationAuthHelper.loginToAnUser(activityRule.activity.application)
    }

    private fun disableCoachMark(){
        val sharedPrefs = context.getSharedPreferences(SHOWCASE_PREFERENCES, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(
                COACH_MARK_TAG, true).apply()
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val gtmLogDBSource = GtmLogDBSource(context)

    @Before
    fun resetAll() {
        gtmLogDBSource.deleteAll().subscribe()
    }

    @Test
    fun testWishlistImpressionAndClickLogin() {
        initTestWithLogin()
        login()
        scrollToItemAndBanner()

        doCassavaCheck()

        onFinishTest()

        addDebugEnd()
    }


    @Test
    fun testClickAddToCartAndSnackbarButton() {
        initTestWithLogin()
        login()
        scrollToItemPositionClickAddToCartAndSnackbarButton(0)

        doCassavaCheckAddToCartTracker()

        onFinishTest()

        addDebugEnd()
    }

    @Test
    fun testDeleteWishlist() {
        initTestWithLogin()
        login()
        scrollToItemPositionDeleteItem(0)

        doCassavaDeleteItem()

        onFinishTest()

        addDebugEnd()
    }

    private fun initTest() {
        clearUserSession()
        waitForData()
    }

    private fun scrollToItemAndBanner() {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = recyclerView.adapter?.itemCount ?: 0
        for (i in 0 until itemCount) {
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkItemsAndBanner(recyclerView, i)
        }
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun scrollToItemPosition(pos: Int) {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        scrollHomeRecyclerViewToPosition(recyclerView, pos)
        checkItemsAndBanner(recyclerView, pos)
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun scrollToItemPositionClickAddToCartAndSnackbarButton(pos: Int) {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        scrollHomeRecyclerViewToPosition(recyclerView, pos)
        clickAddToCart()
        waitFoSnackBar()
        clickSnackBarButton()
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }

    private fun scrollToItemPositionDeleteItem(pos: Int) {
        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.recycler_view)
        scrollHomeRecyclerViewToPosition(recyclerView, pos)
        clickDeleteItem()
        waitDeleteDialog()
        clickCancelDelete()
        waitDeleteDialog()
        clickDeleteItem()
        waitDeleteDialog()
        clickConfirmDelete()
        waitDeleteDialog()
        waitDeleteDialog()
        activityRule.activity.finish()
        logTestMessage("Done UI Test")
    }


    private fun scrollHomeRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset   (position, 0) }
    }


    private fun doCassavaCheck() {
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_ITEM),
                hasAllSuccess())
    }

    private fun doCassavaCheckAddToCartTracker() {
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_ADD_TO_CART),
                hasAllSuccess())
    }

    private fun doCassavaDeleteItem() {
        waitForData()
        assertThat(getAnalyticsWithQuery(gtmLogDBSource, context, ANALYTIC_VALIDATOR_QUERY_FILE_NAME_WISHLIST_DELETE_ITEM),
                hasAllSuccess())
    }

    private fun checkItemsAndBanner(homeRecyclerView: RecyclerView, i: Int) {
        val viewholder = homeRecyclerView.findViewHolderForAdapterPosition(i)
        when (viewholder) {
            is WishlistItemViewHolder -> {
                val holderName = "WishlistItemViewHolder"
                logTestMessage("VH $holderName")
                clickWishlistItem(viewholder.itemView, holderName, i)
            }
        }
    }

    private fun clickAddToCart() {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.buttonAddToCart)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS atc")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED atc")
        }
    }

    private fun clickSnackBarButton() {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.snackbar_btn)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS snackbar button")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED snackbar button")
        }
    }

    private fun clickWishlistItem(view: View, viewComponent: String, itemPos: Int) {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.wishlist_card)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS wishlist item")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED wishlist item")
        }
    }

    private fun clickDeleteItem() {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.imageRemoveFromWishlist)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS remove")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED remove")
        }
    }

    private fun clickCancelDelete() {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.dialog_btn_primary)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS cancelDelete")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED cancelDelete")
        }
    }

    private fun clickConfirmDelete() {
        try {
            Espresso.onView(firstView(ViewMatchers.withId(R.id.dialog_btn_secondary)))
                    .perform(ViewActions.click())
            logTestMessage("Click SUCCESS ConfirmDelete")
        } catch (e: PerformException) {
            e.printStackTrace()
            logTestMessage("Click FAILED ConfirmDelete")
        }
    }

    private fun logTestMessage(message: String) {
        TopAdsVerificationTestReportUtil.writeTopAdsVerificatorLog(activityRule.activity, message)
        Log.d(TAG, message)
    }


    private fun initTestWithLogin() {
        initTest()
    }

    private fun waitForData(repetition: Long = 1) {
        Thread.sleep(5000 * repetition)
    }

    private fun waitFoSnackBar() {
        Thread.sleep(1000)
    }
    private fun waitDeleteDialog() {
        Thread.sleep(1000)
    }

    private fun addDebugEnd() {
        Thread.sleep(5000)
    }

    private fun onFinishTest() {
        gtmLogDBSource.deleteAll().subscribe()
    }



}