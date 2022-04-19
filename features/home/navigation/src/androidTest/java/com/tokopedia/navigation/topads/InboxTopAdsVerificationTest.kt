package com.tokopedia.navigation.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.R
import com.tokopedia.navigation.domain.model.Recommendation
import com.tokopedia.navigation.environment.InstrumentationInboxTestActivity
import com.tokopedia.navigation.presentation.adapter.InboxAdapter
import com.tokopedia.navigation.presentation.adapter.viewholder.InboxTopAdsBannerViewHolder
import com.tokopedia.navigation.presentation.adapter.viewholder.RecommendationViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InboxTopAdsVerificationTest {

    @get:Rule
    var activityRule = object : IntentsTestRule<InstrumentationInboxTestActivity>(InstrumentationInboxTestActivity::class.java) {
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

    private var recyclerViewInbox: RecyclerView? = null

    @Before
    fun setTopAdsAssertion() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        recyclerViewInbox = activityRule.activity.findViewById(R.id.recyclerview)
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
    fun testTopAdsInbox() {
        waitForData()

        performUserJourney()
        topAdsAssertion?.assert()
    }

    private fun performUserJourney() {
        recyclerViewInbox?.id?.let {
            onView(withId(it)).check(matches(isDisplayed()))
        }

        val visitableList = recyclerViewInbox?.getInboxAdapter()?.list
        visitableList?.forEachIndexed(this::scrollAndClickTopAds)
    }

    private fun scrollAndClickTopAds(index: Int, visitable: Visitable<*>) {
        if (visitable is Recommendation && visitable.isTopAds()) {
            recyclerViewInbox?.let {
                when(it.findViewHolderForAdapterPosition(index)) {
                    is RecommendationViewHolder -> {
                        onView(withId(it.id)).perform(scrollToPosition<RecommendationViewHolder>(index))
                        onView(withId(it.id)).perform(actionOnItemAtPosition<RecommendationViewHolder>(index, click()))
                        topAdsCount++
                    }

                    is InboxTopAdsBannerViewHolder -> {
                        onView(withId(it.id)).perform(scrollToPosition<InboxTopAdsBannerViewHolder>(index))
                        onView(withId(it.id)).perform(actionOnItemAtPosition<InboxTopAdsBannerViewHolder>(index, click()))
                        topAdsCount++
                    }
                    else -> { }
                }
            }
        }
    }

    private fun Recommendation.isTopAds(): Boolean = recommendationItem.isTopAds

    private fun RecyclerView?.getInboxAdapter(): InboxAdapter {
        val inboxAdapter = this?.adapter as? InboxAdapter

        if (inboxAdapter == null) {
            val detailMessage = "Adapter is not ${InboxAdapter::class.java.simpleName}"
            throw AssertionError(detailMessage)
        }

        return inboxAdapter
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }
}