package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.GetCouponImagePreviewFacadeMapper
import com.tokopedia.mvc.data.response.UpdateVoucherResponse
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.UpdateCouponRequestParams
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class UpdateCouponUseCase @Inject constructor(
    private val gqlRepository: GraphqlRepository,
    private val mapper: GetCouponImagePreviewFacadeMapper
): GraphqlUseCase<Boolean>() {

    companion object {
        private const val MUTATION = "mutation ChangeVoucherPromo(\$update_param: mvUpdateData!) {\n" +
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

        private const val UPDATE_PARAM_KEY = "update_param"
        private const val OPERATION_NAME = "ChangeVoucherPromo"
    }

    private val mutation = object : GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = MUTATION
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun executeUpdate(useCaseParam: UpdateCouponUseCaseParam): Boolean {
        val params = createRequestParam(useCaseParam)
        val request = GraphqlRequest(mutation, UpdateVoucherResponse::class.java, params)
        val response = gqlRepository.response(listOf(request))

        val error = response.getError(UpdateVoucherResponse::class.java)
        if (error.isNullOrEmpty()) {
            val data = response.getSuccessData<UpdateVoucherResponse>().updateVoucher
            with(data.updateVoucherSuccessData) {
                if (getIsSuccess()) {
                    return true
                } else {
                    throw MessageErrorException(data.message)
                }
            }
        } else {
            throw MessageErrorException(error.joinToString{ it.message })
        }
    }

    fun createRequestParam(useCaseParam: UpdateCouponUseCaseParam): Map<String, UpdateCouponRequestParams> {
        with(useCaseParam) {
            return mapOf(UPDATE_PARAM_KEY to mapper.mapToUpdateCouponProductParam(this))
        }
    }

    data class UpdateCouponUseCaseParam(
        val couponId : Long,
        val voucherConfiguration: VoucherConfiguration,
        val couponProducts: List<SelectedProduct>,
        val token: String,
        val imageUrl : String,
        val imageSquare: String,
        val imagePortrait: String,
        val warehouseId: String
    )
}
