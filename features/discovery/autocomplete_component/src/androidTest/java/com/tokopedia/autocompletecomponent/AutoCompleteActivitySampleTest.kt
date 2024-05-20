package com.tokopedia.autocompletecomponent

import android.content.Context
import android.content.Intent
import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import com.tokopedia.autocompletecomponent.searchbar.SearchBarView
import com.tokopedia.discovery.common.utils.SharedPrefsMpsLocalCache
import com.tokopedia.test.application.annotations.UiTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

private const val PREF_NAME = "MPSSharedPref"
@UiTest
class AutoCompleteActivitySampleTest {

    @get:Rule
    val activityRule = IntentsTestRule(
        AutoCompleteActivitySample::class.java,
        false,
        false,
    )

    private val context = InstrumentationRegistry.getInstrumentation().context

    @Before
    fun setUp() {
        val currentTime = System.currentTimeMillis() - (40L * 60 * 60 * 1000 * 24)
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref?.edit()
            ?.putBoolean(SharedPrefsMpsLocalCache.KEY_FIRST_MPS_SUCCESS, true)
            ?.putLong(SharedPrefsMpsLocalCache.TIME_FIRST_MPS_SUCCESS, currentTime)
            ?.apply()
        activityRule.launchActivity(Intent())
    }

    private fun getSearchBar() = onView(isAssignableFrom(SearchBarView::class.java))
    private fun getSearchBarEditText() = onView(isAssignableFrom(EditText::class.java))
    private fun getInitialStateContainer() = onView(withId(R.id.search_initial_state_container))
    private fun getSuggestionContainer() = onView(withId(R.id.search_suggestion_container))
    private fun getUnificationContainer() = onView(withId(R.id.autocomplete_unification_container))

    @Test
    fun open_auto_complete_will_initially_show_search_bar_and_initial_state() {
        getSearchBar().isDisplayed()
        getUnificationContainer().isDisplayed()
    }

    @Test
    fun typing_on_search_bar_will_show_suggestion_after_delay() {
        getSearchBarEditText().perform(typeText("samsung"))

        Thread.sleep(SearchBarView.SEARCH_BAR_DELAY_MS + 100)

        getUnificationContainer().isDisplayed()
    }

    @After
    fun tearDown() {
        activityRule.finishActivity()
    }
}
