package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mediauploader.BaseUseCase
import com.tokopedia.mediauploader.MediaRepository
import com.tokopedia.mediauploader.data.consts.GraphQueryBuilder
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import javax.inject.Inject

open class DataPolicyUseCase @Inject constructor(
        private val repository: MediaRepository
) : BaseUseCase<Map<String, Any>, DataUploaderPolicy>() {

    override suspend fun execute(params: Map<String, Any>): DataUploaderPolicy {
        if (params.isEmpty()) throw Exception("Not param found")
        val request = GraphqlRequest(QUERY, DataUploaderPolicy::class.java, params)
        val response = repository.response(listOf(request))
        val error = response.getError(DataUploaderPolicy::class.java)
        if (error == null || error.isEmpty()) {
            return response.getData(DataUploaderPolicy::class.java)
        } else {
            throw Exception(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        /**
         * GraphQueryBuilder: query builder,
         * the query of media policy
         */
        private val QUERY = GraphQueryBuilder.mediaPolicy

        /**
         * keys of params
         * @param source_id
         */
        private const val PARAM_SOURCE_ID = "source"

        fun createParams(sourceId: String): Map<String, Any> {
            val requestParams = hashMapOf<String, Any>()
            requestParams[PARAM_SOURCE_ID] = sourceId
            return requestParams
        }
    }

}