package com.tokopedia.home_account.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.environment.InstrumentationNewHomeAccountTestActivity
import com.tokopedia.home_account.pref.AccountPreference
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.tokopedia.home_account.test.R
import com.tokopedia.home_account.view.adapter.HomeAccountUserAdapter
import com.tokopedia.home_account.view.custom.SwipeRecyclerView
import com.tokopedia.home_account.view.viewholder.ProductItemViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class HomeAccountNewTopAdsVerificationTest {
    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationNewHomeAccountTestActivity>(InstrumentationNewHomeAccountTestActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            login()
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
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface {
        topAdsCount
    })
    private var recyclerView: SwipeRecyclerView? = null

    @Before
    fun setTopAdsAssertion() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        recyclerView = activityRule.activity.findViewById(R.id.home_account_user_fragment_rv)
        setCoachMarkToFalse()
    }

    @After
    fun tearDown() {
        topAdsAssertion.after()
        activityRule.finishActivity()
    }

    private fun waitForData() {
        //tempoary changed to 1 minute to test is long load time is the problem
        Thread.sleep(60000)
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    @Test
    fun testTopAdsNewHomeAccount() {
        waitForData()
        performUserJourney()
        topAdsAssertion.assert()
    }

    private fun performUserJourney() {
        recyclerView?.id?.let {
            Espresso.onView(ViewMatchers.withId(it)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
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
            if(item.isTopAds) {
                Espresso.onView(ViewMatchers.withId(recyclerView.id))
                        .perform(RecyclerViewActions.actionOnItemAtPosition<ProductItemViewHolder>(index, ViewActions.click()))
                topAdsCount++
            }
        }
    }

    private fun scrollHomeAccountRecyclerViewToPosition(recyclerView: RecyclerView, position: Int) {
        val layoutManager = recyclerView.layoutManager as StaggeredGridLayoutManager
        activityRule.runOnUiThread { layoutManager.scrollToPositionWithOffset(position, 0) }
    }

    private fun setCoachMarkToFalse() {
        AccountPreference(context).saveSettingValue(AccountConstants.KEY.KEY_SHOW_COACHMARK, false)
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
