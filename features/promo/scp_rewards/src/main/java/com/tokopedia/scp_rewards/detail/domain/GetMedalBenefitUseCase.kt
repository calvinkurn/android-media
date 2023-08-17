package com.tokopedia.scp_rewards.detail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.detail.domain.model.MedalBenefitResponseModel
import javax.inject.Inject

@GqlQuery("ScpRewardsGetMedaliBenefitGQL", SCP_REWARDS_MEDAL_BENEFIT_QUERY)
class GetMedalBenefitUseCase @Inject constructor() : GraphqlUseCase<MedalBenefitResponseModel>() {
    suspend fun getMedalBenefits(medaliSlug: String, sourceName: String, pageName: String): MedalBenefitResponseModel {
        setTypeClass(MedalBenefitResponseModel::class.java)
        setGraphqlQuery(ScpRewardsGetMedaliBenefitGQL())
        setRequestParams(getRequestParams(medaliSlug, sourceName, pageName))
        return executeOnBackground()
    }

    companion object {
        private const val PAGE_NAME_KEY = "pageName"
        private const val MEDALI_SLUG_KEY = "medaliSlug"
        private const val SOURCE_NAME_KEY = "sourceName"
    }

    private fun getRequestParams(medaliSlug: String, sourceName: String, pageName: String) = mapOf(
        PAGE_NAME_KEY to pageName,
        MEDALI_SLUG_KEY to medaliSlug,
        SOURCE_NAME_KEY to sourceName,
    )
}

private const val SCP_REWARDS_MEDAL_BENEFIT_QUERY = """
    query scpRewardsGetMedaliBenefitList(${'$'}pageName:String, ${'$'}medaliSlug:String, ${'$'}sourceName:String) {
      scpRewardsGetMedaliBenefitList(input:{pageName:${'$'}pageName, medaliSlug:${'$'}medaliSlug, sourceName:${'$'}sourceName}) {
        resultStatus {
          code
          status
        }
        medaliBenefitList {
          category {
            id
            text
            iconImageURL
          }
          benefitInfo
          benefit {
            categoryID
            backgroundImageURL
            podiumImageURL
            iconImageURL
            ribbonImageURL
            dividerImageURL
            statusImageURL
            isActive
            status
            title
            tnc {
              text
            }
            statusDescription
            expiryCounter
            info
            url
            appLink
            benefitCTA {
              unifiedStyle
              text
              url
              appLink
              isAutoApply
              couponCode
            }
          }
          cta {
            unifiedStyle
            text
            url
            appLink
          }
          paging {
            hasNext
          }
        }
      }
    }
"""

