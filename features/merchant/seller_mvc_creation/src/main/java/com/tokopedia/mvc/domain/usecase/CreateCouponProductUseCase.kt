package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.mvc.data.mapper.GetCouponImagePreviewFacadeMapper
import com.tokopedia.mvc.data.response.CreateCouponResponse
import com.tokopedia.mvc.domain.entity.CreateCouponProductParams
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class CreateCouponProductUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: GetCouponImagePreviewFacadeMapper
): GraphqlUseCase<Int>() {

    companion object {
        private const val MUTATION = "mutation CreateVoucher(\$merchantVoucherData: mvCreateData!){\n" +
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

        private const val STATUS_SUCCESS = "Success"
        private const val CREATE_PARAM_KEY = "merchantVoucherData"
        private const val OPERATION_NAME = "CreateVoucher"
    }

    private val mutation = object : GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    private fun createRequestParam(useCaseParam: CreateCouponUseCaseParam): Map<String, CreateCouponProductParams> {
        with (useCaseParam) {
            return mapOf(CREATE_PARAM_KEY to mapper.mapToCreateCouponProductParam(this))
        }
    }

    suspend fun executeCreation(useCaseParam: CreateCouponUseCaseParam): Int {
        val params = createRequestParam(useCaseParam)
        val request = GraphqlRequest(mutation, CreateCouponResponse::class.java, params)
        val response = gqlRepository.response(listOf(request))
        val error = response.getError(CreateCouponResponse::class.java)

        if (error.isNullOrEmpty()) {
            val data = response.getSuccessData<CreateCouponResponse>()
            if (data.merchantPromotionCreateMV.data.status != STATUS_SUCCESS) {
                throw MessageErrorException(data.merchantPromotionCreateMV.message)
            } else {
                return data.merchantPromotionCreateMV.data.voucherId.toIntOrZero()
            }
        } else {
            throw MessageErrorException(error.joinToString(", ") {
                it.message
            })
        }
    }

    data class CreateCouponUseCaseParam(
        val voucherConfiguration: VoucherConfiguration,
        val couponProducts: List<SelectedProduct>,
        val token: String,
        val imageUrl: String,
        val imageSquare: String,
        val imagePortrait: String,
        val warehouseId: String
    )
}
