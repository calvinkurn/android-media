package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tokopedianow.home.domain.query.RedeemCoupon.QUERY_NAME
import com.tokopedia.tokopedianow.home.domain.query.RedeemCoupon.QUERY_VALUE

@GqlQuery(QUERY_NAME, QUERY_VALUE)
internal object RedeemCoupon {
    const val QUERY_NAME = "hachikoRedeem"
    const val QUERY_VALUE = """
        mutation hachikoRedeem(${'$'}catalog_id:Int, ${'$'}is_gift:Int, ${'$'}gift_user_id:Int, ${'$'}gift_email:String, ${'$'}notes:String) {
          hachikoRedeem(catalog_id:${'$'}catalog_id,is_gift:${'$'}is_gift,gift_user_id:${'$'}gift_user_id,gift_email:${'$'}gift_email,notes:${'$'}notes){
            coupons {
              id
              owner
              promo_id
              code
              title
              description
              cta
              cta_desktop
              url
              appLink
            }
            reward_points
            redeemMessage
          }
        }
    """
}
