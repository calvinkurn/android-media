package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.exception.VoucherCancellationException
import com.tokopedia.mvc.data.response.CancelVoucherResponse
import com.tokopedia.mvc.data.response.UpdateStatusVoucherDataModel
import com.tokopedia.mvc.domain.entity.enums.UpdateVoucherAction
import com.tokopedia.mvc.util.constant.CommonConstant.SELLER_APP_PAGE_SOURCE
import javax.inject.Inject

class CancelVoucherUseCase @Inject constructor(
    private val repository: GraphqlRepository
) : GraphqlUseCase<Int>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val VOUCHER_ID_KEY = "voucher_id"
        private const val TOKEN_KEY = "token"
        private const val STATUS_KEY = "status"
        private const val SOURCE_KEY = "source"
    }

    private val mutation = object : GqlQueryInterface {
        private val OPERATION_NAME = "merchantPromotionUpdateStatusMV"
        private val MUTATION = "mutation $OPERATION_NAME(${'$'}voucher_id: Int!, ${'$'}token: String!, ${'$'}source: String!, ${'$'}status: String!) {\n" +
            "   $OPERATION_NAME(merchantVoucherUpdateStatusData:{" +
            "    voucher_id: ${'$'}voucher_id,\n" +
            "    voucher_status:${'$'}status,\n" +
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

    fun buildRequest(voucherId: Int, cancelStatus: UpdateVoucherAction, token: String): GraphqlRequest {
        val params = mapOf(
            VOUCHER_ID_KEY to voucherId,
            STATUS_KEY to cancelStatus.state,
            TOKEN_KEY to token,
            SOURCE_KEY to SELLER_APP_PAGE_SOURCE
        )
        return GraphqlRequest(
            mutation,
            CancelVoucherResponse::class.java,
            params
        )
    }

    suspend fun execute(voucherId: Int, cancelStatus: UpdateVoucherAction, token: String): UpdateStatusVoucherDataModel {
        val request = buildRequest(voucherId, cancelStatus, token)
        val response = repository.response(listOf(request))
        val dataSuccess = response.getSuccessData<CancelVoucherResponse>()
        val cancelVoucherData = dataSuccess.cancelVoucher
        with(cancelVoucherData) {
            if (updateStatusVoucherData.getIsSuccess()) {
                return cancelVoucherData
            } else {
                throw VoucherCancellationException(voucherId, message)
            }
        }
    }
}
