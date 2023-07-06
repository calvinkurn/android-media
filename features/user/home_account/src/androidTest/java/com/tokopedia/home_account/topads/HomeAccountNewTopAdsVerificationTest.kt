package com.tokopedia.home_account.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.home_account.di.ActivityComponentFactory
import com.tokopedia.home_account.stub.di.ActivityComponentFactoryStub
import com.tokopedia.home_account.test.R
import com.tokopedia.home_account.view.activity.HomeAccountUserActivity
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.adapter.viewholder.ProductItemViewHolder
import com.tokopedia.home_account.view.custom.SwipeRecyclerView
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@TopAdsTest
class HomeAccountNewTopAdsVerificationTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<HomeAccountUserActivity>(
        HomeAccountUserActivity::class.java,
        false,
        false
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            setupTopAdsDetector()
        }
    }

    @get:Rule
    var grantPermission: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context) { topAdsCount }
    private var recyclerView: SwipeRecyclerView? = null

    @Before
    fun setTopAdsAssertion() {
        val stub = ActivityComponentFactoryStub()
        ActivityComponentFactory.instance = stub
    }

    @After
    fun tearDown() {
        topAdsAssertion.after()
        activityRule.finishActivity()
    }

    @Test
    fun testTopAdsNewHomeAccount() {
        activityRule.launchActivity(Intent())
        recyclerView = activityRule.activity.findViewById(R.id.home_account_user_fragment_rv)
        intending(isInternal())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        Thread.sleep(1000)
        performUserJourney()
        topAdsAssertion.assert()
    }

    private fun performUserJourney() {
        recyclerView?.id?.let {
            onView(withId(it))
                .check(matches(isDisplayed()))
        }

        recyclerView?.let {
            val itemCount = it.adapter?.itemCount ?: 0
            for (i in 0 until itemCount) {
                scrollHomeAccountRecyclerViewToPosition(it, i)
                performOnClickForEachRecommendation(it, i)
            }
        }
    }

    private fun performOnClickForEachRecommendation(recyclerView: SwipeRecyclerView, index: Int) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(index)
        if (viewHolder is ProductItemViewHolder) {
            val item = recyclerView.getItemAdapter().getItem(index) as RecommendationItem
            if (item.isTopAds) {
                onView(withId(recyclerView.id))
                    .perform(
                        RecyclerViewActions.actionOnItemAtPosition<ProductItemViewHolder>(
                            index,
                            ViewActions.click()
                        )
                    )
                topAdsCount++
            }
        }
    }

    private fun scrollHomeAccountRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun RecyclerView?.getItemAdapter(): HomeAccountUserAdapter {
        val itemAdapter = this?.adapter as? HomeAccountUserAdapter

        if (itemAdapter == null) {
            val detailMessage = "Adapter is not ${HomeAccountUserAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return itemAdapter
    }

}
