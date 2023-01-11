package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_SSE_Token
import com.tokopedia.affiliate.model.response.AffiliateSSEToken
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateSSEAuthTokenUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {
    suspend fun getAffiliateToken(): AffiliateSSEToken {
        return repository.getGQLData(
            GQL_Affiliate_SSE_Token,
            AffiliateSSEToken::class.java,
            mapOf()
        )
    }
}
