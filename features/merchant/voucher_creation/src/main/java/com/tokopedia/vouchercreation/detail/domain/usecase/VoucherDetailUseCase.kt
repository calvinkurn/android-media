package com.tokopedia.vouchercreation.detail.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.base.VoucherSource
import com.tokopedia.vouchercreation.common.model.VoucherMapper
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

class VoucherDetailUseCase @Inject constructor(private val gqlRepository: GraphqlRepository,
                                               private val voucherMapper: VoucherMapper): BaseGqlUseCase<VoucherUiModel>() {

    companion object {
        const val QUERY = "query GetVoucherDataById (\$voucher_id: Int!, \$source: String!){\n" +
                "  merchantPromotionGetMVDataByID(voucher_id: \$voucher_id, source: \$source){\n" +
                "    header{\n" +
                "      process_time\n" +
                "      message\n" +
                "      reason\n" +
                "      error_code\n" +
                "    }\n" +
                "    data{\n" +
                "      voucher_id\n" +
                "      voucher_name\n" +
                "      voucher_type\n" +
                "      voucher_image\n" +
                "      voucher_image_square\n" +
                "      voucher_status\n" +
                "      voucher_discount_type\n" +
                "      voucher_discount_amt\n" +
                "      voucher_discount_amt_max\n" +
                "      voucher_minimum_amt\n" +
                "      voucher_quota\n" +
                "      voucher_start_time\n" +
                "      voucher_finish_time\n" +
                "      voucher_code\n" +
                "      create_time\n" +
                "      update_time\n" +
                "      is_public\n" +
                "      voucher_type_formatted\n" +
                "      voucher_discount_type_formatted\n" +
                "      voucher_discount_amt_formatted\n" +
                "      confirmed_global_quota\n" +
                "      booked_global_quota\n" +
                "    }\n" +
                "  }\n" +
                "}"

        private const val VOUCHER_ID_KEY = "voucher_id"

        @JvmStatic
        fun createRequestParam(voucherId: Int) =
                VoucherSource.getVoucherRequestParams().apply {
                    putInt(VOUCHER_ID_KEY, voucherId)
                }
    }

    override suspend fun executeOnBackground(): VoucherUiModel {
        val request = GraphqlRequest(QUERY, VoucherDetailResponse::class.java, params.parameters)
        val response = gqlRepository.getReseponse(listOf(request))

        val errors = response.getError(VoucherDetailResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getData<VoucherDetailResponse>()
            return voucherMapper.mapRemoteModelToUiModel(data.result.merchantVoucherModel)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }
}