package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
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
        postMapper: PostMapper
) : CloudAndCacheGraphqlUseCase<GetPostDataResponse, List<PostListDataUiModel>>(gqlRepository, postMapper, true, GetPostDataResponse::class.java, QUERY, false) {

    var firstLoad: Boolean = true

    override suspend fun executeOnBackground(requestParams: RequestParams, includeCache: Boolean) {
        return super.executeOnBackground(requestParams, includeCache).also { firstLoad = false }
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
                }
              }
            }
        """.trimIndent()
    }
}