package com.tokopedia.scp_rewards.celebration.domain

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.scp_rewards.celebration.domain.model.ScpRewardsCelebrationModel
import javax.inject.Inject

@GqlQuery("ScpRewardsCelebrationPage", SCP_REWARDS_QUERY)
class RewardsGetMedaliCelebrationPageUseCase @Inject constructor() : GraphqlUseCase<ScpRewardsCelebrationModel>() {
    suspend fun getRewards(medaliSlug:String,sourceName:String) : ScpRewardsCelebrationModel{
        setTypeClass(ScpRewardsCelebrationModel::class.java)
        setGraphqlQuery(ScpRewardsCelebrationPage())
        setRequestParams(getRequestParams(medaliSlug,sourceName))
        return executeOnBackground()
    }

    private fun getRequestParams(medaliSlug:String,sourceName:String) = mapOf(
        API_VERSION_KEY to "5.0.0",
        MEDALI_SLUG_KEY to medaliSlug,
        SOURCE_NAME_KEY to sourceName
    )

    companion object{
        private const val API_VERSION_KEY = "apiVersion"
        private const val MEDALI_SLUG_KEY = "medaliSlug"
        private const val SOURCE_NAME_KEY = "sourceName"
    }
}

private const val SCP_REWARDS_QUERY = """
query scpRewardsCelebrationPage(${'$'}apiVersion:String, ${'$'}medaliSlug:String, ${'$'}sourceName:String) {
  scpRewardsCelebrationPage(input:{apiVersion:${'$'}apiVersion, medaliSlug:${'$'}medaliSlug, sourceName:${'$'}sourceName}) {
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
    }
  }
}
"""






