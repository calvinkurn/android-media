package com.tokopedia.vouchercreation.common.domain.usecase

import androidx.annotation.StringDef
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.domain.model.CancelVoucherResponse
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase.GetTokenUseCase
import javax.inject.Inject

class CancelVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                               private val getTokenUseCase: GetTokenUseCase): BaseGqlUseCase<Int>() {

    companion object {

        const val MUTATION = "mutation CancelVoucher (\$voucher_id: Int!, \$token: String!, \$source: String!, \$status: String!){\n" +
                "  merchantPromotionUpdateStatusMV(merchantVoucherUpdateStatusData:{\n" +
                "    voucher_id: \$voucher_id,\n" +
                "    voucher_status:\$status,\n" +
                "    token: \$token,\n" +
                "    source: \$source\n" +
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
                "}"

        private const val VOUCHER_ID_KEY = "voucher_id"
        private const val TOKEN_KEY = "token"
        private const val STATUS_KEY = "status"

        @JvmStatic
        fun createRequestParam(voucherId: Int,
                               @CancelStatus cancelStatus: String) =
            VoucherSource.getVoucherRequestParams().apply {
                putInt(VOUCHER_ID_KEY, voucherId)
                putString(STATUS_KEY, cancelStatus)
            }

    }

    @MustBeDocumented
    @Retention(AnnotationRetention.SOURCE)
    @StringDef(CancelStatus.STOP, CancelStatus.DELETE)
    annotation class CancelStatus {
        companion object {
            const val STOP = "stop"
            const val DELETE = "delete"
        }
    }

    override suspend fun executeOnBackground(): Int {
        val token = getTokenUseCase.executeOnBackground()
        params.putString(TOKEN_KEY, token)

        val voucherId = params.getInt(VOUCHER_ID_KEY, 0)

        val request = GraphqlRequest(MUTATION, CancelVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val errors = response.getError(CancelVoucherResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<CancelVoucherResponse>()
            val cancelVoucherData = data.cancelVoucher
            with(cancelVoucherData) {
                if (updateVoucherSuccessData.getIsSuccess()) {
                    return updateVoucherSuccessData.voucherId
                } else {
                    throw VoucherCancellationException(voucherId, message)
                }
            }
        } else {
            throw VoucherCancellationException(voucherId, errors.joinToString { it.message })
        }

    }
}