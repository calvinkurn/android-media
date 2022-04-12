package com.tokopedia.shopdiscount.manage_discount.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.manage_discount.data.request.DoSlashPriceReservationRequest
import com.tokopedia.shopdiscount.manage_discount.data.response.DoSlashPriceProductReservationResponse
import javax.inject.Inject

class MutationDoSlashPriceProductReservationUseCase @Inject constructor(
    gqlRepository: GraphqlRepository
) : GraphqlUseCase<DoSlashPriceProductReservationResponse>(gqlRepository) {

    init {
        setupUseCase()
    }

    @GqlQuery(QUERY_NAME, QUERY)
    private fun setupUseCase() {
        setGraphqlQuery(DoSlashPriceProductReservationQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(DoSlashPriceProductReservationResponse::class.java)
    }

    fun setParams(request: DoSlashPriceReservationRequest) {
        setRequestParams(
            mapOf<String, Any>(
                KEY_PARAMS to request
            )
        )
    }

    companion object {
        private const val KEY_PARAMS = "params"
        private const val QUERY_NAME = "DoSlashPriceProductReservationQuery"
        private const val QUERY = """
            mutation DoSlashPriceProductReservation(${'$'}params: DoSlashPriceProductReservationRequest!) {
              DoSlashPriceProductReservation(params: ${'$'}params){
                response_header{
                  status
                  error_code
                  error_message
                  success
                  process_time
                  reason
                }
                failed_products{
                  product_id
                  name
                  url
                  message
                  error_code
                }
              }
            } 
        """
    }
}