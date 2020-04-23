package com.tokopedia.autocomplete.initialstate.recentsearch

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable

class DeleteRecentSearchUseCase(
        private val initialStateRepository: InitialStateRepository
) : UseCase<Boolean>() {

    override fun createObservable(requestParams: RequestParams): Observable<Boolean> {
        return initialStateRepository.deleteRecentSearch(requestParams.parameters)
    }

    companion object {

        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_USER_ID = "user_id"
        private const val DEVICE_ID = "device_id"
        private const val UNIQUE_ID = "unique_id"
        private const val KEY_Q = "q"
        private const val KEY_DELETE_ALL = "clear_all"

        private const val DEFAULT_DELETE_ALL = "false"
        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"

        fun getParams(query: String, registrationId: String, userId: String): RequestParams {
            val params = RequestParams.create()
            params.putString(KEY_DEVICE, "android")
            params.putString(KEY_SOURCE, "searchbar")
            params.putString(KEY_DELETE_ALL, "false")
            params.putString(KEY_Q, query)
            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }
            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(DEVICE_ID, registrationId)
            return params
        }

        fun getParams(registrationId: String, userId: String): RequestParams {
            val params = RequestParams.create()
            params.putString(KEY_DEVICE, "android")
            params.putString(KEY_SOURCE, "searchbar")
            params.putString(KEY_DELETE_ALL, "true")
            var uniqueId = AuthHelper.getMD5Hash(registrationId)
            if (!TextUtils.isEmpty(userId)) {
                uniqueId = AuthHelper.getMD5Hash(userId)
                params.putString(KEY_USER_ID, userId)
            }
            params.putString(KEY_UNIQUE_ID, uniqueId)
            params.putString(DEVICE_ID, registrationId)
            return params
        }
    }
}