package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateOnBoardingData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_On_Boarding
import com.tokopedia.affiliate.model.request.OnBoardingRequest
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateOnBoardingUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(channels : ArrayList<OnBoardingRequest.Channel>): Map<String, Any> {
        return  mapOf<String,Any>(PARAM_CHANNEL to OnBoardingRequest(channels))
    }

    suspend fun affiliateOnBoarding(channels: ArrayList<OnBoardingRequest.Channel>): AffiliateOnBoardingData.OnBoardAffiliate? {
        return repository.getGQLData(
                GQL_Affiliate_On_Boarding,
                AffiliateOnBoardingData::class.java,
                createRequestParams(channels)
        ).onBoardAffiliate
    }

    companion object {
        private const val PARAM_CHANNEL = "channel"
    }
}