package com.tokopedia.tokomember_seller_dashboard.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokomember_seller_dashboard.model.TmCouponInitialResponse
import javax.inject.Inject

class TmKuponInitialUsecase @Inject constructor(graphqlRepository: GraphqlRepository) :
    GraphqlUseCase<TmCouponInitialResponse>(graphqlRepository) {

    @GqlQuery("TmKuponInitial", KUPON_INITIAL)
    fun getInitialCoupon(
        success: (TmCouponInitialResponse) -> Unit,
        onFail: (Throwable) -> Unit,
        actionType: String,
        couponType: String
    ) {
        this.setTypeClass(TmCouponInitialResponse::class.java)
        this.setGraphqlQuery(TmKuponInitial.GQL_QUERY)
        setRequestParams(getRequestParams(actionType, couponType))
        this.execute({
            success(it)
        }, {
            onFail(it)
        })
    }

    private fun getRequestParams(action: String, couponType: String): Map<String, Any> {
        val reqMap = mutableMapOf<String, Any>()
        reqMap[ACTION] = action
        reqMap[TARGET_BUYER] = 3
        reqMap[COUPON_TYPE] = couponType
        return reqMap
    }

}

const val ACTION = "Action"
const val TARGET_BUYER = "TargetBuyer"
const val COUPON_TYPE = "CouponType"

const val KUPON_INITIAL = """
    
  query getInitiateVoucherPage(${'$'}Action: String!, ${'$'}TargetBuyer: Int!, ${'$'}CouponType: String!){
    getInitiateVoucherPage(Action: ${'$'}Action, TargetBuyer: ${'$'}TargetBuyer , CouponType: ${'$'}CouponType) {
    header {
      process_time
      messages
      reason
      error_code
    }
    data {
      shop_id
      token
      user_id
      access_token
      upload_app_url
      img_banner_base
      img_banner_cover_gm
      img_banner_cover_os
      img_banner_ig_post
      img_banner_ig_story
      img_banner_label_gratis_ongkir
      img_banner_label_cashback
      img_banner_label_cashback_hingga
      prefix_voucher_code
      is_eligible
      max_product
    }
}
}
"""