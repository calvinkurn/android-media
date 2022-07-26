package com.tokopedia.shopdiscount.manage_discount.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceProductSubmissionRequest
import com.tokopedia.shopdiscount.manage_discount.data.request.GetSlashPriceSetupProductListRequest
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductSubmissionResponse
import com.tokopedia.shopdiscount.manage_discount.data.response.GetSlashPriceSetupProductListResponse
import javax.inject.Inject

class MutationSlashPriceProductSubmissionUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<DoSlashPriceProductSubmissionResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(DoSlashPriceProductSubmissionQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(DoSlashPriceProductSubmissionResponse::class.java)
    }

    fun setParams(request: DoSlashPriceProductSubmissionRequest) {
        setRequestParams(
            mapOf<String, Any>(
                KEY_PARAMS to request
            )
        )
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "DoSlashPriceProductSubmissionQuery"
        private const val QUERY = """
            mutation DoSlashPriceProductSubmission(${'$'}params: DoSlashPriceProductSubmissionRequest!) {
              DoSlashPriceProductSubmission(params: ${'$'}params){
                response_header{
                  status
                  error_code
                  error_message
                  success
                  process_time
                  reason
                }
                data{
                  product_id
                  name
                  url
                  sku
                  picture
                  success
                  message
                  error_code
                  warehouses{
                    key
                    value{
                      warehouse_id
                      success
                      message
                      error_code
                    }
                  }
                }
              }
            } 
        """
    }
}