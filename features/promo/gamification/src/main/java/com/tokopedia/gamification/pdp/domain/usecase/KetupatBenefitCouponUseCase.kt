package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponData
import com.tokopedia.gamification.pdp.data.model.request.BenefitCouponRequest
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase.Companion.GET_TOKOPOINTS_COUPON_LIST
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetTokopointsCouponList", GET_TOKOPOINTS_COUPON_LIST)
class KetupatBenefitCouponUseCase @Inject constructor(
    private val repository: GamificationRepository
) {

    private fun createRequestParams(benefitCouponRequest: BenefitCouponRequest): Map<String, Any> {
        return mapOf<String, Any>(
            INPUT to benefitCouponRequest
        )
    }

    suspend fun getTokopointsCouponList(benefitCouponRequest: BenefitCouponRequest): KetupatBenefitCouponData {
        return repository.getGQLData(
            GetTokopointsCouponList.GQL_QUERY,
            KetupatBenefitCouponData::class.java,
            createRequestParams(benefitCouponRequest)
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
