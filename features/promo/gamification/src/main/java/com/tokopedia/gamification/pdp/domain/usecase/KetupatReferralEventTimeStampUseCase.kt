package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.model.KetupatReferralEventTimeStamp
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetKetupatReferralEventTimeStamp", KetupatReferralEventTimeStampUseCase.REFERRAL_EVENT_TIME_STAMP_DATA)
class KetupatReferralEventTimeStampUseCase @Inject constructor(
    private val repository: GamificationRepository
){
    companion object{
        private const val EVENT_SLUG = "eventSlug"
        const val REFERRAL_EVENT_TIME_STAMP_DATA = """
            query Gamification_gameReferralStamp(${'$'}eventSlug: String!) {
              gameReferralStamp(eventSlug: ${'$'}eventSlug) {
                resultStatus {
                  code
                  message
                  reason
                }
                currentStampCount
                maxStampCount
                stampLevelData {
                  LevelIndex
                  TotalStampNeeded
                }
              }
              gameReferralEventContent(eventSlug:${'$'}eventSlug){
                resultStatus{
                  code
                  message
                  reason
                }
                eventContent{
                  remainingTime
                }
              }
            }
        """
    }
    private fun createRequestParams(slug: String): HashMap<String, Any> {
        val input = HashMap<String, Any>()
        input[EVENT_SLUG] = slug
//        val input = HashMap<String, Any>()
//        input[EVENT_SLUG] = request
        return input
    }

    suspend fun getKetupatReferralTimeStampData(slug: String): KetupatReferralEventTimeStamp {
        return repository.getGQLData(
            GetKetupatReferralEventTimeStamp.GQL_QUERY,
            KetupatReferralEventTimeStamp::class.java,
            createRequestParams(slug)
        )
    }
}
