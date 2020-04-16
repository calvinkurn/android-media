package com.rahullohra.fakeresponse.domain.usecases

import android.text.TextUtils
import com.rahullohra.fakeresponse.Extensions
import com.rahullohra.fakeresponse.data.models.Either
import com.rahullohra.fakeresponse.db.entities.GqlRecord
import com.rahullohra.fakeresponse.domain.exceptions.EmptyException
import com.rahullohra.fakeresponse.domain.exceptions.NoResponseException
import com.rahullohra.fakeresponse.domain.repository.GqlRepository
import com.rahullohra.fakeresponse.presentation.viewmodels.data.AddGqlData
import com.rahullohra.fakeresponse.presentation.viewmodels.data.toGqlRecord


class AddToDbUseCase(val repository: GqlRepository) : BaseUseCase<GqlRepository>(repository) {

    fun addToDb(data: AddGqlData): Long {
        validateData(data)
        return repository.addToDb(data.toGqlRecord())
    }

    fun updateRestRecord(id: Int, data: AddGqlData) {
        validateData(data)
        return repository.updateResponse(data.toGqlRecord(id))
    }

    fun getRecordFromTable(id: Int): GqlRecord {
        return repository.getGqlRecord(id)
    }

    private fun validateData(data: AddGqlData) {
        if (TextUtils.isEmpty(data.gqlQueryName)) throw EmptyException("gql name cannot be empty")
        if (TextUtils.isEmpty(data.response)) throw NoResponseException()
        validateJson(data)
    }

    private fun validateJson(data: AddGqlData) {
        val either = Extensions.isJSONValid(data.response!!)
        if (either is Either.Left) {
            throw either.a
        }
    }
}