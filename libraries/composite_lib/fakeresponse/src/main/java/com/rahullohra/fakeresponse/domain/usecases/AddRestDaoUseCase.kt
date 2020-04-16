package com.rahullohra.fakeresponse.domain.usecases

import android.text.TextUtils
import com.rahullohra.fakeresponse.Extensions.isJSONValid
import com.rahullohra.fakeresponse.data.models.Either
import com.rahullohra.fakeresponse.db.entities.RestRecord
import com.rahullohra.fakeresponse.domain.exceptions.EmptyException
import com.rahullohra.fakeresponse.domain.exceptions.NoResponseException
import com.rahullohra.fakeresponse.domain.repository.RestRepository
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddRestData
import com.rahullohra.fakeresponse.presentation.viewmodels.data.toRestRecord

class AddRestDaoUseCase(val repository: RestRepository) : BaseUseCase<RestRepository>(repository) {

    fun addRestRecord(data: AddRestData): Long {
        validateData(data)
        return repository.addToDb(data.toRestRecord())
    }

    fun updateRestRecord(id:Int, data: AddRestData) {
        validateData(data)
        val restResponse = getRecordFromTable(id)
        restResponse.httpMethod = data.methodName
        return repository.updateResponse(data.toRestRecord(id))
    }

    private fun validateData(data: AddRestData) {
        if (TextUtils.isEmpty(data.url)) throw EmptyException("url cannot be empty")
        if (TextUtils.isEmpty(data.methodName)) throw EmptyException("httpMethod cannot be empty")
        if (TextUtils.isEmpty(data.response)) throw NoResponseException()
        validateJson(data)
    }

    private fun validateJson(data: AddRestData) {
        val either = isJSONValid(data.response!!)
        if (either is Either.Left) {
            throw either.a
        }
    }

    fun getRecordFromTable(id: Int): RestRecord {
        return repository.getResponse(id)
    }
}