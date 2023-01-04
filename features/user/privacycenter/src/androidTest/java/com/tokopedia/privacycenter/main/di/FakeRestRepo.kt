package com.tokopedia.privacycenter.main.di

import com.google.gson.Gson
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.common.network.data.model.RestRequest
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.privacycenter.data.PrivacyPolicyListResponse
import com.tokopedia.privacycenter.ui.main.section.privacypolicy.PrivacyPolicyConst
import timber.log.Timber
import java.lang.reflect.Type

class FakeRestRepo : RestRepository {
    override suspend fun getResponse(request: RestRequest): RestResponse {
        Timber.d("processing request $request")
        return when {
            request.url.contains(PrivacyPolicyConst.GET_LIST_URL) -> {
                val obj = Gson().fromJson(responseJson, PrivacyPolicyListResponse::class.java)
                RestResponse(obj, 200, false).apply {
                    type = request.typeOfT
                    isError = false
                }
            }
            else -> TODO()
        }
    }

    override suspend fun getResponses(requests: List<RestRequest>): Map<Type, RestResponse> {
        return mapOf()
    }
}

private val responseJson = """
    {
        "resp_code": "200",
        "resp_desc": "success",
        "data": [
            {
                "section_id": 68,
                "section_title": "Kebijakan Privasi test",
                "topic_id": 1,
                "is_active": 1,
                "language": "id",
                "created_date": "2021-04-06T08:58:15.737Z",
                "last_update": "2021-04-06T08:58:21.685Z"
            },
            {
                "section_id": 69,
                "section_title": "Privacy test",
                "topic_id": 1,
                "is_active": 1,
                "language": "en",
                "created_date": "2021-04-06T08:58:15.747Z",
                "last_update": "2021-04-06T08:58:21.085Z"
            }
        ]
    }
""".trimIndent()
