package com.tokopedia.sellerhomecommon.domain.usecase

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.sellerhomecommon.domain.mapper.PostMapper
import com.tokopedia.sellerhomecommon.domain.model.DataKeyModel
import com.tokopedia.sellerhomecommon.domain.model.GetPostDataResponse
import com.tokopedia.sellerhomecommon.domain.model.DynamicParameterModel
import com.tokopedia.sellerhomecommon.presentation.model.PostListDataUiModel
import com.tokopedia.usecase.RequestParams

/**
 * Created By @ilhamsuaib on 21/05/20
 */

class GetPostDataUseCase(
        private val gqlRepository: GraphqlRepository,
        private val postMapper: PostMapper
) : BaseGqlUseCase<List<PostListDataUiModel>>() {

    override suspend fun executeOnBackground(): List<PostListDataUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetPostDataResponse::class.java, params.parameters)
        val gqlResponse: GraphqlResponse = gqlRepository.getReseponse(listOf(gqlRequest))

        val errors: List<GraphqlError>? = gqlResponse.getError(GetPostDataResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetPostDataResponse>()
            val widgetDataList = data.getPostWidgetData?.data.orEmpty()
            return postMapper.mapRemoteDataModelToUiDataModel(widgetDataList)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

    companion object {
        private const val DATA_KEYS = "dataKeys"
        private const val DEFAULT_POST_LIMIT = 3

        fun getRequestParams(
                dataKey: List<String>,
                dynamicParameter: DynamicParameterModel,
                limit: Int = DEFAULT_POST_LIMIT
        ): RequestParams = RequestParams.create().apply {
            val dataKeys = dataKey.map {
                DataKeyModel(
                        key = it,
                        jsonParams = dynamicParameter.copy(limit = limit).toJsonString()
                )
            }
            putObject(DATA_KEYS, dataKeys)
        }

        private val QUERY = """
            query (${'$'}dataKeys: [dataKey!]!) {
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
                  error
                  errorMsg
                }
              }
            }
        """.trimIndent()
    }
}