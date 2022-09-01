package com.tokopedia.entertainment

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.entertainment.search.activity.EventSearchActivity
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventListViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchLocationListViewHolder
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.test.application.environment.interceptor.mock.MockModelConfig
import com.tokopedia.test.application.util.ResourcePathUtil
import com.tokopedia.test.application.util.setupGraphqlMockResponse
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchEventActivityTest {
    private val graphqlCacheManager = GraphqlCacheManager()

    @get:Rule
    var activityRule = ActivityTestRule(EventSearchActivity::class.java, false, false)

    @get:Rule
    var cassavaRule = CassavaTestRule()

    @Before
    fun setup() {
        Intents.init()
        graphqlCacheManager.deleteAll()
        setupGraphqlMockResponse {
            addMockResponse(
                    KEY_EVENT_CHILD,
                    ResourcePathUtil.getJsonFromResource(PATH_RESPONSE_SEARCH),
                    MockModelConfig.FIND_BY_CONTAINS)
        }

        val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
        val intent = Intent(targetContext, EventSearchActivity::class.java)
        activityRule.launchActivity(intent)
    }

    @Test
    fun validateSearchCity() {
        Thread.sleep(5000)
        search_keyword()
        click_city()
        assertThat(cassavaRule.validate(ENTERTAINMENT_EVENT_SEARCH_VALIDATOR_QUERY), hasAllSuccess())

    }

    @Test
    fun validateSearchEvent() {
        Thread.sleep(5000)
        search_keyword()
        click_event()
        assertThat(cassavaRule.validate(ENTERTAINMENT_EVENT_SEARCH_PRODUCT_VALIDATOR_QUERY), hasAllSuccess())
    }

    fun search_keyword() {
        Thread.sleep(3000)
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(click())
        onView(withId(com.tokopedia.unifycomponents.R.id.searchbar_textfield)).perform(typeText("Hong Kong"))
    }

    fun click_city() {
        blockIntents()
        Thread.sleep(5000)
        onView(withId(R.id.recycler_view_location)).perform(RecyclerViewActions.actionOnItemAtPosition<SearchLocationListViewHolder>(0, click()))
        Thread.sleep(3000)
    }

    fun click_event() {
        blockIntents()
        Thread.sleep(5000)
        onView(withId(R.id.recycler_view_kegiatan)).perform(RecyclerViewActions.actionOnItemAtPosition<SearchEventListViewHolder>(0, click()))
        Thread.sleep(3000)
    }

    private fun blockIntents(){
        intending(isInternal()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
    }

    @After
    fun cleanUp() {
        Intents.release()
    }

    companion object {
        private const val ENTERTAINMENT_EVENT_SEARCH_VALIDATOR_QUERY = "tracker/event/searchpageevent.json"
        private const val ENTERTAINMENT_EVENT_SEARCH_PRODUCT_VALIDATOR_QUERY = "tracker/event/searchpageeventproduct.json"

        private const val KEY_EVENT_CHILD = "searchEventLocation"
        private const val PATH_RESPONSE_SEARCH = "event_search.json"
    }
}