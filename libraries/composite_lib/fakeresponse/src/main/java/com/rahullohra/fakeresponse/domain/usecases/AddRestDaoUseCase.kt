package com.rahullohra.fakeresponse.domain.usecases

import android.text.TextUtils
import com.rahullohra.fakeresponse.data.models.Either
import com.rahullohra.fakeresponse.db.entities.RestResponse
import com.rahullohra.fakeresponse.domain.exceptions.EmptyException
import com.rahullohra.fakeresponse.domain.exceptions.NoResponseException
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.presentaiton.viewmodels.data.AddRestData
import java.util.*

class AddRestDaoUseCase(val repository: RestRepository) : BaseUseCase<RestRepository>(repository) {

    fun addRestRecord(data: AddRestData): Long {
        if (TextUtils.isEmpty(data.url)) throw EmptyException("url cannot be empty")
        if (TextUtils.isEmpty(data.methodName)) throw EmptyException("httpMethod cannot be empty")
        if (TextUtils.isEmpty(data.response)) throw NoResponseException()
        val either = isJSONValid(data.response!!)
        return when (either) {
            is Either.Left -> throw either.a
            else -> repository.addToDb(gqlDataToRest(data))
        }
    }

    protected fun gqlDataToRest(data: AddRestData): RestResponse {
        val date = Date()

        return RestResponse(
            url = data.url!!,
            httpMethod = data.methodName,
            createdAt = date.time,
            updatedAt = date.time,
            enabled = false,
            response = data.response!!
        )
    }
}