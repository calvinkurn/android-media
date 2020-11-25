package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.base.BaseUseCase
import com.tokopedia.mediauploader.MediaRepository
import com.tokopedia.mediauploader.data.consts.GraphQueryBuilder
import com.tokopedia.mediauploader.data.entity.DataUploaderPolicy
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

open class DataPolicyUseCase @Inject constructor(
        private val repository: MediaRepository
) : BaseUseCase<RequestParams, DataUploaderPolicy>() {

    override suspend fun execute(params: RequestParams): DataUploaderPolicy {
        if (params.paramsAllValueInString.isEmpty()) throw Exception("Not param found")

        return execute(
                query = QUERY,
                repository = repository,
                requestParams = params
        )
    }

    companion object {
        private val QUERY = GraphQueryBuilder.mediaPolicy
        private const val PARAM_SOURCE_ID = "source"

        fun createParams(sourceId: String): RequestParams {
            return RequestParams.create().apply {
                putString(PARAM_SOURCE_ID, sourceId)
            }
        }
    }

}