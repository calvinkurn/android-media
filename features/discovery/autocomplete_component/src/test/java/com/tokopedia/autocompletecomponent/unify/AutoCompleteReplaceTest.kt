package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyModel
import com.tokopedia.usecase.RequestParams
import io.mockk.slot
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class AutoCompleteReplaceTest : AutoCompleteTestFixtures() {

    @Test
    fun `on cta replace should replace search bar`() {
        val model =
            AutoCompleteSuggestionSuccessJSON.jsonToObject<SuggestionUnifyModel>().data
        val itemWithReplaceCta = model.data.first { it.cta.action == "replace" }
        val viewModel = autoCompleteViewModel()
        val requestParamsSlot = slot<RequestParams>()

        `Given Initial Use Case Is Successful`(model, requestParamsSlot)

        viewModel.onScreenInitialized()

        `When Action Replace`(viewModel, itemWithReplaceCta)

        `Then Viewmodel state actionReplaceKeyword should be item's title text`(
            viewModel,
            itemWithReplaceCta
        )
    }

    private fun `Then Viewmodel state actionReplaceKeyword should be item's title text`(
        viewModel: AutoCompleteViewModel,
        itemWithReplaceCta: SuggestionUnify
    ) {
        assertThat(
            viewModel.stateFlow.value.actionReplaceKeyword,
            `is`(itemWithReplaceCta.title.text)
        )
    }

    private fun `When Action Replace`(
        viewModel: AutoCompleteViewModel,
        itemWithReplaceCta: SuggestionUnify
    ) {
        viewModel.onAutocompleteItemAction(itemWithReplaceCta)
    }
}
