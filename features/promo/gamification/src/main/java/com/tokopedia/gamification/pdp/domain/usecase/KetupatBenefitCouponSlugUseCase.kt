package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.model.KetupatBenefitCouponSlugData
import com.tokopedia.gamification.pdp.data.model.request.BenefitCouponSlugRequest
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponSlugUseCase.Companion.GET_TOKOPOINTS_COUPON_LIST_STACK
import com.tokopedia.gamification.pdp.domain.usecase.KetupatBenefitCouponUseCase.Companion.GET_TOKOPOINTS_COUPON_LIST
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetTokopointsCouponListStack", GET_TOKOPOINTS_COUPON_LIST_STACK)
class KetupatBenefitCouponSlugUseCase @Inject constructor(
    private val repository: GamificationRepository
) {

    private fun createRequestParams(): Map<String, Any> {
        return mapOf<String, Any>(
            INPUT to BenefitCouponSlugRequest(
                serviceID = "",
                categoryIDCoupon = -1,
                categoryID = 0,
                limit = 10,
                page = 1,
                includeExtraInfo = 0,
                source = "discovery-page",
                isGetPromoInfo = true,
                apiVersion = "2.0.0",
                catalogSlugs = arrayListOf("DC200STG", "DC300STG", "DC300STG"),
                clientID = "disco"
            )
        )
    }

    suspend fun getTokopointsCouponListStack(): KetupatBenefitCouponSlugData {
        return repository.getGQLData(
            GetTokopointsCouponListStack.GQL_QUERY,
            KetupatBenefitCouponSlugData::class.java,
            createRequestParams()
        )
    }

    companion object {
        private const val INPUT = "input"
        const val GET_TOKOPOINTS_COUPON_LIST_STACK = """
    query tokopointsCouponListStack(${'$'}input: CouponListRequest) {
    tokopointsCouponListStack(input: ${"$"}input) {
        tokopointsCouponDataStack {
          id
          promoID
          code
          imageURL
          imageURLMobile
          imageHalfURL
          imageHalfURLMobile
          redirectURL
          redirectAppLink
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
          minimumUsage
          minimumUsageLabel
          isNewCoupon
          isStacked
          stackID
          isHighlighted
          upperLeftSection {
            textAttributes {
              text
              color
              isBold
            }
            backgroundColor
          }
          promoInfo {
            promoType
            promoIcon
          }
        }
        tokopointsPaging {
          hasNext
        }
        tokopointsExtraInfo {
          linkUrl
          infoHTML
          linkText
        }
        tokopointsEmptyMessage {
          title
          subTitle
        }
    }
}
"""
    }
}
