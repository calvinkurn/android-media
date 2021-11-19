package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateGenerateLinkData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Generate_Link
import com.tokopedia.affiliate.model.request.GenerateLinkRequest
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateGenerateLinkUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(id: Int?, url: String?, identifier: String?): Map<String, Any> {
        return  mapOf<String,Any>(PARAM_INPUT to GenerateLinkRequest.Input(arrayListOf(id),
                arrayListOf(GenerateLinkRequest.Input.Link(
                        PARAM_PDP,
                        url ?: "",
                        identifier ?: "",
                        0
                ))))
    }

    suspend fun affiliateGenerateLink(id: Int?, url: String?, identifier: String?): AffiliateGenerateLinkData.AffiliateGenerateLink.Data? {
        return repository.getGQLData(
                GQL_Affiliate_Generate_Link,
                AffiliateGenerateLinkData::class.java,
                createRequestParams(id, url, identifier)
        ).affiliateGenerateLink.data?.firstOrNull()
    }

    companion object {
        private const val PARAM_INPUT = "input"
        private const val PARAM_PDP = "pdp"
    }
}