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
        private const val QUERY = """
            query getAllMerchantPromotion(${'$'}filter : MVFilter!) {
                MerchantPromotionGetMVList(Filter: ${'$'}filter) {
                    data {
                      paging {
                        per_page
                        page
                        has_prev
                        has_next
                      }
                      vouchers {
                        voucher_id
                        voucher_name
                        voucher_type
                        voucher_type_formatted
                        voucher_image
                        voucher_image_square
                        voucher_status
                        voucher_discount_type_formatted
                        voucher_discount_amt
                        voucher_discount_amt_formatted
                        voucher_discount_amt_max
                        voucher_minimum_amt
                        voucher_quota
                        confirmed_global_quota
                        booked_global_quota
                        voucher_start_time
                        voucher_finish_time
                        voucher_code
                        create_time
                        update_time
                        is_public
                        is_vps
                        package_name
                        is_subsidy
                      }
                    }
                }
            }
        """

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
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest))

        val errors = gqlResponse.getError(GetMerchantVoucherListResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = gqlResponse.getData<GetMerchantVoucherListResponse>()
            return mapper.mapRemoteModelToUiModel(data.result.data.vouchers)
        } else {
            throw MessageErrorException(errors.joinToString(", ") { it.message })
        }
    }

}