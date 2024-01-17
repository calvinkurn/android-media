package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponData
import com.tokopedia.gamification.pdp.data.model.KetupatLandingPageData
import com.tokopedia.gamification.pdp.data.model.request.BenefitCouponRequest
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase.Companion.GET_TOKOPOINTS_COUPON_LIST
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetTokopointsCouponList", GET_TOKOPOINTS_COUPON_LIST)
class KetupatBenefitCouponUseCase @Inject constructor(
    private val repository: GamificationRepository
) {

    private fun createRequestParams(): Map<String, Any> {
        return mapOf<String, Any>(
            INPUT to BenefitCouponRequest(
                serviceID = "marketplace",
                categoryIDCoupon = 1,
                categoryID = 54,
                limit = 6,
                page = 1
            )
        )
    }

    suspend fun getTokopointsCouponList(): KetupatBenefitCouponData {
        return repository.getGQLData(
            GetTokopointsCouponList.GQL_QUERY,
            KetupatBenefitCouponData::class.java,
            createRequestParams()
        )
    }

    companion object {
        private const val INPUT = "input"
        const val GET_TOKOPOINTS_COUPON_LIST = """
    query tokopointsCouponList(${'$'}input: CouponListRequest) {
    tokopointsCouponList(input: ${"$"}input) {
        tokopointsCouponData{
              id
              promoID
              code
              minimum_usage
              minimum_usage_label
              expired
              title
              catalogTitle
              subTitle
              catalogSubTitle
              description
              icon
              imageUrl
              imageUrlMobile
              thumbnailUrl
              thumbnailUrlMobile
              imageV2Url
              imageV2UrlMobile
              thumbnailV2Url
              thumbnailV2UrlMobile
              cta
              ctaDesktop
              slug
              isHighlighted
              usage {
                  activeCountdown
                  expiredCountdown
                  text
                  usageStr
                  buttonUsage {
                      text
                      url
                      appLink
                      type
                  }
              }
        }
        tokopointsPaging{
              hasNext
        }
        tokopointsExtraInfo{
            infoHTML
            linkText
            linkUrl
        }
        tokopointsEmptyMessage{
            title
            subTitle
        }
    }
}
"""
    }
}
