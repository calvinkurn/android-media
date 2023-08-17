package com.tokopedia.scp_rewards.detail.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.common.utils.API_VERSION_PARAM
import com.tokopedia.scp_rewards.detail.domain.model.MedalDetailResponseModel
import javax.inject.Inject

@GqlQuery("ScpRewardsMedaliDetailPage", SCP_REWARDS_MEDAL_DETAIL_QUERY)
class MedalDetailUseCase @Inject constructor() : GraphqlUseCase<MedalDetailResponseModel>() {
    suspend fun getMedalDetail(medaliSlug: String, sourceName: String, pageName: String): MedalDetailResponseModel {
        setTypeClass(MedalDetailResponseModel::class.java)
        setGraphqlQuery(ScpRewardsMedaliDetailPage())
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
        API_VERSION_PARAM to "2.0.0"
    )
}

private const val SCP_REWARDS_MEDAL_DETAIL_QUERY = """
    query scpRewardsMedaliDetailPage(${'$'}pageName:String, ${'$'}medaliSlug:String, ${'$'}sourceName:String) {
      scpRewardsMedaliDetailPage(input:{pageName:${'$'}pageName, medaliSlug:${'$'}medaliSlug, sourceName:${'$'}sourceName}) {
        resultStatus {
          code
          status
        }
        medaliDetailPage {
          backgroundImageURL
          backgroundImageColor
          frameAltImageURL
          frameMaskingImageURL
          innerIconImageURL
          iconImageURL
          maskingImageURL
          shutterImageURL
          shutterText
          shimmerAltImageURL
          shimmerShutterLottieURL
          outerBlinkingImageURL
          outerBlinkingLottieURL
          baseImageURL
          sourceText
          sourceFontColor
          sourceBackgroundColor
          name
          description
          isMedaliGrayScale
          tncButton {
            text
            url
            appLink
          }
          coachmark {
            text
            showNumberOfTimes
          }
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
          section {
            id
            layout
            medaliSectionTitle {
              content
            }
            backgroundColor
            jsonParameter
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
