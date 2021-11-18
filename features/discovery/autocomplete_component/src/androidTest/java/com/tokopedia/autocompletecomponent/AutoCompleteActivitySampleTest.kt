package com.tokopedia.autocompletecomponent

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.autocompletecomponent.searchbar.SearchBarView
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Rule
import org.junit.Test

@UiTest
class AutoCompleteActivitySampleTest {

    @get:Rule
    val activityRule = IntentsTestRule(
        AutoCompleteActivitySample::class.java,
        false,
        true,
    )

    private fun getSearchBar() = onView(isAssignableFrom(SearchBarView::class.java))
    private fun getSearchBarEditText() = onView(isAssignableFrom(EditText::class.java))
    private fun getInitialStateContainer() = onView(withId(R.id.search_initial_state_container))
    private fun getSuggestionContainer() = onView(withId(R.id.search_suggestion_container))

    @Test
    fun open_auto_complete_will_initially_show_search_bar_and_initial_state() {
        getSearchBar().isDisplayed()
        getInitialStateContainer().isDisplayed()
    }

    @Test
    fun typing_on_search_bar_will_show_suggestion_after_delay() {
        getSearchBarEditText().perform(typeText("samsung"))

        Thread.sleep(SearchBarView.SEARCH_BAR_DELAY_MS + 100)

        getSuggestionContainer().isDisplayed()
    }
}