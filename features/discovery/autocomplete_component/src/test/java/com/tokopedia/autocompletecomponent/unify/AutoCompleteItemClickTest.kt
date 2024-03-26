package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker.SuggestionTrackerUseCase
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.util.AutoCompleteNavigate
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.slot
import io.mockk.verify
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class AutoCompleteItemClickTest : AutoCompleteTestFixtures() {
    @Test
    fun `on item clicked should update state to navigate`() {
        val clickedItemDataView = AutoCompleteUnifyDataView(
                domainModel = SuggestionUnify(
                    applink = "test_applink",
                    featureId = "keyword",
                )
            )
        val viewModel = autoCompleteViewModel()
        `When AutoComplete Item is Clicked`(viewModel, clickedItemDataView)
        `Then Assert State Navigation Value is Changed According to Clicked Item Data View`(viewModel, clickedItemDataView)
    }

    private fun `Then Assert State Navigation Value is Changed According to Clicked Item Data View`(viewModel: AutoCompleteViewModel, clickedItemDataView: AutoCompleteUnifyDataView) {
        assertThat(viewModel.stateValue.navigate?.applink, equalTo(clickedItemDataView.domainModel.applink))
        assertThat(
            viewModel.stateValue.navigate?.featureId,
            equalTo(clickedItemDataView.domainModel.featureId)
        )
    }

    private fun `When AutoComplete Item is Clicked`(viewModel: AutoCompleteViewModel, clickedItemDataView: AutoCompleteUnifyDataView) {
        viewModel.onAutoCompleteItemClick(item = clickedItemDataView, "")
    }

    @Test
    fun `After Navigated, Update Navigation State in ViewModel State`() {
        val navigateState = AutoCompleteState(navigate = AutoCompleteNavigate(applink = "test_applink"))
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

    @Test
    fun `on item clicked should hit suggestionTrack state`() {
        val clickedItemDataView = AutoCompleteUnifyDataView(domainModel = SuggestionUnify(applink = "test_applink"))
        val viewModel = autoCompleteViewModel()
        val requestParamsSlot = slot<RequestParams>()
        `Given Track Url Use Case Is Successful`(requestParamsSlot)
        `When AutoComplete Item is Clicked`(viewModel, clickedItemDataView)

        `Then assert suggestion tracker called once with correct param`(clickedItemDataView, requestParamsSlot)
    }

    private fun `Then assert suggestion tracker called once with correct param`(
        clickedItemDataView: AutoCompleteUnifyDataView, requestParamsSlot: CapturingSlot<RequestParams>
    ) {
        verify(exactly = 1) { suggestionTrackerUseCase.execute(any(), any()) }
        val expectedParam = SuggestionTrackerUseCase.getParams(
            clickedItemDataView.domainModel.tracking.trackerUrl,
            userSession.deviceId, userSession.userId
        )
        assertThat(
            requestParamsSlot.captured.getString("url_tracker", ""),
            equalTo(expectedParam.parameters["url_tracker"]
            )
        )
    }
}
