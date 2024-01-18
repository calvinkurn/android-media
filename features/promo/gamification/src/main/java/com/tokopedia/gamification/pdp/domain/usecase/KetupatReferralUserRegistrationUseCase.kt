package com.tokopedia.gamification.pdp.domain.usecase

import com.tokopedia.gamification.pdp.data.model.KetupatReferralUserRegistration
import com.tokopedia.gamification.pdp.repository.GamificationRepository
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetKetupatReferralUserRegistration", KetupatReferralUserRegistrationUseCase.REFERRAL_REGISTRATION_KETUPAT)
class KetupatReferralUserRegistrationUseCase @Inject constructor(
    private val repository: GamificationRepository
){
    companion object{
        private const val EVENT_SLUG = "eventSlug"
        const val REFERRAL_REGISTRATION_KETUPAT = """
        query gameReferralUserData(${'$'}eventSlug: String!) {
            gameReferralUserData(eventSlug: ${"$"}eventSlug) {
        resultStatus {
            code
            message
            reason
        }
        userData {
            name
            referralCode
            tier {
                tierID
                title
            }
            audienceGroupKey
            isEligible
            isRequested
        }
    }
    }
        """
    }

    private fun createRequestParams(slug: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[EVENT_SLUG] = slug
//        val input = HashMap<String, Any>()
//        input[EVENT_SLUG] = request
        return request
    }

    suspend fun getKetupatReferralUserRegistrationData(slug: String): KetupatReferralUserRegistration {
        return repository.getGQLData(
            GetKetupatReferralUserRegistration.GQL_QUERY,
            KetupatReferralUserRegistration::class.java,
            createRequestParams(slug)
        )
    }
}
