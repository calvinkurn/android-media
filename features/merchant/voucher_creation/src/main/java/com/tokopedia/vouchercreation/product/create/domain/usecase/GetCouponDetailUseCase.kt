package com.tokopedia.vouchercreation.product.create.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.model.VoucherMapper
import com.tokopedia.vouchercreation.shop.detail.domain.usecase.VoucherDetailResponse
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

class GetCouponDetailUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                                 private val voucherMapper: VoucherMapper
): BaseGqlUseCase<VoucherUiModel>() {

    companion object {
        private const val QUERY = """
            query GetVoucherDataById (${'$'}voucher_id: Int!, ${'$'}source: String!) {
                merchantPromotionGetMVDataByID(voucher_id: ${'$'}voucher_id, source: ${'$'}source) {                
                    header {
                      process_time
                      message
                      reason
                      error_code
                    }
                    data {
                      voucher_id
                      voucher_name
                      voucher_type
                      voucher_image
                      voucher_image_square
                      voucher_status
                      voucher_discount_type
                      voucher_discount_amt
                      voucher_discount_amt_max
                      voucher_minimum_amt
                      voucher_quota
                      voucher_start_time
                      voucher_finish_time
                      voucher_code
                      create_time
                      update_time
                      is_public
                      voucher_type_formatted
                      voucher_discount_type_formatted
                      voucher_discount_amt_formatted
                      confirmed_global_quota
                      booked_global_quota
                      is_vps
                      package_name
                      is_subsidy
                      tnc
                    }                    
                }
            }
        """

        private const val VOUCHER_ID_KEY = "voucher_id"

        @JvmStatic
        fun createRequestParam(voucherId: Int) =
                VoucherSource.getVoucherRequestParams().apply {
                    putInt(VOUCHER_ID_KEY, voucherId)
                }
    }

    override suspend fun executeOnBackground(): VoucherUiModel {
        val request = GraphqlRequest(QUERY, VoucherDetailResponse::class.java, params.parameters)
        val response = gqlRepository.response(listOf(request))

        val errors = response.getError(VoucherDetailResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<VoucherDetailResponse>()
            return voucherMapper.mapRemoteModelToUiModel(data.result.merchantVoucherModel)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }
}