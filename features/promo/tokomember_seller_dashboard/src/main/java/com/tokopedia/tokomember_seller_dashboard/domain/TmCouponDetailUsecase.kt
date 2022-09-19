package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponDetailResponseData
import com.tokopedia.tokomember_seller_dashboard.util.ANDROID
import com.tokopedia.tokomember_seller_dashboard.util.SOURCE
import com.tokopedia.tokomember_seller_dashboard.util.VOUCHER_ID
import javax.inject.Inject

class TmCouponDetailUsecase @Inject constructor(graphqlRepository: GraphqlRepository): GraphqlUseCase<TmCouponDetailResponseData>(graphqlRepository) {
    fun getCouponDetail(
        success: (TmCouponDetailResponseData) -> Unit,
        fail: (Throwable) -> Unit,
        voucherId: Int){
        setGraphqlQuery(TM_COUPON_DETAIL)
        setTypeClass(TmCouponDetailResponseData::class.java)
        setRequestParams(getRequestParams(voucherId))
        execute({
            success(it)
        },{
            fail(it)
        }
        )
    }

    private fun getRequestParams(voucherId: Int): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map[VOUCHER_ID] = voucherId
        map[SOURCE] = ANDROID
        return map
    }
}

const val TM_COUPON_DETAIL = """
   query merchantPromotionGetMVDataByID(${'$'}voucher_id: Int!, ${'$'}source: String!){
    merchantPromotionGetMVDataByID(voucher_id: ${'$'}voucher_id, source: ${'$'}source) {
    
    header{
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
      is_quota_avaiable
      voucher_type_formatted
      voucher_status_formatted
      voucher_discount_type_formatted
      voucher_discount_amt_formatted
      voucher_discount_amt_max_formatted
      remaning_quota
      voucher_minimum_amt_formatted
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
    }
  }
}
"""