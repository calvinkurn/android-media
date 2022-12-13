package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.mvc.data.request.MvUpdateStatusRequest
import com.tokopedia.mvc.data.response.MerchantPromotionUpdateStatusMVResponse
import javax.inject.Inject

class MerchantPromotionUpdateStatusMVUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<Long>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_KEY = "merchantVoucherUpdateStatusData"
        private const val SOURCE = "fe-mobile"
        const val STOP = "stop"
        const val DELETE = "delete"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "merchantPromotionUpdateStatusMV"
        private val MUTATION = """
            mutation $OPERATION_NAME(${'$'}merchantVoucherUpdateStatusData: mvUpdateStatusRequest!){
                $OPERATION_NAME($REQUEST_PARAM_KEY: ${'$'}merchantVoucherUpdateStatusData){
                      status
                      message
                      process_time
                      data{
                        redirect_url
                        voucher_id
                        status
                       }
                     }
                   }
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): Long {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))

        val data = response.getSuccessData<MerchantPromotionUpdateStatusMVResponse>()
        val mvPromotionStatusUpdate = data.merchantPromotionUpdateStatusMV
        return mvPromotionStatusUpdate.data.voucherId
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val status = when (param.bottomSheetType) {
            Int.ONE -> DELETE
            else -> STOP
        }

        val payload = MvUpdateStatusRequest(
            param.voucherId,
            status,
            param.token,
            param.source
        )
        val params = mapOf(REQUEST_PARAM_KEY to payload)

        return GraphqlRequest(
            mutation,
            MerchantPromotionUpdateStatusMVResponse::class.java,
            params
        )
    }

    data class Param(
        val voucherId: Long,
        val token: String,
        val source: String = SOURCE,
        val bottomSheetType: Int
    )
}
