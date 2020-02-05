package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

open class DataPolicyUseCase @Inject constructor(
        private val query: String,
        private val repository: GraphqlRepository
) : UseCase<DataUploaderPolicy>() {

    var requestParams = mapOf<String, Any>()

    override suspend fun executeOnBackground(): DataUploaderPolicy {
        if (requestParams.isEmpty()) throw Exception("Not param found")
        val request = GraphqlRequest(query, DataUploaderPolicy::class.java, requestParams)
        val response = repository.getReseponse(listOf(request))
        val error = response.getError(DataUploaderPolicy::class.java)
        if (error == null || error.isEmpty()) {
            return response.getData(DataUploaderPolicy::class.java)
        } else {
            throw Exception(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SOURCE_ID = "source"

        fun createParams(sourceId: String): Map<String, Any> {
            val requestParams = hashMapOf<String, Any>()
            requestParams[PARAM_SOURCE_ID] = sourceId
            return requestParams
        }
    }

}