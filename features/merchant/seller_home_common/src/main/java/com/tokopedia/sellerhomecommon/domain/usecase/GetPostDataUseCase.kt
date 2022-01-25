package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sellerhomecommon.domain.gqlquery.GqlGetPostData
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.domain.model.TableAndPostDataKey
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetPostDataUseCase(
    gqlRepository: GraphqlRepository,
    private val postMapper: PostMapper,
    dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetPostDataResponse, List<PostListDataUiModel>>(
    gqlRepository, postMapper, dispatchers, GqlGetPostData.QUERY, false
) {

    companion object {
        private const val DATA_KEYS = "dataKeys"

        fun getRequestParams(
            dataKey: List<TableAndPostDataKey>,
            dynamicParameter: DynamicParameterModel
        ): RequestParams = RequestParams.create().apply {

            val dataKeys = dataKey.map {
                DataKeyModel(
                    key = it.dataKey,
                    jsonParams = dynamicParameter.copy(
                        limit = it.maxData,
                        postFilter = it.filter
                    ).toJsonString(),
                    maxDisplay = it.maxDisplayPerPage
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }
    }

    override val classType: Class<GetPostDataResponse>
        get() = GetPostDataResponse::class.java

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<PostListDataUiModel> {
        val dataKeys: List<DataKeyModel> = (params.getObject(DATA_KEYS) as? List<DataKeyModel>)
            .orEmpty()
        val gqlRequest = GraphqlRequest(GqlGetPostData, classType, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.response(
            listOf(gqlRequest), cacheStrategy
        )

        val errors: List<GraphqlError>? = gqlResponse.getError(classType)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPostDataResponse>()
            val isFromCache = cacheStrategy.type == CacheType.CACHE_ONLY
            postMapper.setDataKeys(dataKeys)
            return postMapper.mapRemoteDataToUiData(data, isFromCache)
        } else {
            throw MessageErrorException(errors.firstOrNull()?.message.orEmpty())
        }
    }
}