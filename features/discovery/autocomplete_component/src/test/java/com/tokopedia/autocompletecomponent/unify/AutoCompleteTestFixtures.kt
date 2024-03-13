package com.tokopedia.autocompletecomponent.unify

import com.tokopedia.autocompletecomponent.stubExecute
import com.tokopedia.autocompletecomponent.unify.domain.model.UniverseSuggestionUnifyModel
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import rx.Subscriber
import com.tokopedia.usecase.UseCase as RxUseCase

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
    protected val deleteRecentUseCase = mockk<RxUseCase<Boolean>>(relaxed = true)
    protected val suggestionTrackerUseCase = mockk<RxUseCase<Void?>>(relaxed = true)
    protected val userSession = mockk<UserSessionInterface>(relaxed = true)
    protected val topAdsUrlHitter = mockk<TopAdsUrlHitter>(relaxed = true)
    internal fun autoCompleteViewModel(state: AutoCompleteState = AutoCompleteState()): AutoCompleteViewModel =
        AutoCompleteViewModel(
            autoCompleteState = state,
            initialStateUseCase = initialStateUseCase,
            suggestionStateUseCase = suggestionStateUseCase,
            deleteRecentSearchUseCase = deleteRecentUseCase,
            suggestionTrackerUseCase = suggestionTrackerUseCase,
            userSession = userSession,
            mockk(relaxed = true),
            topAdsUrlHitter = topAdsUrlHitter
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

    fun `Given Track Url Use Case Is Successful`(
        requestParamsSlot: CapturingSlot<RequestParams> = slot()
    ) {
        every { suggestionTrackerUseCase.execute(capture(requestParamsSlot), any()) }.answers {
            secondArg<Subscriber<Boolean>>().onStart()
            secondArg<Subscriber<Boolean>>().onNext(null)
        }
    }

    fun `Given Topads Track Impress Is Successful`(
        className: CapturingSlot<String>,
        url: CapturingSlot<String>,
        productId: CapturingSlot< String>,
        productName: CapturingSlot<String>,
        imageUrl: CapturingSlot<String>,
    ) {
        every { topAdsUrlHitter.hitImpressionUrl(
            capture(className),
            capture(url),
            capture(productId),
            capture(productName),
            capture(imageUrl),
        ) }.answers {}
    }

    fun `Given Topads Track Click Is Successful`(
        className: CapturingSlot<String>,
        url: CapturingSlot<String>,
        productId: CapturingSlot< String>,
        productName: CapturingSlot<String>,
        imageUrl: CapturingSlot<String>,
    ) {
        every { topAdsUrlHitter.hitClickUrl(
            capture(className),
            capture(url),
            capture(productId),
            capture(productName),
            capture(imageUrl),
        ) }.answers {}
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
