package com.tokopedia.shopdiscount.manage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shopdiscount.manage.data.request.GetSlashPriceProductListMetaRequest
import com.tokopedia.shopdiscount.manage.data.response.GetSlashPriceProductListMetaResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetSlashPriceProductListMetaUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : UseCase<GetSlashPriceProductListMetaResponse>(), GqlQueryInterface {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
    }

    override fun getOperationNameList(): List<String> {
        return emptyList()
    }

    override fun getQuery(): String {
        return """
            query GetSlashPriceProductListMeta(${'$'}params: GetSlashPriceProductListMetaRequest)  {
              GetSlashPriceProductListMeta(params: ${'$'}params){
                response_header {
                  status
                  error_message
                  success
                  process_time
                  reason
                  error_code
                }
                data {
                  tab{
                    id
                    name
                    value
                  }
                }
              }
            }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return EMPTY_STRING
    }

    private var params = emptyMap<String, Any>()

    suspend fun execute(
        source: String = CAMPAIGN,
        ip: String = EMPTY_STRING,
        useCase: String = EMPTY_STRING
    ): GetSlashPriceProductListMetaResponse {
        val request = GetSlashPriceProductListMetaRequest(source, ip, useCase)
        params = mapOf(REQUEST_PARAM_KEY to request)
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): GetSlashPriceProductListMetaResponse {
        val request =
            GraphqlRequest(this, GetSlashPriceProductListMetaResponse::class.java, params, true)
        return repository.response(listOf(request)).getSuccessData()
    }


}