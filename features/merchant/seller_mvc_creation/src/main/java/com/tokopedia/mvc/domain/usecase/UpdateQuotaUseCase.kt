package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.response.UpdateQuotaResponse
import com.tokopedia.mvc.util.constant.CommonConstant.SELLER_APP_PAGE_SOURCE
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class UpdateQuotaUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<Boolean>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object{
        private const val VOUCHER_ID_KEY = "voucher_id"
        private const val QUOTA = "quota"
        private const val UPDATE_ALL_PERIOD = "update_next_period"
        private const val TOKEN_KEY = "token"
        private const val SOURCE_KEY = "source"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "merchantPromotionUpdateMVQuota"
        private val MUTATION = "mutation $OPERATION_NAME(${'$'}voucher_id: Int!, ${'$'}quota: Int!, ${'$'}update_next_period: Boolean!, ${'$'}token: String!, ${'$'}source: String!) {\n" +
            "   $OPERATION_NAME(merchantVoucherUpdateQuotaData:{" +
            "    voucher_id: ${'$'}voucher_id,\n" +
            "    quota:${'$'}quota,\n" +
            "    update_next_period:${'$'}update_next_period,\n" +
            "    token: ${'$'}token,\n" +
            "    source: ${'$'}source\n" +
            "  }){\n" +
            "    status\n" +
            "    message\n" +
            "    process_time\n" +
            "    data{\n" +
            "      redirect_url\n" +
            "      voucher_id\n" +
            "      status\n" +
            "    }\n" +
            "  }\n" +
            "}".trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    fun buildRequest(voucherId: Long, quota : Int, updateAllPeriod: Boolean, token : String): GraphqlRequest {
        val params = mapOf(
            VOUCHER_ID_KEY to voucherId,
            QUOTA to quota,
            UPDATE_ALL_PERIOD to updateAllPeriod,
            TOKEN_KEY to token,
            SOURCE_KEY to SELLER_APP_PAGE_SOURCE,
        )
        return GraphqlRequest(
            mutation,
            UpdateQuotaResponse::class.java,
            params
        )
    }

    suspend fun execute(voucherId: Long, quota: Int, updateAllPeriod: Boolean, token : String): Boolean {
        val request = buildRequest(voucherId, quota, updateAllPeriod, token)
        val response = repository.response(listOf(request))
        val dataSuccess = response.getSuccessData<UpdateQuotaResponse>()
        val updateQuotaResponse = dataSuccess.updateVoucher
        with(updateQuotaResponse) {
            if (updateVoucherSuccessData.getIsSuccess()) {
                return updateVoucherSuccessData.getIsSuccess()
            } else {
                throw MessageErrorException(updateQuotaResponse.message)
            }
        }
    }

}
