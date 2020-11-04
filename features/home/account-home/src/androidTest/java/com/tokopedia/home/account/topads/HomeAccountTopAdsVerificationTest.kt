package com.tokopedia.home.account.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.account.R
import com.tokopedia.home.account.environment.InstrumentationHomeAccountTestActivity
import com.tokopedia.home.account.presentation.adapter.buyer.BuyerAccountAdapter
import com.tokopedia.home.account.presentation.viewholder.RecommendationProductViewHolder
import com.tokopedia.home.account.presentation.viewmodel.RecommendationProductViewModel
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HomeAccountTopAdsVerificationTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationHomeAccountTestActivity>(InstrumentationHomeAccountTestActivity::class.java) {
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
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })

    private var recyclerViewAccountHome: RecyclerView? = null

    @Before
    fun setTopAdsAssertion() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        recyclerViewAccountHome = activityRule.activity.findViewById(R.id.recycler_buyer)
    }

    @After
    fun tearDown() {
        topAdsAssertion?.after()
        activityRule?.finishActivity()
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    @Test
    fun testTopAdsHomeAccount() {
        waitForData()

        performUserJourney()
        topAdsAssertion?.assert()
    }

    private fun performUserJourney() {
        recyclerViewAccountHome?.id?.let {
            onView(withId(it)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        val visitableList = recyclerViewAccountHome?.getAccountHomeAdapter()?.list
        visitableList?.forEachIndexed { index, visitable ->
            scrollAndClickTopAds(index, visitable)
        }
    }

    private fun scrollAndClickTopAds(index: Int, visitable: Visitable<*>) {
        if (visitable is RecommendationProductViewModel && visitable.isTopAds()) {
            recyclerViewAccountHome?.let {
                when(it.findViewHolderForAdapterPosition(index)) {
                    is RecommendationProductViewHolder -> {
                        onView(withId(it.id)).perform(RecyclerViewActions.scrollToPosition<RecommendationProductViewHolder>(index))
                        onView(withId(it.id)).perform(RecyclerViewActions.actionOnItemAtPosition<RecommendationProductViewHolder>(index, ViewActions.click()))
                        topAdsCount++
                    }

                    else -> { }
                }
            }
        }
    }

    private fun RecommendationProductViewModel.isTopAds(): Boolean = product.isTopAds

    private fun RecyclerView?.getAccountHomeAdapter(): BuyerAccountAdapter {
        val inboxAdapter = this?.adapter as? BuyerAccountAdapter

        if (inboxAdapter == null) {
            val detailMessage = "Adapter is not ${BuyerAccountAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return inboxAdapter
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}