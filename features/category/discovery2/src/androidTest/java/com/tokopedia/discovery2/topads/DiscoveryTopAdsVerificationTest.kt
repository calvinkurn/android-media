package com.tokopedia.discovery2.topads

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.discovery2.test.R
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.productcardcarousel.ProductCardCarouselViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import com.tokopedia.test.application.util.setupTopAdsDetector
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
    private var topAdsAssertion: TopAdsAssertion? = null
    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val discoveryTopadsTestDeeplink = "tokopedia-android-internal://discovery/topads-test-2"

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    var activityRule = object : ActivityTestRule<DiscoveryActivity>(DiscoveryActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            loginInstrumentationTestTopAdsUser()
            setupTopAdsDetector()
        }
    }

    @Before
    fun setTopAdsAssertion() {
        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
        activityRule.launchActivity(Intent(context, DiscoveryActivity::class.java).apply {
            data = Uri.parse(discoveryTopadsTestDeeplink)
        })
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsDiscovery() {
        waitForData()

        val productsRecyclerView = activityRule.activity.findViewById<RecyclerView>(com.tokopedia.discovery2.R.id.recycler_view)
        val itemCount = productsRecyclerView.adapter?.itemCount ?: 0

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(productsRecyclerView, i)
            checkProductsOnEachComponent(productsRecyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkProductsOnEachComponent(discoveryRecyclerView: RecyclerView, i: Int) {
        when (val viewHolder = discoveryRecyclerView.findViewHolderForAdapterPosition(i)) {
            is MasterProductCardItemViewHolder -> {
                try {
                    Espresso.onView(withId(com.tokopedia.discovery2.R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<MasterProductCardItemViewHolder>(
                            i, CommonActions.clickChildViewWithId(com.tokopedia.discovery2.R.id.cardViewProductCard)))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            is ProductCardCarouselViewHolder -> {
                CommonActions.clickOnEachItemRecyclerView(viewHolder.itemView, R.id.products_rv, 0)
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
}
