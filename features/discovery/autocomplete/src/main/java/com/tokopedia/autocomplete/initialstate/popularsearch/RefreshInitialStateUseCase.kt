package com.tokopedia.autocomplete.initialstate.popularsearch

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.autocomplete.initialstate.InitialStateData
import com.tokopedia.autocomplete.initialstate.InitialStateRepository
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.util.*

class RefreshInitialStateUseCase(
        private val initialStateRepository: InitialStateRepository
) : UseCase<List<InitialStateData>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<InitialStateData>> {
        return initialStateRepository.getInitialStateData(requestParams.parameters)
    }

    companion object {

        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_COUNT = "count"
        private const val KEY_USER_ID = "user_id"
        private const val DEFAULT_DEVICE = "android"
        private const val DEFAULT_SOURCE = "searchbar"
        private const val DEFAULT_COUNT = "5"
        private const val DEVICE_ID = "device_id"

        fun getParams(query: String, registrationId: String, userId: String): RequestParams {
            val searchParameter = HashMap<String, Any>()
            searchParameter[SearchApiConst.Q] = query
            return getParams(searchParameter, registrationId, userId)
        }

        fun getParams(searchParameter: Map<String, Any>, registrationId: String, userId: String): RequestParams {
            val params = RequestParams.create()

            params.putAll(searchParameter)

            params.putString(KEY_DEVICE, DEFAULT_DEVICE)
            params.putString(KEY_SOURCE, DEFAULT_SOURCE)
            params.putString(KEY_COUNT, DEFAULT_COUNT)
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