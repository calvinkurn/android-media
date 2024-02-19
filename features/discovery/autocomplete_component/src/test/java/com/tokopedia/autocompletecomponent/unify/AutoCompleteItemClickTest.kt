package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class AutoCompleteItemClickTest : AutoCompleteTestFixtures() {
    @Test
    fun `on item clicked should update state to navigate`() {
        val clickedItemDataView =
            AutoCompleteUnifyDataView(domainModel = SuggestionUnify(applink = "test_applink"))
        val viewModel = autoCompleteViewModel()
        `When AutoComplete Item is Clicked`(viewModel, clickedItemDataView)
        `Then Assert State Navigation Value is Changed According to Clicked Item Data View`(
            viewModel,
            clickedItemDataView
        )
    }

    private fun `Then Assert State Navigation Value is Changed According to Clicked Item Data View`(
        viewModel: AutoCompleteViewModel,
        clickedItemDataView: AutoCompleteUnifyDataView
    ) {
        assertThat(
            viewModel.stateValue.navigate?.applink,
            equalTo(clickedItemDataView.domainModel.applink)
        )
    }

    private fun `When AutoComplete Item is Clicked`(
        viewModel: AutoCompleteViewModel,
        clickedItemDataView: AutoCompleteUnifyDataView
    ) {
        viewModel.onAutoCompleteItemClick(item = clickedItemDataView)
    }

    @Test
    fun `After Navigated, Update Navigation State in ViewModel State`() {
        val navigateState =
            AutoCompleteState(navigate = AutoCompleteNavigate(applink = "test_applink"))
        val viewModel = autoCompleteViewModel(navigateState)
        `When Navigation is Done`(viewModel)
        `Then Assert That State Navigate is Null`(viewModel)
    }

    private fun `Then Assert That State Navigate is Null`(viewModel: AutoCompleteViewModel) {
        assertThat(viewModel.stateValue.navigate, equalTo(null))
    }

    private fun `When Navigation is Done`(viewModel: AutoCompleteViewModel) {
        viewModel.onNavigated()
    }
}
