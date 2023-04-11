package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponListResponse
import com.tokopedia.tokomember_seller_dashboard.model.TmMVFilter
import javax.inject.Inject

class TmCouponUsecase@Inject constructor(
    graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmCouponListResponse>(graphqlRepository) {

    fun getCouponList(
        success: (TmCouponListResponse) -> Unit,
        failure: (Throwable) -> Unit,
        voucherStatus: String, voucherType: Int?, page: Int, perPage: Int
    ){
        setTypeClass(TmCouponListResponse::class.java)
        setRequestParams(getRequestParams(voucherStatus, voucherType, page, perPage))
        setGraphqlQuery(QUERY_TM_COUPON_LIST)
        execute(
            {
                success(it)
            },
            {
                failure(it)
            }
        )
    }

    private fun getRequestParams(voucherStatus: String, voucherType: Int?, page: Int, perPage: Int): Map<String, Any> {
        val req = TmMVFilter(voucherType, voucherStatus, "1", "3")
        val map = mapOf<String, Any?>(
            "voucher_type" to voucherType,
            "voucher_status" to voucherStatus,
            "is_public" to "1",
            "target_buyer" to "3",
            "page" to page,
            "per_page" to perPage,
        )
        return mapOf(FILTER_INPUT to map)
    }
}

const val FILTER_INPUT = "Filter"
const val QUERY_TM_COUPON_LIST = """
    
    query MerchantPromotionGetMVList(${'$'}Filter: MVFilter!) {
    MerchantPromotionGetMVList(Filter: ${'$'}Filter) {
        header{
            process_time
            message
            reason
            error_code
        }
        data{
            paging{
                per_page
                page
                has_prev
                has_next
            }
            vouchers{
                voucher_id
                shop_id
                voucher_name
                voucher_type
                voucher_type_formatted
                voucher_image
                voucher_image_square
                voucher_image_portrait
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
                remaining_quota
                booked_global_quota
                used_global_quota
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
                hyperlink{
                    edit
                    edit_quota_ajax
                    delete
                    stop
                    share
                }
                target_buyer
                total_new_follower
                minimum_tier_level
                performance_income
                performance_outcome
                performance_income_formatted
                performance_outcome_formatted
                is_lock_to_product
                is_vps
                package_name
                vps_unique_id
                voucher_package_id
                vps_bundling_id
                is_subsidy
                applink
                weblink
                warehouse_id
            }
        }
    }
}
"""
