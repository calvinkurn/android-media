package com.tokopedia.scp_rewards_touchpoints.bottomsheet.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards_touchpoints.bottomsheet.model.ScpRewardsCelebrationModel
import javax.inject.Inject

@GqlQuery("ScpRewardsCelebrationPage", SCP_REWARDS_QUERY)
class RewardsGetMedaliCelebrationPageUseCase @Inject constructor() : GraphqlUseCase<ScpRewardsCelebrationModel>() {
    suspend fun getRewards(medaliSlug:String,pageName:String) : ScpRewardsCelebrationModel {
        setTypeClass(ScpRewardsCelebrationModel::class.java)
        setGraphqlQuery(ScpRewardsCelebrationPage())
        setRequestParams(getRequestParams(medaliSlug,pageName))
        return executeOnBackground()
    }

    private fun getRequestParams(medaliSlug:String,pageName:String) = mapOf(
        API_VERSION_KEY to "5.0.0",
        MEDALI_SLUG_KEY to medaliSlug,
        PAGE_NAME_KEY to pageName
    )

    companion object{
        private const val API_VERSION_KEY = "apiVersion"
        private const val MEDALI_SLUG_KEY = "medaliSlug"
        private const val PAGE_NAME_KEY = "pageName"
    }
}
private const val SCP_REWARDS_QUERY = """
query scpRewardsCelebrationPage(${'$'}apiVersion:String, ${'$'}medaliSlug:String, ${'$'}pageName:String) {
  scpRewardsCelebrationPage(input:{apiVersion:${'$'}apiVersion, medaliSlug:${'$'}medaliSlug, pageName:${'$'}pageName}) {
    resultStatus {
      code
      status
    }
    celebrationPage{
      isNeedAppUpdate
      backgroundColor
      backgroundColorImageURL
      backgroundImageURL
      title
      medaliIconImageURL
      medaliEffectImageURL
      medaliSpotLightImageURL
      medaliConfettiLottieURL
      medaliConfettiImageURL
      medaliBlinkingLottieURL
      medaliBlinkingImageURL
      medaliSoundEffectURL
      medaliSourceText
      medaliSourceFontColor
      medaliSourceBackgroundColor
      medaliName
      medaliDescription
      redirectDelayInMilliseconds
      redirectURL
      redirectAppLink
      redirectSourceName
      benefit {
        imageURL
        status
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
