package com.tokopedia.search.robot

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.applink.internal.ApplinkConstInternalDiscovery
import com.tokopedia.search.R
import com.tokopedia.search.RecyclerViewHasItemIdlingResource
import com.tokopedia.search.result.presentation.view.activity.SearchActivity

internal class SearchRobot(private val activityRule: ActivityTestRule<SearchActivity>) {

    private val recyclerViewId = R.id.recyclerview
    private var recyclerView: RecyclerView? = null
    private var recyclerViewIdlingResource: IdlingResource? = null

    fun withKeyword(keyword: String) {
        withQueryParams("q=$keyword")
    }

    fun withQueryParams(queryParams: String) {
        val intent = Intent(InstrumentationRegistry.getInstrumentation().targetContext, SearchActivity::class.java).also {
            it.data = Uri.parse(ApplinkConstInternalDiscovery.SEARCH_RESULT + "?" + queryParams)
        }

        activityRule.launchActivity(intent)

        setupIdlingResource()

        intending(isInternal()).respondWith(ActivityResult(RESULT_OK, null))

        onView(withId(recyclerViewId)).check(matches(isDisplayed()))
    }

    private fun setupIdlingResource() {
        recyclerView = activityRule.activity.findViewById(recyclerViewId)
        recyclerViewIdlingResource = RecyclerViewHasItemIdlingResource(recyclerView)

        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
    }

    infix fun interact(action: SearchInteractionRobot.() -> Unit) = SearchInteractionRobot(
            listOf(recyclerViewIdlingResource),
            activityRule.activity
    ).apply {
        action()
    }
}