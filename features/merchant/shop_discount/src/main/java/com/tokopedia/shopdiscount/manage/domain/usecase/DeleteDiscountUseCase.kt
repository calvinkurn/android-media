package com.tokopedia.shopdiscount.manage.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shopdiscount.common.data.request.RequestHeader
import com.tokopedia.shopdiscount.common.data.response.ResponseHeader
import com.tokopedia.shopdiscount.manage.data.request.DeleteDiscountRequest
import com.tokopedia.shopdiscount.manage.data.response.DeleteDiscountResponse
import com.tokopedia.shopdiscount.utils.constant.CAMPAIGN
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import javax.inject.Inject

class DeleteDiscountUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<DeleteDiscountResponse>(repository) {

    companion object {
        private const val REQUEST_PARAM_KEY = "params"
        private const val REQUEST_MUTATION_NAME = "doSlashPriceStop"
        private const val REQUEST_MUTATION = """
            mutation doSlashPriceStop(${'$'}params: DoSlashPriceStopRequest!)  {
              doSlashPriceStop(params: ${'$'}params){
                response_header {
                  status
                  error_message
                  success
                  process_time
                  reason
                  error_code
                }
              }
            }
        """
    }

    init {
        setupUseCase()
    }

    @GqlQuery(
        REQUEST_MUTATION_NAME,
        REQUEST_MUTATION
    )

    private fun setupUseCase() {
        setGraphqlQuery(DoSlashPriceStop())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(DeleteDiscountResponse::class.java)
    }

    fun setParams(
        source: String = CAMPAIGN,
        ip: String = EMPTY_STRING,
        useCase: String = EMPTY_STRING,
        discountStatusId : Int,
        productIds : List<String>,
        productData : List<DeleteDiscountRequest.ProductData> = emptyList()
    ) {
        val header = RequestHeader(source, ip, useCase)
        val request = DeleteDiscountRequest(header, discountStatusId, productIds, productData)
        val params = mapOf(REQUEST_PARAM_KEY to request)
        setRequestParams(params)
    }


}