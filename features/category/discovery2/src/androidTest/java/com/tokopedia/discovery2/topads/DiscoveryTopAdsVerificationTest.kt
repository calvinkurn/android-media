package com.tokopedia.discovery2.topads

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery.INTERNAL_DISCOVERY
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created by Zishan
 * Instrumented test, which will execute on an Android device.
 * Verify Topads for Discovery Pages
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

class DiscoveryTopAdsVerificationTest {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    @get:Rule
    var activityRule = object : ActivityTestRule<DiscoveryActivity>(DiscoveryActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            login()
        }
    }

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context) { topAdsCount }
    private val discoveryTopadsTestDeeplink = "${INTERNAL_DISCOVERY}/topads-test-2"

    @Before
    fun setup() {
        activityRule.launchActivity(Intent(context, DiscoveryActivity::class.java).apply {
            data = Uri.parse(discoveryTopadsTestDeeplink)
        })
    }

    @After
    fun tearDown() {
        topAdsAssertion.after()
    }

    @Test
    fun testTopAdsDiscovery() {
        waitForData()

        val discoveryRecyclerView = activityRule.activity
                .findViewById<RecyclerView>(R.id.recycler_view)
        val itemCount = discoveryRecyclerView.adapter?.itemCount ?: 0

        val itemList = discoveryRecyclerView.getItemList()
        topAdsCount = calculateTopAdsCount(itemList)

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(discoveryRecyclerView, i)
            checkProductsOnEachComponent(discoveryRecyclerView, i)
        }
        topAdsAssertion.assert()
    }


    private fun calculateTopAdsCount(itemList: List<ComponentsItem>): Int {
        var count = 0
        for (item in itemList) {
            count += countTopAdsInItem(item)
        }
        return count
    }

    private fun countTopAdsInItem(item: ComponentsItem): Int {
        var count = 0

        when (item.name) {
            ComponentNames.ProductCardRevampItem.componentName -> {
                if (item.data?.firstOrNull()?.isTopads == true) {
                    count++
                }
            }
            ComponentNames.MasterProductCardItemList.componentName -> {
                if (item.data?.firstOrNull()?.isTopads == true) {
                    count++
                }
            }
            ComponentNames.ProductCardCarousel.componentName -> {
                item.data?.let { componentItemsList ->
                    for (grid in componentItemsList) {
                        if (grid.isTopads == true) count++
                    }
                }
            }
        }
        return count
    }

    private fun checkProductsOnEachComponent(discoveryRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = discoveryRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MasterProductCardItemViewHolder -> {
                try {
                    Espresso.onView(withId(R.id.recycler_view))
                            .perform(RecyclerViewActions.actionOnItemAtPosition<MasterProductCardItemViewHolder>(
                                    i, ViewActions.click()))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            is ProductCardCarouselViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView,
                        R.id.products_rv, 0)
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    private fun RecyclerView.getItemList(): List<ComponentsItem> {
        val discoveryAdapter = this.adapter as? DiscoveryRecycleAdapter

        if (discoveryAdapter == null) {
            val detailMessage = "Adapter is not ${DiscoveryRecycleAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return discoveryAdapter.currentList
    }
}
