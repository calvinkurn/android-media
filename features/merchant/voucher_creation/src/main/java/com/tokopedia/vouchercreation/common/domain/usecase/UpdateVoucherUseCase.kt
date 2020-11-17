package com.tokopedia.vouchercreation.common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.domain.model.UpdateVoucherParam
import com.tokopedia.vouchercreation.voucherlist.domain.model.UpdateVoucherResponse
import javax.inject.Inject

class UpdateVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository) : BaseGqlUseCase<Boolean>()  {

    companion object {
        const val MUTATION = "mutation ChangeVoucherPromo(\$update_param: mvUpdateData!) {\n" +
                "  merchantPromotionUpdateMV(merchantVoucherUpdateData: \$update_param) {\n" +
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

        const val UPDATE_PARAM_KEY = "update_param"

        @JvmStatic
        fun createRequestParam(updateVoucherParam: UpdateVoucherParam) = RequestParams().apply {
            putObject(UPDATE_PARAM_KEY, updateVoucherParam)
        }
    }

    override suspend fun executeOnBackground(): Boolean {
        val request = GraphqlRequest(MUTATION, UpdateVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val error = response.getError(UpdateVoucherResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<UpdateVoucherResponse>().updateVoucher
            with(data.updateVoucherSuccessData) {
                if (getIsSuccess()) {
                    return true
                } else {
                    throw MessageErrorException(data.message)
                }
            }
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }
}