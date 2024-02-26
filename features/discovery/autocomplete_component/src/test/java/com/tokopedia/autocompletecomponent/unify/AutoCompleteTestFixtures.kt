package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.stubExecute
import com.tokopedia.autocompletecomponent.unify.byteio.SugSessionId
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

const val AutoCompleteInitialStateSuccessJSON = "autocomplete/unify/initial_state.json"
const val AutoCompleteInitialStateRecentSuccessJSON = "autocomplete/unify/initial_state_recent.json"
const val AutoCompleteSuggestionSuccessJSON = "autocomplete/unify/suggestion_state.json"
const val AutoCompleteSuggestionRumahSuccessJSON = "autocomplete/unify/suggestion_state_rumah.json"
const val AutoCompleteSuggestionRumahBothSuccessJSON = "autocomplete/unify/suggestion_state_rumah_both.json"
const val HeadlineAdsRumahSuccessJSON = "autocomplete/unify/headline_ads_rumah.json"

abstract class AutoCompleteTestFixtures {
    // Consts
    protected val USER_SESSION_DEVICE_ID = "user_session_device_id_dummy"
    protected val USER_SESSION_USER_ID = "user_session_user_id_dummy"

    protected val initialStateUseCase = mockk<UseCase<UniverseSuggestionUnifyModel>>(relaxed = true)
    protected val suggestionStateUseCase =
        mockk<UseCase<UniverseSuggestionUnifyModel>>(relaxed = true)
    protected val deleteRecentUseCase = mockk<com.tokopedia.usecase.UseCase<Boolean>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    internal fun autoCompleteViewModel(state: AutoCompleteState = AutoCompleteState()): AutoCompleteViewModel =
        AutoCompleteViewModel(
            autoCompleteState = state,
            initialStateUseCase = initialStateUseCase,
            suggestionStateUseCase = suggestionStateUseCase,
            deleteRecentSearchUseCase = deleteRecentUseCase,
            userSession = userSession,
            mockk(relaxed = true),
            mockk(relaxed = true),
        )

    fun `Given Initial Use Case Is Successful`(
        model: UniverseSuggestionUnifyModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        initialStateUseCase.stubExecute(requestParamsSlot) returns model
    }

    fun `Given Suggestion Use Case Is Successful`(
        model: UniverseSuggestionUnifyModel,
        requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        suggestionStateUseCase.stubExecute(requestParamsSlot) returns model
    }

    fun `Given User Session`() {
        every {
            userSession.deviceId
        } returns USER_SESSION_DEVICE_ID

        every {
            userSession.userId
        } returns USER_SESSION_USER_ID
    }
}
