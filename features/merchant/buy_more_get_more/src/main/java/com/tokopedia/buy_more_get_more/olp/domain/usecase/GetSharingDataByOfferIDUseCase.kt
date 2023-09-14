package com.tokopedia.buy_more_get_more.olp.domain.usecase

import com.tokopedia.buy_more_get_more.olp.data.mapper.SharingDataByOfferIDMapper
import com.tokopedia.buy_more_get_more.olp.data.request.GetSharingDataByOfferIDParam
import com.tokopedia.buy_more_get_more.olp.data.response.SharingDataByOfferIDResponse
import com.tokopedia.buy_more_get_more.olp.domain.entity.SharingDataByOfferIdUiModel
import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import javax.inject.Inject

class GetSharingDataByOfferIDUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: SharingDataByOfferIDMapper
) : GraphqlUseCase<SharingDataByOfferIdUiModel>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_INPUT = "input"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "getSharingDataByOfferID"
        private val QUERY = """
              query $OPERATION_NAME(${'$'}input: GetSharingDataByOfferIDRequest!) {
                  GetSharingDataByOfferID(params: ${'$'}input) {
                     response_header{
                          errorCode
                          success
                          processTime
                        }
                     offer_data{
                          image_url
                          title
                          description
                          deep_link
                          tag
                          page_type
                          campaign_name
                        }
                      }
                    }


        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: GetSharingDataByOfferIDParam): SharingDataByOfferIdUiModel {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<SharingDataByOfferIDResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: GetSharingDataByOfferIDParam): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_INPUT to param
        )

        return GraphqlRequest(
            query,
            SharingDataByOfferIDResponse::class.java,
            params
        )
    }
}
