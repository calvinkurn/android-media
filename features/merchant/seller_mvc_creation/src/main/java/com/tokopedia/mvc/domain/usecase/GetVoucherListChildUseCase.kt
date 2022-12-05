package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.GetVoucherListMapper
import com.tokopedia.mvc.data.response.GetMerchantVoucherListResponse
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetVoucherListChildUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: GetVoucherListMapper
): GraphqlUseCase<List<Voucher>>(repository) {

    companion object {
        private const val PARAM_KEY = "voucherId"
        private const val OPERATION_NAME = "MerchantPromotionGetChildMVList"
        private const val QUERY = """
            query MerchantPromotionGetChildMVList(${'$'}voucherId: Int!) {
                MerchantPromotionGetChildMVList(parent_voucher_id: ${'$'}voucherId, Filter: {}) {
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
                      }
                    }
                }
            }
        """
    }

    private val query = object: GqlQueryInterface {
        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY.trimIndent()
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(voucherId: Long): List<Voucher> {
        val request = buildRequest(voucherId)
        val response = repository.response(listOf(request))
        val errors = response.getError(GetMerchantVoucherListResponse::class.java)
        if (errors.isNullOrEmpty()) {
            val data = response.getSuccessData<GetMerchantVoucherListResponse>()
            return mapper.mapRemoteModelToUiModel(data.result.data.vouchers)
        } else {
            throw MessageErrorException(errors.joinToString { it.message })
        }
    }

    private fun buildRequest(voucherId: Long): GraphqlRequest {
        return GraphqlRequest(
            query,
            GetMerchantVoucherListResponse::class.java,
            mapOf(PARAM_KEY to voucherId)
        )
    }
}
