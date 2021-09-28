package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.data.consts.GraphQueryBuilder
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

open class DataPolicyUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<String, DataUploaderPolicy>(Dispatchers.IO) {

    override fun graphqlQuery(): String {
        return GraphQueryBuilder.mediaPolicy
    }

    override suspend fun execute(params: String): DataUploaderPolicy {
        val param = setSourceId(params)

        return repository.request(graphqlQuery(), param)
    }

    private fun setSourceId(sourceId: String) = mapOf(
        PARAM_SOURCE_ID to sourceId
    )

    companion object {
        private const val PARAM_SOURCE_ID = "source"
    }

}