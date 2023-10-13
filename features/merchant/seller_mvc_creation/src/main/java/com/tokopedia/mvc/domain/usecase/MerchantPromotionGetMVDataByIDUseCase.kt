package com.tokopedia.mvc.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.mvc.data.mapper.MerchantPromotionGetMVDataByIDMapper
import com.tokopedia.mvc.data.response.MerchantPromotionGetMVDataByIDResponse
import com.tokopedia.mvc.domain.entity.VoucherDetailData
import javax.inject.Inject

class MerchantPromotionGetMVDataByIDUseCase @Inject constructor(
    private val repository: GraphqlRepository,
    private val mapper: MerchantPromotionGetMVDataByIDMapper
) : GraphqlUseCase<VoucherDetailData>(repository) {

    init {
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
    }

    companion object {
        private const val REQUEST_PARAM_VOUCHER_ID = "voucher_id"
        private const val REQUEST_PARAM_SOURCE = "source"
        private const val SOURCE = "android-sellerapp"
    }

    private val query = object : GqlQueryInterface {
        private val OPERATION_NAME = "merchantPromotionGetMVDataByID"
        private val QUERY = """
              query $OPERATION_NAME(${'$'}voucher_id: Int!, ${'$'}source: String!){
                $OPERATION_NAME(voucher_id: ${'$'}voucher_id, source: ${'$'}source){
                    header  {
                      process_time
                      message
                      reason
                      error_code
                    }
                    data{
                      voucher_id
                      shop_id
                      voucher_name
                      voucher_type
                      voucher_image
                      voucher_image_square
                      voucher_image_portrait
                      voucher_status
                      voucher_discount_type
                      voucher_discount_amt
                      voucher_discount_amt_max
                      voucher_minimum_amt
                      voucher_quota
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
                      is_child
                      is_quota_avaiable
                      voucher_type_formatted
                      voucher_status_formatted
                      voucher_discount_type_formatted
                      voucher_discount_amt_formatted
                      voucher_discount_amt_max_formatted
                      voucher_minimum_amt
                      remaning_quota
                      tnc
                      booked_global_quota
                      confirmed_global_quota
                      target_buyer
                      minimum_tier_level
                      is_lock_to_product
                      is_vps
                      package_name
                      vps_unique_id
                      voucher_package_id
                      vps_bundling_id
                      is_subsidy
                      product_ids {
                          parent_product_id
                          child_product_id
                      }
                      applink
                      weblink
                      warehouse_id
                      voucher_minimum_amt_type
                      voucher_minimum_amt_type_formatted
                      is_period
                      total_period
                      voucher_lock_type
                      voucher_lock_id
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
        """.trimIndent()

        override fun getOperationNameList(): List<String> = listOf(OPERATION_NAME)
        override fun getQuery(): String = QUERY
        override fun getTopOperationName(): String = OPERATION_NAME
    }

    suspend fun execute(param: Param): VoucherDetailData {
        val request = buildRequest(param)
        val response = repository.response(listOf(request))
        val data = response.getSuccessData<MerchantPromotionGetMVDataByIDResponse>()
        return mapper.map(data)
    }

    private fun buildRequest(param: Param): GraphqlRequest {
        val params = mapOf(
            REQUEST_PARAM_VOUCHER_ID to param.voucherId,
            REQUEST_PARAM_SOURCE to param.source
        )

        return GraphqlRequest(
            query,
            MerchantPromotionGetMVDataByIDResponse::class.java,
            params
        )
    }

    data class Param(
        val voucherId: Long,
        val source: String = SOURCE
    )
}
