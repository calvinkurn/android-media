package com.rahullohra.fakeresponse.domain.usecases

import android.text.TextUtils
import com.rahullohra.fakeresponse.data.models.Either
import com.rahullohra.fakeresponse.db.entities.GqlRecord
import com.rahullohra.fakeresponse.domain.exceptions.EmptyException
import com.rahullohra.fakeresponse.domain.exceptions.NoResponseException
import com.rahullohra.fakeresponse.domain.repository.LocalRepository
import com.rahullohra.fakeresponse.presentaiton.viewmodels.data.AddGqlData
import java.util.*


class AddToDbUseCase(val repository: LocalRepository) : BaseUseCase<LocalRepository>(repository) {

    fun addToDb(data: AddGqlData): Long {
        validateData(data)
        return repository.addToDb(gqlDataToGql(data))
    }

    fun updateRestRecord(id:Int, data: AddGqlData) {
        validateData(data)
        return repository.updateResponse(gqlDataToGql(data, id))
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
        val either = isJSONValid(data.response!!)
        if (either is Either.Left) {
            throw either.a
        }
    }

    protected fun gqlDataToGql(data: AddGqlData, id:Int?=null): GqlRecord {
        val date = Date()

        val gql = GqlRecord(
                id = id,
                gqlOperationName = data.gqlQueryName!!,
                javaQueryName = data.javaQueryName,
                createdAt = date.time,
                updatedAt = date.time,
                enabled = true,
                customTag = data.customTag,
                response = data.response!!

        )
        return gql
    }
}