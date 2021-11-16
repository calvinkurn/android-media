package com.tokopedia.officialstore.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.viewholders.MixLeftComponentViewHolder
import com.tokopedia.home_component.viewholders.MixTopComponentViewHolder
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestActivity
import com.tokopedia.officialstore.environment.InstrumentationOfficialStoreTestFullActivity
import com.tokopedia.officialstore.official.presentation.adapter.OfficialHomeAdapter
import com.tokopedia.officialstore.official.presentation.adapter.viewholder.OfficialProductRecommendationViewHolder
import com.tokopedia.officialstore.official.presentation.adapter.datamodel.ProductRecommendationDataModel
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelMixTopViewHolder
import com.tokopedia.officialstore.util.OSRecyclerViewIdlingResource
import com.tokopedia.officialstore.util.preloadRecomOnOSPage
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions.clickOnEachItemRecyclerView
import com.tokopedia.test.application.espresso_component.CommonMatcher.firstView
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by DevAra
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for OSPage
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class OSTopAdsVerificationTest {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object: IntentsTestRule<InstrumentationOfficialStoreTestFullActivity>(
        InstrumentationOfficialStoreTestFullActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            loginInstrumentationTestTopAdsUser()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })
    private var osRecyclerViewIdlingResource: OSRecyclerViewIdlingResource? = null

    @Before
    fun setUp() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        osRecyclerViewIdlingResource = OSRecyclerViewIdlingResource(
            activity = activityRule.activity,
            limitCountToIdle = 3
        )
        IdlingRegistry.getInstance().register(osRecyclerViewIdlingResource)
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion.after()
        IdlingRegistry.getInstance().unregister(osRecyclerViewIdlingResource)
    }

    @Test
    fun testTopAdsHome() {
        Espresso.onView(firstView(withId(R.id.os_child_recycler_view))).check(matches(isDisplayed()))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.os_child_recycler_view)
        val itemAdapter: OfficialHomeAdapter = recyclerView.adapter as OfficialHomeAdapter

        /**
         * This function needed to remove any loading view, because any infinite loop rendered view such as loading view,
         * shimmering, progress bar, etc can block instrumentation test
         */
        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val checkLoadingView: View? = activityRule.activity.findViewById<View>(R.id.loading_view)
                checkLoadingView?.let { checkLoadingView.gone() }
            }
        })

        /**
         * This function needed to trigger product recommendation usecase in official store,
         * official store page only hit recommendation usecase on scroll in the end of current list
         */
        preloadRecomOnOSPage(recyclerView)
        Espresso.onView(firstView(withId(R.id.os_child_recycler_view))).perform(ViewActions.swipeUp())

        waitForData()

        val itemList = itemAdapter.currentList
        topAdsCount = calculateTopAdsCount(itemList)

        val itemCount = itemList.size

        for (i in 0 until itemCount) {
            val checkLoadingView: View? = activityRule.activity.findViewById<View>(R.id.loading_view)
            checkLoadingView?.let { checkLoadingView.gone() }
            scrollHomeRecyclerViewToPosition(recyclerView, i)
            checkProductOnDynamicChannel(recyclerView, i)
        }
        topAdsAssertion.assert()
    }

    private fun calculateTopAdsCount(itemList: List<Visitable<*>>) : Int {
        var count = 0
        for (item in itemList) {
            count += countTopAdsInItem(item)
        }
        return count
    }

    private fun countTopAdsInItem(item: Visitable<*>) : Int {
        var count = 0

        when (item) {
            is MixTopDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
            is MixLeftDataModel -> {
                for (grid in item.channelModel.channelGrids)
                    if (grid.isTopads) count++
            }
            is ProductRecommendationDataModel -> {
                if (item.productItem.isTopAds) count++
            }
        }
        return count
    }

    private fun checkProductOnDynamicChannel(homeRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = homeRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MixTopComponentViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is MixLeftComponentViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.rv_product, 0)
            }
            is DynamicChannelMixTopViewHolder -> {
                clickOnEachItemRecyclerView(viewHolder.itemView, R.id.dc_banner_rv, 0)
            }
            is OfficialProductRecommendationViewHolder -> {
                Espresso.onView(firstView(withId(R.id.os_child_recycler_view)))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(i, ViewActions.click()))
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollHomeRecyclerViewToPosition(homeRecyclerView: RecyclerView, position: Int) {
        val layoutManager = homeRecyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }
}
