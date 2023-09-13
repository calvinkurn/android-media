package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.GetVoucherListMapper
import com.tokopedia.mvc.data.response.GetMerchantVoucherListResponse
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.mvc.domain.entity.enums.VoucherStatus
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetVoucherListChildUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetVoucherListMapper
) : GraphqlUseCase<List<Voucher>>(repository) {

    companion object {
        private const val PARAM_KEY = "voucherId"
        private const val PARAM_FILTER_VOUCHER_STATUS = "voucherStatus"
        private const val PARAM_SORT_BY = "sortBy"
        private const val VALUE_SORT = "voucher_start_time"
        private const val OPERATION_NAME = "MerchantPromotionGetChildMVList"
        private const val QUERY = """
            query MerchantPromotionGetChildMVList(${'$'}voucherId: Int!, ${'$'}voucherStatus: String, ${'$'}sortBy: String) {
                MerchantPromotionGetChildMVList(parent_voucher_id: ${'$'}voucherId, Filter: {
                voucher_status: ${'$'}voucherStatus
                sort_by: ${'$'}sortBy
                }) {
                    data {
                      vouchers {
                        voucher_id
                        voucher_name
                        voucher_type
                        voucher_type_formatted
                        voucher_image
                        voucher_image_square
                        voucher_image_portrait
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
                        is_lock_to_product
                        total_child
                        target_buyer
                        label_voucher{
                        label_quota
                        label_quota_formatted
                        label_quota_color_type
                        label_creator
                        label_creator_formatted
                        label_creator_color_type
                        label_subsidy_info
                        label_subsidy_info_formatted
                        label_subsidy_info_color_type
                        label_budgets_voucher{
                          label_budget_voucher_formatted
                          label_budget_voucher
                          label_budget_voucher_value
                        }
                      }
                      is_editable
                      subsidy_detail{
                        quota_subsidized{
                          voucher_quota
                          remaining_quota
                          booked_global_quota
                          confirmed_global_quota
                        }
                        program_detail{
                          program_name
                          program_status
                          program_label
                          program_label_detail
                          promotion_status
                          promotion_label
                        }
                      }
                    }
                  }
               }
            }
        """
    }

    private val query = object : GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY.trimIndent()
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(voucherId: Long, voucherStatus: ArrayList<VoucherStatus>? = null): List<Voucher> {
        val filterToApply = voucherStatus?.joinToString { it.id.toString() }.orEmpty().replace(" ", "")
        val request = buildRequest(voucherId, filterToApply)
        val response = repository.response(listOf(request))
        val errors = response.getError(GetMerchantVoucherListResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getSuccessData<GetMerchantVoucherListResponse>()
            return mapper.mapRemoteModelToUiModel(data.result.data.vouchers)
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }

    private fun buildRequest(
        voucherId: Long,
        voucherStatus: String
    ): GraphqlRequest {
        return GraphqlRequest(
            query,
            GetMerchantVoucherListResponse::class.java,
            mapOf(
                PARAM_KEY to voucherId,
                PARAM_FILTER_VOUCHER_STATUS to voucherStatus,
                PARAM_SORT_BY to VALUE_SORT
            )
        )
    }
}
