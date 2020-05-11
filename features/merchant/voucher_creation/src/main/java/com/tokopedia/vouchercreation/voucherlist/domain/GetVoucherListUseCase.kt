package com.tokopedia.vouchercreation.voucherlist.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.vouchercreation.common.base.BaseGqlUseCase
import com.tokopedia.vouchercreation.voucherlist.domain.mapper.VoucherMapper
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

    override suspend fun executeOnBackground(): List<VoucherUiModel> {
        /*val gqlRequest = GraphqlRequest(QUERY, GetMerchantVoucherListResponse::class.java, params.parameters)
        val gqlResponse = graphqlRepository.getReseponse(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetMerchantVoucherListResponse::class.java)
        if (errors.isEmpty()) {
            val data = gqlResponse.getData<GetMerchantVoucherListResponse>()
            return mapper.mapRemoteModelToUiModel(data.result.data)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }*/
        return emptyList()
    }

    companion object {
        private const val QUERY = """
            query (${'$'}Filter: MVFilter!) {
              MerchantPromotionGetMVList(Filter: ${'$'}Filter) {
                data {
                  paging {
                    per_page
                    page
                    has_prev
                    has_next
                  }
                  vouchers {
                    voucher_id
                    shop_id
                    voucher_name
                    voucher_type
                    voucher_type_formatted
                    voucher_image
                    voucher_image_square
                    voucher_status
                    voucher_status_formatted
                    voucher_discount_type
                    voucher_discount_type_formatted
                    voucher_discount_amt
                    voucher_discount_amt_formatted
                    voucher_discount_amt_max
                    voucher_discount_amt_max_formatted
                    voucher_minimum_amt
                    voucher_minimum_amt_formatted
                    voucher_quota
                    remaning_quota
                    booked_global_quota
                    voucher_start_time
                    voucher_finish_time
                    voucher_code
                    galadriel_voucher_id
                    galadriel_catalog_id
                    create_time
                    create_by
                    update_time
                    update_by
                    is_public
                    is_quota_avaiable
                    tnc
                  }
                }
              }
            }
        """
    }
}