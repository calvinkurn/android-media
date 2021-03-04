package com.tokopedia.tkpd.category_levels.activity

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.categorylevels.view.activity.CategoryRevampActivity
import com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.masterproductcarditem.MasterProductCardItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.espresso_component.CommonActions
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CategoryLevelsTopAdsVerificationTest {
    private var topAdsAssertion: TopAdsAssertion? = null

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
        topAdsAssertion = TopAdsAssertion(
                activityRule.activity,
                activityRule.activity.application as TopAdsVerificatorInterface
        )
        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        activityRule.launchActivity(Intent(targetContext, CategoryRevampActivity::class.java).apply {
            data = Uri.parse("tokopedia-android-internal://category/25?categoryName=Audio")
        })
    }

    @After
    fun deleteDatabase() {
        topAdsAssertion?.after()
    }

    @Test
    fun testTopAdsCategory() {
        waitForData()

        val productsRecyclerView = activityRule.activity.findViewById<RecyclerView>(com.tokopedia.discovery2.R.id.recycler_view)
        val itemCount = productsRecyclerView.adapter?.itemCount?:0

        for (i in 0 until itemCount) {
            scrollRecyclerViewToPosition(productsRecyclerView, i)
            checkProductOnDynamicChannel(productsRecyclerView, i)
        }
        topAdsAssertion?.assert()
    }

    private fun checkProductOnDynamicChannel(recyclerView: RecyclerView, i: Int) {
        when (recyclerView.findViewHolderForAdapterPosition(i)) {
            is MasterProductCardItemViewHolder -> {
                try {
                    onView(withId(com.tokopedia.discovery2.R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition<MasterProductCardItemViewHolder>(
                            i,CommonActions.clickChildViewWithId(com.tokopedia.discovery2.R.id.cardViewProductCard)))
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
    }


}