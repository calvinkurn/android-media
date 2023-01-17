package com.tokopedia.mediauploader.image.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.GqlQueryBuilder
import com.tokopedia.mediauploader.common.data.entity.DataUploaderPolicy
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetImageSecurePolicyUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<String, SourcePolicy>(Dispatchers.IO) {

    override fun graphqlQuery(): String {
        return GqlQueryBuilder.imagePolicySecure
    }

    override suspend fun execute(params: String): SourcePolicy {
        val param = GqlQueryBuilder.setSourceId(params)
        val result: DataUploaderPolicy = repository.request(graphqlQuery(), param)
        return result.sourcePolicy()
    }

}
