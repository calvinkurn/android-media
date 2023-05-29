package com.tokopedia.scp_rewards.detail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import com.tokopedia.scp_rewards.detail.domain.model.SCPRewardsGetDetailPageRequest
import javax.inject.Inject

@GqlQuery("ScpRewardsCelebrationPage", SCP_REWARDS_MEDAL_DETAIL_QUERY)
class MedalDetailUseCase @Inject constructor() : GraphqlUseCase<MedalDetailResponseModel>() {
    suspend fun getMedalDetail(medaliSlug: String, sourceName: String, pageName: String): MedalDetailResponseModel {
        setTypeClass(MedalDetailResponseModel::class.java)
        setGraphqlQuery(ScpRewardsCelebrationPage())
        setRequestParams(getRequestParams(medaliSlug, sourceName, pageName))
        return executeOnBackground()
    }

    private fun getRequestParams(medaliSlug: String, sourceName: String, pageName: String) = mapOf(
        "input" to SCPRewardsGetDetailPageRequest(medaliSlug, sourceName, pageName)
    )
}

private const val SCP_REWARDS_MEDAL_DETAIL_QUERY = """
    query scpRewardsMedaliDetailPage(${'$'}input: SCPRewardsMedaliDetailPageRequest!) {
      scpRewardsMedaliDetailPage(input:${'$'}input) {
        resultStatus {
          code
          status
        }
        medaliDetailPage {
          backgroundImageURL
          backgroundImageColor
          frameImageURL
          innerIconImageURL
          maskingImageURL
          shutterImageURL
          shutterText
          shimmerImageURL
          shimmerShutterLottieURL
          outerBlinkingImageURL
          outerBlinkingLottieURL
          baseImageURL
          sourceText
          sourceFontColor
          sourceBackgroundColor
          name
          description
          mission {
            title
            progress
            task {
              isCompleted
              title
            }
          }
          benefit {
            imageURL
            isActive
            status
            statusDescription
          }
          benefitButton {
            unifiedStyle
            text
            url
            appLink
            isAutoApply
            couponCode
          }
        }
      }
    }
"""
