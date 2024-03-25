package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.jsonToObject
import com.tokopedia.autocompletecomponent.unify.domain.model.InitialStateUnifyModel
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnify
import com.tokopedia.autocompletecomponent.unify.domain.model.SuggestionUnifyModel
import com.tokopedia.autocompletecomponent.util.FEATURE_ID_RECENT_SEARCH
import com.tokopedia.usecase.RequestParams
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import junit.framework.Assert.assertNull
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test
import rx.Subscriber

class AutoCompleteDeleteTest : AutoCompleteTestFixtures() {
    @Test
    fun `on cta delete clicked should delete autocomplete entry`() {
        val model =
            AutoCompleteSuggestionSuccessJSON.jsonToObject<SuggestionUnifyModel>().data
        val itemWithDeleteCta = model.data.first { it.cta.action == "delete" }
        val viewModel = autoCompleteViewModel()
        val requestParamsSlot = slot<RequestParams>()

        `Given User Session`()
        `Given Initial Use Case Is Successful`(model, requestParamsSlot)

        `Given delete use case is successful`()

        viewModel.onScreenInitialized()

        `When cta delete search entry`(viewModel, itemWithDeleteCta)

        `Then verify delete recent use case is executed`(requestParamsSlot)

        `Then assert user session parameter is correct`(requestParamsSlot)

        `Then assert captured parameter is item going to be deleted`(
            requestParamsSlot,
            itemWithDeleteCta
        )

        `Then assert that item is not in state value`(viewModel, itemWithDeleteCta)
    }

    private fun `Given delete use case is successful`() {
        every { deleteRecentUseCase.execute(any(), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onNext(true)
        }
    }

    private fun `Then assert captured parameter is item going to be deleted`(
        requestParamsSlot: CapturingSlot<RequestParams>,
        itemWithDeleteCta: SuggestionUnify
    ) {
        assertThat(
            requestParamsSlot.captured.parameters["id"],
            `is`(itemWithDeleteCta.suggestionId)
        )
        assertThat(
            requestParamsSlot.captured.parameters["type"],
            `is`(itemWithDeleteCta.featureType)
        )
    }

    private fun `Then assert that item is not in state value`(
        viewModel: AutoCompleteViewModel,
        itemWithDeleteCta: SuggestionUnify
    ) {
        assertNull(
            viewModel.stateFlow.value.resultList.find {
                it.domainModel.suggestionId == itemWithDeleteCta.suggestionId
            }
        )
    }

    private fun `Then assert user session parameter is correct`(
        requestParamsSlot: CapturingSlot<RequestParams>
    ) {
        assertThat(requestParamsSlot.captured.parameters["user_id"], `is`(USER_SESSION_USER_ID))
        assertThat(requestParamsSlot.captured.parameters["device_id"], `is`(USER_SESSION_DEVICE_ID))
    }

    private fun `Then verify delete recent use case is executed`(
        requestParamsSlot: CapturingSlot<RequestParams>
    ) {
        verify(exactly = 1) { deleteRecentUseCase.execute(capture(requestParamsSlot), any()) }
    }

    private fun `When cta delete search entry`(
        viewModel: AutoCompleteViewModel,
        itemWithDeleteCta: SuggestionUnify
    ) {
        viewModel.onAutocompleteItemAction(itemWithDeleteCta)
    }

    @Test
    fun `on label delete all clicked should delete all autocomplete recent`() {
        val model =
            AutoCompleteInitialStateRecentSuccessJSON.jsonToObject<InitialStateUnifyModel>().data
        val itemWithDeleteLabel = model.data.first { it.label.action == "delete" }
        val viewModel = autoCompleteViewModel()
        val requestParamsSlot = slot<RequestParams>()

        `Given User Session`()
        `Given Initial Use Case Is Successful`(model, requestParamsSlot)

        `Given delete use case is successful`()

        viewModel.onScreenInitialized()

        `When cta delete search entry`(viewModel, itemWithDeleteLabel)

        `Then verify delete recent use case is executed`(requestParamsSlot)

        `Then assert user session parameter is correct`(requestParamsSlot)

        `Then assert no recent search feature id exist in state`(viewModel)
    }

    private fun `Then assert no recent search feature id exist in state`(viewModel: AutoCompleteViewModel) {
        assertNull(
            viewModel.stateFlow.value.resultList.find {
                it.domainModel.featureId == FEATURE_ID_RECENT_SEARCH
            }
        )
    }
}
