package com.tokopedia.autocompletecomponent.suggestion.domain.suggestiontracker

import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.autocompletecomponent.suggestion.domain.SuggestionRepository
import com.tokopedia.autocompletecomponent.util.UrlParamHelper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class SuggestionTrackerUseCase(
        private val suggestionRepository: SuggestionRepository
) : UseCase<Void?>() {

    override fun createObservable(requestParams: RequestParams?): Observable<Void?> {
        val urlTracker = requestParams?.getString(URL_TRACKER, "") ?: ""

        return suggestionRepository.hitSuggestionUrlTracker(urlTracker)
    }

    companion object {
        const val URL_TRACKER = "url_tracker"
        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_COUNT = "count"
        private const val KEY_USER_ID = "user_id"
        private const val DEFAULT_DEVICE = "android"
        private const val DEFAULT_SOURCE = "searchbar"
        private const val DEFAULT_COUNT = "5"
        private const val DEVICE_ID = "device_id"

        fun getParams(url: String, registrationId: String, userId: String): RequestParams {
            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!textIsEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
            }

            val appendedParams = mapOf<String,String>(
                    KEY_DEVICE to DEFAULT_DEVICE,
                    KEY_SOURCE to DEFAULT_SOURCE,
                    KEY_COUNT to DEFAULT_COUNT,
                    KEY_USER_ID to userId,
                    KEY_UNIQUE_ID to uniqueId,
                    DEVICE_ID to registrationId
            )
            val appendedString = UrlParamHelper.generateUrlParamString(appendedParams)
            val urlTracker = url + "&${appendedString}"
            val params = RequestParams.create()
            params.putString(URL_TRACKER, urlTracker)
            return params
        }

        private fun textIsEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }
    }
}