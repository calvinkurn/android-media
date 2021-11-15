package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliateOnBoardingData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_On_Boarding
import com.tokopedia.affiliate.model.raw.request.OnBoardingRequest
import com.tokopedia.affiliate.repository.AffiliateRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AffiliateOnBoardingUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(channels : ArrayList<OnBoardingRequest.Channel>): Map<String, Any> {
        return  mapOf<String,Any>(PARAM_CHANNEL to OnBoardingRequest(channels))
    }

    suspend fun affiliateOnBoarding(channels : ArrayList<OnBoardingRequest.Channel>): AffiliateOnBoardingData.OnBoardAffiliate? {
        val a =  repository.getGQLData(
                GQL_Affiliate_On_Boarding,
                AffiliateOnBoardingData::class.java,
                createRequestParams(channels)
        ).onBoardAffiliate

        delay(5000)

        return a
    }

    companion object {
        private const val PARAM_CHANNEL = "channel"
    }
}