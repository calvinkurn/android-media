package com.tokopedia.search.result.domain.usecase.searchcoupon

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.search.result.domain.model.SearchCouponModel

internal object SearchCouponQuery {
    const val GET_COUPON_QUERY_NAME = "GetCoupon"
    const val GET_COUPON_QUERY_PAYLOAD = """
        query GetCoupon(${'$'}slugs: [String]!, ${'$'}themeType: String!) {
          promoCatalogGetCouponListWidget(source: "", themeType: ${'$'}themeType, widgetType: "", 
            filter: {catalogAttributeFilter: {
              ids: ["1"], 
              categoryIDs: ["1"], 
              subCategoryIDs: ["1"],
              slugs: ${'$'}slugs}
            }){
            resultStatus {
              code
              status
            }
            couponListWidget {
              widgetInfo {
                headerList {
                  key
                  parent {
                    key
                    text
                    colorInfo{
                      colorList
                    }
                  }
                  child {
                    key
                    text
                    colorInfo{
                      colorList
                    }
                  }
                }
                titleList {
                  key
                  parent {
                    key
                    text
                    colorInfo{
                      colorList
                    }
                  }
                }
                subtitleList {
                  key
                  parent {
                    key
                    text
                    colorInfo{
                      colorList
                    }
                  }
                }
                footerList {
                  key
                  parent {
                    key
                    text
                    colorInfo{
                      colorList
                    }
                  }
                  child {
                    key
                    text
                    valueList{
                      key
                      value
                    }
                    colorInfo{
                      colorList
                    }
                  }
                }
                ctaList {
                  text
                  type
                  isDisabled
                  jsonMetadata
                  toasters{
                    type
                    message
                  }
                }
                badgeList{
                  key
                  text
                }
                iconURL
                backgroundInfo {
                  imageURL
                }
                actionInfo {
                  text
                  url
                  applink
                  type
                  isDisabled
                  jsonMetadata
                }
              }
            }
          }
        }
    """
    const val REDEEM_COUPON_QUERY_NAME = "RedeemCoupon"
    const val REDEEM_COUPON_QUERY_PAYLOAD = """
        mutation RedeemCoupon(${'$'}catalog_id:Int!){
          hachikoRedeem(catalog_id:${'$'}catalog_id,is_gift:0,gift_user_id:9103587,gift_email:"",notes:""){
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
            ctaList {
              icon
              text
              url
              applink
              type
              isShown
              isDisabled
              jsonMetadata
            }
          }
        }
    """

    @GqlQuery(GET_COUPON_QUERY_NAME, GET_COUPON_QUERY_PAYLOAD)
    fun createGetCouponRequest(variables: Map<String, String>): GraphqlRequest =
        GraphqlRequest(
            GetCoupon(),
            SearchCouponModel::class.java,
            variables
        )

    @GqlQuery(REDEEM_COUPON_QUERY_NAME, REDEEM_COUPON_QUERY_PAYLOAD)
    fun createRedeemCouponRequest(variables: Map<String, String>): GraphqlRequest =
        GraphqlRequest(
            RedeemCoupon(),
            SearchCouponModel::class.java,
            variables
        )
}
