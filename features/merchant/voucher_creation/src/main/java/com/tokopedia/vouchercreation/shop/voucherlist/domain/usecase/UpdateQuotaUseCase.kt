package com.tokopedia.vouchercreation.shop.voucherlist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.shop.voucherlist.domain.model.UpdateQuotaResponse
import javax.inject.Inject

class UpdateQuotaUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                             private val getTokenUseCase: GetTokenUseCase): BaseGqlUseCase<Boolean>() {

    companion object {
        const val QUERY = "mutation UpdateVoucherQuota(\$voucher_id: Int!, \$quota: Int!, \$token: String!, \$source: String!){\n" +
                "  merchantPromotionUpdateMVQuota(merchantVoucherUpdateQuotaData:{\n" +
                "    voucher_id: \$voucher_id,\n" +
                "    quota: \$quota,\n" +
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
        private const val QUOTA_KEY = "quota"
        private const val TOKEN = "token"

        @JvmStatic
        fun createRequestParam(voucherId: Int,
                               quota: Int) =
            VoucherSource.getVoucherRequestParams().apply {
                putInt(VOUCHER_ID_KEY, voucherId)
                putInt(QUOTA_KEY, quota)
            }
    }

    override suspend fun executeOnBackground(): Boolean {
        val token = getTokenUseCase.executeOnBackground()
        params.putString(TOKEN, token)

        val request = GraphqlRequest(QUERY, UpdateQuotaResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val errors = response.getError(UpdateQuotaResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<UpdateQuotaResponse>().updateVoucher
            with(data.updateVoucherSuccessData) {
                if (getIsSuccess()) {
                    return true
                } else {
                    throw MessageErrorException(data.message)
                }
            }
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }
}