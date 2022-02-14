package com.tokopedia.mediauploader.video.domain

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.GqlQueryBuilder
import com.tokopedia.mediauploader.common.data.entity.DataUploaderPolicy
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetVideoPolicyUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : CoroutineUseCase<String, DataUploaderPolicy>(Dispatchers.IO) {

    override fun graphqlQuery(): String {
        return GqlQueryBuilder.videoPolicy
    }

    override suspend fun execute(params: String): DataUploaderPolicy {
        val param = GqlQueryBuilder.setSourceId(params)
        return repository.request(graphqlQuery(), param)
    }

}