package com.tokopedia.exploreCategory.usecase

import com.tokopedia.exploreCategory.model.AffiliateGenerateLinkData
import com.tokopedia.exploreCategory.model.AffiliateLinkRequest
import com.tokopedia.exploreCategory.model.raw.GQL_Affiliate_Generate_Link
import com.tokopedia.exploreCategory.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateGenerateLinkUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(affiliateId: String, name: String?, url: String?, identifier: String?): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_AFFILIATE_ID] = affiliateId
        request[PARAM_CHANNEL] = arrayListOf(name)
        request[PARAM_LINK] = arrayListOf(AffiliateLinkRequest(PARAM_PDP, url, identifier, arrayListOf()))
        return request
    }

    suspend fun affiliateGenerateLink(affiliateId: String, name: String?, url: String?, identifier: String?): AffiliateGenerateLinkData.AffiliateGenerateLink.Data {
        return repository.getGQLData(
                GQL_Affiliate_Generate_Link,
                AffiliateGenerateLinkData::class.java,
                createRequestParams(affiliateId, name, url, identifier)
        ).affiliateGenerateLink.data
    }

    companion object {
        private const val PARAM_AFFILIATE_ID = "affiliateID"
        private const val PARAM_CHANNEL = "channel"
        private const val PARAM_LINK = "link"
        private const val PARAM_PDP = "pdp"
    }


}