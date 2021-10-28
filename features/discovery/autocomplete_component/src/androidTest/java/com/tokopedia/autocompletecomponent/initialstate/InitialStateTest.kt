package com.tokopedia.autocompletecomponent.initialstate

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.intent.rule.IntentsTestRule
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.cassavatest.CassavaTestRule
import org.junit.Rule
import org.junit.Test

class InitialStateTest {

    companion object {
        private const val ANALYTIC_VALIDATOR_QUERY_FILE_NAME =
            "tracker/autocomplete/initial_state.json"
    }

    @get:Rule
    val cassavaTestRule = CassavaTestRule()

    @get:Rule
    val activityTestRule = IntentsTestRule(
        InitialStateActivityTest::class.java,
        false,
        true,
    )

    private val recyclerViewId = R.id.recyclerViewInitialState

    @Test
    fun testInitialState() {
        val recyclerView = findViewById<RecyclerView>(recyclerViewId)
    }

    private fun <T: View?> findViewById(@IdRes id: Int): T =
        activityTestRule.activity.findViewById<T>(id)
}