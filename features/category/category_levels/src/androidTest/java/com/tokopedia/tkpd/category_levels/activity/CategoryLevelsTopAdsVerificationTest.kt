package com.tokopedia.tkpd.category_levels.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.categorylevels.view.activity.CategoryRevampActivity
import com.tokopedia.discovery2.ComponentNames
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.viewcontrollers.adapter.DiscoveryRecycleAdapter
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardrevamp.ProductCardRevampViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryLevelsTopAdsVerificationTest {
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context) { topAdsCount }

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object: ActivityTestRule<CategoryRevampActivity>(CategoryRevampActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    @Before
    fun setTopAdsAssertion() {
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        activityRule.launchActivity(Intent(targetContext, CategoryRevampActivity::class.java).apply {
            data = Uri.parse("tokopedia-android-internal://category/25?categoryName=Audio")
        })
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion.after()
    }

    @Test
    fun testTopAdsCategory() {
        waitForData()

        val productsRecyclerView =
            activityRule.activity.findViewById<RecyclerView>(com.tokopedia.discovery2.R.id.recycler_view)
        val itemCount = productsRecyclerView.adapter?.itemCount ?: 0
        val itemList = (productsRecyclerView.adapter as DiscoveryRecycleAdapter).currentList
        topAdsCount = calculateTopAdsCount(itemList)

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(productsRecyclerView, i)
            checkProductOnDynamicChannel(productsRecyclerView, i)
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

    private fun checkProductOnDynamicChannel(recyclerView: RecyclerView, i: Int) {
        when (recyclerView.findViewHolderForAdapterPosition(i)) {
            is MasterProductCardItemViewHolder -> {
                try {
                    onView(withId(com.tokopedia.discovery2.R.id.recycler_view))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<MasterProductCardItemViewHolder>(
                            i, ViewActions.click()))
                } catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun waitForData() {
        Thread.sleep(5000)
    }

    private fun scrollRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
        when (recyclerView.findViewHolderForAdapterPosition(position)) {
            is ProductCardRevampViewHolder -> {
                waitForData()
            }
        }
    }
}
