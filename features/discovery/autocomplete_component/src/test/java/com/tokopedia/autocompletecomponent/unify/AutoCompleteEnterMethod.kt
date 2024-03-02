package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CANCEL
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.CLICK_SEARCH_BAR
import com.tokopedia.analytics.byteio.search.AppLogSearch.ParamValue.ENTER
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.discovery.common.constants.SearchApiConst.Companion.ENTER_METHOD
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class AutoCompleteEnterMethod: AutoCompleteTestFixtures() {

    @Test
    fun `Enter method is taken from params`() {
        val enterMethod = CLICK_SEARCH_BAR
        val autoCompleteViewModel = autoCompleteViewModel(AutoCompleteState(
            mapOf(ENTER_METHOD to enterMethod)
        ))

        autoCompleteViewModel.onScreenInitialized()

        assertThat(autoCompleteViewModel.stateValue.enterMethod, `is`(enterMethod))
    }

    @Test
    fun `empty enter method`() {
        val autoCompleteViewModel = autoCompleteViewModel()

        autoCompleteViewModel.onScreenInitialized()

        assertThat(autoCompleteViewModel.stateValue.enterMethod, `is`(""))
    }

    @Test
    fun `enter method from initial state to suggestion is ENTER`() {
        val autoCompleteViewModel = autoCompleteViewModel()
        autoCompleteViewModel.onScreenInitialized()

        autoCompleteViewModel.onScreenUpdateParameter(mapOf(SearchApiConst.Q to "samsung"))

        assertThat(autoCompleteViewModel.stateValue.enterMethod, `is`(ENTER))
    }

    @Test
    fun `enter method from suggestion to initial state is CANCEL`() {
        val autoCompleteViewModel = autoCompleteViewModel(
            AutoCompleteState(mapOf(
                SearchApiConst.Q to "samsung"
            ))
        )
        autoCompleteViewModel.onScreenInitialized()

        autoCompleteViewModel.onScreenUpdateParameter(mapOf(SearchApiConst.Q to ""))

        assertThat(autoCompleteViewModel.stateValue.enterMethod, `is`(CANCEL))
    }

    @Test
    fun `enter method from suggestion to suggestion will not change`() {
        val autoCompleteViewModel = autoCompleteViewModel(AutoCompleteState(
            parameter = mapOf(
                SearchApiConst.Q to "sam",
                ENTER_METHOD to CLICK_SEARCH_BAR
            )
        ))
        autoCompleteViewModel.onScreenInitialized()

        autoCompleteViewModel.onScreenUpdateParameter(mapOf(SearchApiConst.Q to "samsung"))

        assertThat(autoCompleteViewModel.stateValue.enterMethod, `is`(CLICK_SEARCH_BAR))
    }
}
