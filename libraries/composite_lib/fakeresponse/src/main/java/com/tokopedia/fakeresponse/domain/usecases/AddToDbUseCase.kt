package com.tokopedia.fakeresponse.domain.usecases

import android.text.TextUtils
import com.tokopedia.fakeresponse.Extensions
import com.tokopedia.fakeresponse.data.models.Either
import com.tokopedia.fakeresponse.db.entities.GqlRecord
import com.tokopedia.fakeresponse.domain.exceptions.EmptyException
import com.tokopedia.fakeresponse.domain.exceptions.NoResponseException
import com.tokopedia.fakeresponse.domain.repository.GqlRepository
import com.tokopedia.fakeresponse.presentation.viewmodels.data.AddGqlData
import com.tokopedia.fakeresponse.presentation.viewmodels.data.toGqlRecord


class AddToDbUseCase(val repository: GqlRepository) : BaseUseCase<GqlRepository>(repository) {

    fun addToDb(data: AddGqlData): Long {
        validateData(data)
        return repository.addToDb(data.toGqlRecord())
    }

    fun updateRecord(id: Int, data: AddGqlData) {
        validateData(data)
        val record = getRecordFromTable(id)
        return repository.updateResponse(data.toGqlRecord(id, record.createdAt))
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

    fun deleteRecord(id:Int){
        repository.delete(id)
    }
}