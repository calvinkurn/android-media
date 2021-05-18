package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetPostDataUseCase(
        gqlRepository: GraphqlRepository,
        postMapper: PostMapper,
        dispatchers: CoroutineDispatchers
) : CloudAndCacheGraphqlUseCase<GetPostDataResponse, List<PostListDataUiModel>>(
        gqlRepository, postMapper, dispatchers, GetPostDataResponse::class.java, QUERY, false) {

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        super.executeOnBackground(requestParams, includeCache).also { isFirstLoad = false }
    }

    override suspend fun executeOnBackground(): List<PostListDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetPostDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest), cacheStrategy)

        val errors: List<GraphqlError>? = gqlResponse.getError(GetPostDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPostDataResponse>()
            return mapper.mapRemoteDataToUiData(data, cacheStrategy.type == CacheType.CACHE_ONLY)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"
        private const val DEFAULT_POST_LIMIT = 3

        fun getRequestParams(
                dataKey: List<Pair<String, String>>,
                dynamicParameter: DynamicParameterModel,
                limit: Int = DEFAULT_POST_LIMIT
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it.first,
                        jsonParams = dynamicParameter.copy(limit = limit, postFilter = it.second).toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }

        private val QUERY = """
            query getPostWidgetData(${'$'}dataKeys: [dataKey!]!) {
              fetchPostWidgetData(dataKeys: ${'$'}dataKeys) {
                data {
                  datakey
                  list {
                    title
                    url
                    applink
                    subtitle
                    featuredMediaURL
                  }
                  cta{
                    text
                    applink
                  }
                  error
                  errorMsg
                  showWidget
                }
              }
            }
        """.trimIndent()
    }
}