package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.consts.GraphQueryBuilder
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class DataPolicyUseCase @Inject constructor(
    private val repository: GraphqlRepository,
) : CoroutineUseCase<Map<String, String>, DataUploaderPolicy>(Dispatchers.IO) {

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