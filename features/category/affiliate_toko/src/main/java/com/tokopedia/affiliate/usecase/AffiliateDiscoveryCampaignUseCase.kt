package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateDiscoveryCampaignResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateDiscoveryCampaignUseCase.Companion.QUERY
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("AffiliateDiscoveryCampaign", QUERY)
class AffiliateDiscoveryCampaignUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {
    companion object {
        const val QUERY =
            """query recommendedAffiliateDiscoveryCampaign(${'$'}page: Int, ${'$'}limit: Int) {
  recommendedAffiliateDiscoveryCampaign(page: ${'$'}page, limit: ${'$'}limit) {
    data {
      items {
        appUrl
        commission {
          percentage
        }
        additionalInformation {
          htmlText
          type
          color
        }
        title
        pageId
        url
        imageBanner
      }
    }
    error {
      errorType
      errorStatus
      title
      message
      errorImage {
        IosURL
        DesktopURL
        MobileURL
        AndroidURL
      }
    }
  }
}
"""
    }

    suspend fun getAffiliateDiscoveryCampaign(
        page: Int,
        limit: Int
    ): AffiliateDiscoveryCampaignResponse =
        repository.getGQLData(
            AffiliateDiscoveryCampaign.GQL_QUERY,
            AffiliateDiscoveryCampaignResponse::class.java,
            createRequestParamsList(page, limit)
        )

    private fun createRequestParamsList(page: Int, limit: Int): HashMap<String, Any> =
        hashMapOf(
            "page" to page,
            "limit" to limit
        )
}
