package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Generate_Link
import com.tokopedia.affiliate.model.request.GenerateLinkRequest
import com.tokopedia.affiliate.model.response.AffiliateGenerateLinkData
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateGenerateLinkUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(
        id: Int?,
        pageType: String,
        itemId: String,
        url: String?,
        identifier: String?,
        type: String
    ): Map<String, Any> {
        return mapOf<String, Any>(
            PARAM_INPUT to GenerateLinkRequest.Input(
                "",
                arrayListOf(id),
                pageType,
                itemId,
                arrayListOf(
                    GenerateLinkRequest.Input.Link(
                        type,
                        url ?: "",
                        identifier ?: "",
                        0
                    )
                )
            )
        )
    }

    suspend fun affiliateGenerateLink(
        id: Int?,
        pageType: String,
        itemId: String,
        url: String?,
        identifier: String?,
        type: String
    ): AffiliateGenerateLinkData.AffiliateGenerateLink.Data? {
        return repository.getGQLData(
            GQL_Affiliate_Generate_Link,
            AffiliateGenerateLinkData::class.java,
            createRequestParams(id, pageType, itemId, url, identifier, type)
        ).affiliateGenerateLink.generateLinkData?.firstOrNull()
    }

    companion object {
        private const val PARAM_INPUT = "input"
    }
}
