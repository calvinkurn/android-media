package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.consts.GraphQueryBuilder
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import kotlinx.coroutines.CoroutineDispatcher

open class DataPolicyUseCase constructor(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Map<String, String>, DataUploaderPolicy>(dispatcher) {

    override suspend fun execute(params: Map<String, String>): DataUploaderPolicy {
        return request(repository, params)
    }

    override fun graphqlQuery(): String {
        return GraphQueryBuilder.mediaPolicy
    }

    fun createParams(sourceId: String): Map<String, String> {
        return mapOf(
            PARAM_SOURCE_ID to sourceId
        )
    }

    companion object {
        private const val PARAM_SOURCE_ID = "source"
    }

}