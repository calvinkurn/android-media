package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.product.create.data.CreateCouponProductParams
import com.tokopedia.vouchercreation.product.create.data.CreateCouponProductResponse
import javax.inject.Inject

class CreateCouponProductUseCase  @Inject constructor(private val gqlRepository: GraphqlRepository): BaseGqlUseCase<Int>() {


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
        fun createRequestParam(param: CreateCouponProductParams) : RequestParams {
            return RequestParams.create().apply {
                putObject(CREATE_PARAM_KEY, param)
            }
        }
    }

    override suspend fun executeOnBackground(): Int {
        val request = GraphqlRequest(MUTATION, CreateCouponProductResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(CreateCouponProductResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getData<CreateCouponProductResponse>()

            if (data.data.merchantPromotionCreateMV.data.status != STATUS_SUCCESS) {
                throw MessageErrorException(data.data.merchantPromotionCreateMV.message)
            } else {
                return data.data.merchantPromotionCreateMV.data.voucherId
            }
        } else {
            throw MessageErrorException(error.joinToString(", ") {
                it.message
            })
        }
    }

}