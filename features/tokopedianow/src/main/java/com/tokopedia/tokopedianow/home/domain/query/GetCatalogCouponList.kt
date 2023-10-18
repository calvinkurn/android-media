package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.tokopedianow.home.domain.query.GetCatalogCouponList.QUERY_NAME
import com.tokopedia.tokopedianow.home.domain.query.GetCatalogCouponList.QUERY_VALUE

@GqlQuery(QUERY_NAME, QUERY_VALUE)
internal object GetCatalogCouponList {
    const val QUERY_NAME = "tokopointsCatalogWithCouponList"
    const val QUERY_VALUE = """
        query tokopointsCatalogWithCouponList(${'$'}categorySlug: String!, ${'$'}catalogSlugs: [String!]){
          tokopointsCatalogWithCouponList(categorySlug:${'$'}categorySlug,catalogSlugs:${'$'}catalogSlugs){
            resultStatus {
              code
              status
              message
            }
           catalogWithCouponList {
                id
                promoID
                quota
                title
                subTitle
                isDisabled
                disableErrorMessage
                thumbnailUrl
                thumbnailUrlMobile
                imageUrl
                imageUrlMobile
                slug
                baseCode
                upperTextDesc
                isDisabledButton
                catalogType
                couponCode
                cta
                ctaDesktop
                minimumUsageLabel
                minimumUsage
                smallImageUrl
                smallImageUrlMobile
                buttonStr
                url
                appLink
           }
           countdownInfo{
              isShown
              type
              label
              countdownUnix
              countdownStr
              textColor
              backgroundColor
            }
          }
        }
    """
}
