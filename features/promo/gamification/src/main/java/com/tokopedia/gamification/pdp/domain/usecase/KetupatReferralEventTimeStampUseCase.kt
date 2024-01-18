package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gql_query_annotation.GqlQuery

@GqlQuery("GetKetupatReferralEventTimeStampUseCase", KetupatReferralEventTimeStampUseCase.REFERRAL_EVENT_TIME_STAMP_DATA)
class KetupatReferralEventTimeStampUseCase {
    companion object{
        private const val EVENT_SLUG = "eventSlug"
        const val REFERRAL_EVENT_TIME_STAMP_DATA = """
            {
  query gameReferralStampData(${'$'}eventSlug: String!) {
  gameReferralStamp(eventSlug: ${"$"}eventSlug) {
    resultStatus {
      code
      message
      reason
    }
    stamps {
      imageUrl
      name
      timestamp
    }
    currentStampCount
    maxStampCount
    stampLevelData{
        LevelIndex
        TotalStampNeeded
        RewardNumber
        MilestoneRewardNumber
  }
  }
  }
  gameReferralEventContent(eventSlug:${"$"}eventSlug){
    resultStatus{
      code
      message
      reason
    }
    eventContent{
      id
      eventId
      name
      description
      referralHomepageUrl
      headerHomepageImageUrl
      titleDescription
      remainingTime
      isShowRecommendation
      bannerTitle
      bannerImageURL
      bannerImageURLMobile
      bannerRedirectURL
      timewindowStatus
      timeUntilStart
      }
    }
}
        """

    }
    private fun createRequestParams(slug: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[KetupatReferralEventTimeStampUseCase.EVENT_SLUG] = slug
        val input = HashMap<String, Any>()
        input[KetupatReferralEventTimeStampUseCase.EVENT_SLUG] = request
        return input
    }
}
