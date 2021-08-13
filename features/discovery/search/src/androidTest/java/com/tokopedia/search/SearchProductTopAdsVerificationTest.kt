package com.tokopedia.search

import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
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
import com.tokopedia.search.result.presentation.model.ProductItemDataView
import com.tokopedia.search.result.presentation.view.activity.SearchActivity
import com.tokopedia.search.result.presentation.view.adapter.viewholder.product.ProductItemViewHolder
import com.tokopedia.test.application.assertion.topads.TopAdsAssertion
import com.tokopedia.test.application.environment.callback.TopAdsVerificatorInterface
import com.tokopedia.test.application.util.InstrumentationAuthHelper.loginInstrumentationTestTopAdsUser
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class SearchProductTopAdsVerficationTest {

    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val activityRule = IntentsTestRule(SearchActivity::class.java, false, false)

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null
    private var topAdsCount = 0
    private val topAdsAssertion = TopAdsAssertion(context, TopAdsVerificatorInterface { topAdsCount })

    @Before
    fun setUp() {
        loginInstrumentationTestTopAdsUser()

        disableOnBoarding(context)

        activityRule.launchActivity(createIntent())

        setupIdlingResource()

        Intents.intending(IntentMatchers.isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)

        activityRule.activity.finish()

        topAdsAssertion.after()
    }

    @Test
    fun testTopAdsUrlTracking() {
        performUserJourney()
        topAdsAssertion.assert()
    }

    private fun performUserJourney() {
        onView(withId(recyclerViewId)).check(matches(isDisplayed()))

        val visitableList = recyclerView.getProductListAdapter().itemList
        visitableList.forEachIndexed(this::scrollAndClickTopAds)
    }

    private fun scrollAndClickTopAds(index: Int, visitable: Visitable<*>) {
        if (visitable is ProductItemDataView && visitable.isTopAdsOrOrganicAds()) {
            topAdsCount++

            onView(withId(recyclerViewId)).perform(scrollToPosition<ProductItemViewHolder>(index))
            onView(withId(recyclerViewId)).perform(actionOnItemAtPosition<ProductItemViewHolder>(index, click()))
        }
    }

    private fun ProductItemDataView.isTopAdsOrOrganicAds() = isTopAds || isOrganicAds
}