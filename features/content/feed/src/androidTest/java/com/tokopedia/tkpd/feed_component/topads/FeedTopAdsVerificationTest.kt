package com.tokopedia.tkpd.feed_component.topads

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.detail.FeedDetailActivity
import com.tokopedia.feedplus.presentation.adapter.FeedContentAdapter
import com.tokopedia.feedplus.presentation.adapter.viewholder.FeedPostImageViewHolder
import com.tokopedia.feedplus.presentation.model.FeedCardImageContentModel
import com.tokopedia.test.application.annotations.TopAdsTest
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.util.InstrumentationAuthHelper
import com.tokopedia.test.application.util.setupTopAdsDetector
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Created By : Muhammad Furqan on 23/01/24
 */
@TopAdsTest
class FeedTopAdsVerificationTest {

    @get:Rule
    val activityRule =
        object : IntentsTestRule<FeedDetailActivity>(FeedDetailActivity::class.java) {
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
    private val topAdsAssertion = TopAdsAssertion(context) { topAdsCount }

    @Before
    fun setup() {
        Intents.intending(IntentMatchers.isInternal()).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                null
            )
        )
    }

    @After
    fun tearDown() {
        topAdsAssertion.after()
        activityRule.finishActivity()
    }

    @Test
    fun validateFeedTopAds() {
        waitForData()
        performUserJourney()
    }

    private fun performUserJourney() {
        onView(withId(R.id.rv_feed_post))
            .check(ViewAssertions.matches(isDisplayed()))

        val recyclerView = activityRule.activity.findViewById<RecyclerView>(R.id.rv_feed_post)
        val itemList = recyclerView.getContentAdapter().currentList
        itemList.forEachIndexed { index, item ->
            scrollAndClickTopAds(index, item, recyclerView)
        }
    }

    private fun scrollAndClickTopAds(
        index: Int,
        item: FeedContentAdapter.Item,
        recyclerView: RecyclerView
    ) {
        if (item.data is FeedCardImageContentModel && (item.data as FeedCardImageContentModel).isTopAds) {
            onView(withId(recyclerView.id)).perform(
                RecyclerViewActions.scrollToPosition<FeedPostImageViewHolder>(
                    index
                )
            )
            onView(withId(recyclerView.id)).perform(
                RecyclerViewActions.actionOnItemAtPosition<FeedPostImageViewHolder>(
                    index,
                    click()
                )
            )
            topAdsCount++
        }
    }

    private fun waitForData() {
        Thread.sleep(10000)
    }

    private fun login() {
        InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser()
    }

    private fun RecyclerView?.getContentAdapter(): FeedContentAdapter =
        this?.adapter as? FeedContentAdapter
            ?: throw AssertionError("Adapter is not ${FeedContentAdapter::class.java.simpleName}")
}
