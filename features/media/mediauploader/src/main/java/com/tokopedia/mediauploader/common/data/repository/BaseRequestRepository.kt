package com.tokopedia.mediauploader.common.data.repository

import com.google.gson.GsonBuilder
import com.tokopedia.mediauploader.video.data.entity.ErrorResponse
import retrofit2.HttpException

open class BaseRequestRepository {

    fun getRequestId(t: HttpException): String {
        val builder = GsonBuilder().setLenient().create()
        val adapter = builder.getAdapter(ErrorResponse::class.java)

        return try {
            val body = t.response()?.errorBody()
            val err = adapter.fromJson(body?.charStream())

            err.header?.requestId ?: "-1"
        } catch (t: Throwable) {
            throw t
        }
    }
}
