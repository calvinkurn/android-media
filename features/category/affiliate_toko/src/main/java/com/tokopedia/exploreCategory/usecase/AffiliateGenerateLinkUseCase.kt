package com.tokopedia.exploreCategory.usecase

import com.tokopedia.exploreCategory.model.AffiliateGenerateLinkData
import com.tokopedia.exploreCategory.model.raw.GQL_Affiliate_Generate_Link
import com.tokopedia.exploreCategory.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateGenerateLinkUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(affiliateId: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_AFFILIATE_ID] = affiliateId
        return request
    }

    suspend fun affiliateGenerateLink(affiliateId: String): AffiliateGenerateLinkData {
        return repository.getGQLData(
                GQL_Affiliate_Generate_Link,
                AffiliateGenerateLinkData::class.java,
                createRequestParams(affiliateId)
        )
    }

    companion object {
        private const val PARAM_AFFILIATE_ID = "affiliateID"
    }


}