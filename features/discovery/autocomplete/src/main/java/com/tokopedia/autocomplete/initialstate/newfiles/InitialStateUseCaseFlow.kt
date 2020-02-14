package com.tokopedia.autocomplete.initialstate.newfiles

import android.text.TextUtils
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.flow.Flow
import java.util.*

class InitialStateUseCaseFlow(
        private val initialStateRepository: InitialStateRepository
) {

    companion object{
        private const val KEY_DEVICE = "device"
        private const val KEY_SOURCE = "source"
        private const val KEY_UNIQUE_ID = "unique_id"
        private const val KEY_COUNT = "count"
        private const val KEY_USER_ID = "user_id"
        private const val DEFAULT_DEVICE = "android"
        private const val DEFAULT_SOURCE = "searchbar"
        private const val DEFAULT_COUNT = "5"
        private const val DEVICE_ID = "device_id"
    }

    fun getInitialStateDataFlow(searchParameter: Map<String, Any>, registrationId: String, userId: String): Flow<List<InitialStateData>> {
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

        return initialStateRepository.getInitialStateDataFlow(searchParameter as HashMap<String, Any>)
    }
}