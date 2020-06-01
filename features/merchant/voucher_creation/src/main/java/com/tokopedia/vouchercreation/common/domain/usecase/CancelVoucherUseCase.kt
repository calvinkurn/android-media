package com.tokopedia.vouchercreation.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.domain.model.CancelVoucherResponse
import com.tokopedia.vouchercreation.common.exception.VoucherCancellationException
import com.tokopedia.vouchercreation.voucherlist.domain.usecase.GetTokenUseCase
import javax.inject.Inject

class CancelVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                               private val getTokenUseCase: GetTokenUseCase): BaseGqlUseCase<Int>() {

    companion object {

        const val MUTATION = "mutation CancelVoucher (\$voucher_id: Int!, \$token: String!, \$source: String!){\n" +
                "  merchantPromotionUpdateStatusMV(merchantVoucherUpdateStatusData:{\n" +
                "    voucher_id: \$voucher_id,\n" +
                "    voucher_status:\"delete\",\n" +
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

        @JvmStatic
        fun createRequestParam(voucherId: Int) =
            VoucherSource.getVoucherRequestParams().apply {
                putInt(VOUCHER_ID_KEY, voucherId)
            }

    }

    override suspend fun executeOnBackground(): Int {
        val token = getTokenUseCase.executeOnBackground()
        params.putString(TOKEN_KEY, token)

        val voucherId = params.getInt(VOUCHER_ID_KEY, 0)

        val request = GraphqlRequest(MUTATION, CancelVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val errors = response.getError(CancelVoucherResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<CancelVoucherResponse>()
            val cancelVoucherData = data.cancelVoucher
            with(cancelVoucherData) {
                if (updateVoucherSuccessData.isSuccess) {
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