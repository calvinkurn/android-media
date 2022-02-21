package com.tokopedia.vouchercreation.shop.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.shop.create.domain.model.CreateVoucherParam
import com.tokopedia.vouchercreation.shop.create.domain.model.CreateVoucherResponse
import javax.inject.Inject

class CreateVoucherUseCase @Inject constructor(private val gqlRepository: GraphqlRepository): BaseGqlUseCase<Int>() {

    companion object {

        const val MUTATION = "mutation CreateVoucher(\$merchantVoucherData: mvCreateData!){\n" +
                " merchantPromotionCreateMV(merchantVoucherData: \$merchantVoucherData){\n" +
                "  status\n" +
                "  message\n" +
                "  process_time\n" +
                "  data{\n" +
                "    redirect_url\n" +
                "    voucher_id\n" +
                "    status\n" +
                "  }\n" +
                "}\n" +
                "}"

        const val STATUS_SUCCESS = "Success"

        private const val CREATE_PARAM_KEY = "merchantVoucherData"

        @JvmStatic
        fun createRequestParam(param: CreateVoucherParam) : RequestParams {
            return RequestParams.create().apply {
                putObject(CREATE_PARAM_KEY, param)
            }
        }
    }

    override suspend fun executeOnBackground(): Int {
        val request = GraphqlRequest(MUTATION, CreateVoucherResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(CreateVoucherResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<CreateVoucherResponse>()
            if (data.merchantPromotionCreateMv.data.status != STATUS_SUCCESS) {
                throw MessageErrorException(data.merchantPromotionCreateMv.message)
            } else {
                return data.merchantPromotionCreateMv.data.voucherId
            }
        } else {
            throw MessageErrorException(error.joinToString(", ") {
                it.message
            })
        }
    }
}