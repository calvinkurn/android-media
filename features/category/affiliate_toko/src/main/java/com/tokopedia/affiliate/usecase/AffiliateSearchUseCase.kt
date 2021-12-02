package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateSearchData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Search
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateSearchUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(filter: ArrayList<String>): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_FILTER] = filter
        return request
    }

    suspend fun affiliateSearchWithLink(filter: ArrayList<String>): AffiliateSearchData {
        return repository.getGQLData(
                GQL_Affiliate_Search,
                AffiliateSearchData::class.java,
                createRequestParams(filter)
        )
    }

    companion object {
        private const val PARAM_FILTER = "filter"
    }
}