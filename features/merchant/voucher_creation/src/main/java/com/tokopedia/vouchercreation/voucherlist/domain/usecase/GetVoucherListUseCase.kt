package com.tokopedia.vouchercreation.voucherlist.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.common.model.VoucherMapper
import com.tokopedia.vouchercreation.voucherlist.domain.model.VoucherListParam
import com.tokopedia.vouchercreation.voucherlist.model.remote.GetMerchantVoucherListResponse
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 11/05/20
 */

class GetVoucherListUseCase @Inject constructor(
        private val graphqlRepository: GraphqlRepository,
        private val mapper: VoucherMapper
) : BaseGqlUseCase<List<VoucherUiModel>>() {

    companion object {
        private const val QUERY = "query getAllMerchantPromotion(\$filter : MVFilter!) {\n" +
                "              MerchantPromotionGetMVList(Filter: \$filter) {\n" +
                "                data {\n" +
                "                  paging {\n" +
                "                    per_page\n" +
                "                    page\n" +
                "                    has_prev\n" +
                "                    has_next\n" +
                "                  }\n" +
                "                  vouchers {\n" +
                "                    voucher_id\n" +
                "                    voucher_name\n" +
                "                    voucher_type\n" +
                "                    voucher_type_formatted\n" +
                "                    voucher_image\n" +
                "                    voucher_image_square\n" +
                "                    voucher_status\n" +
                "                    voucher_discount_type_formatted\n" +
                "                    voucher_discount_amt\n" +
                "                    voucher_discount_amt_formatted\n" +
                "                    voucher_discount_amt_max\n" +
                "                    voucher_minimum_amt\n" +
                "                    voucher_quota\n" +
                "                    confirmed_global_quota\n" +
                "                    booked_global_quota\n" +
                "                    voucher_start_time\n" +
                "                    voucher_finish_time\n" +
                "                    voucher_code\n" +
                "                    create_time\n" +
                "                    update_time\n" +
                "                    is_public\n" +
                "                  }\n" +
                "                }\n" +
                "              }\n" +
                "            }"

        private const val FILTER_KEY = "filter"

        @JvmStatic
        fun createRequestParam(voucherListParam: VoucherListParam) : RequestParams {
            return RequestParams.create().apply {
                putObject(FILTER_KEY, voucherListParam)
            }
        }

    }

    var isActive = false

    override suspend fun executeOnBackground(): List<VoucherUiModel> {
        val gqlRequest = GraphqlRequest(QUERY, GetMerchantVoucherListResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetMerchantVoucherListResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetMerchantVoucherListResponse>()
            return mapper.mapRemoteModelToUiModel(data.result.data.vouchers)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

}