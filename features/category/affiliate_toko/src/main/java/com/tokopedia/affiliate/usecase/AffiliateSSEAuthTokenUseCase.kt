package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateSSEToken
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateSSEAuthTokenUseCase.Companion.GQL_Affiliate_SSE_Token
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetAffiliateToken", GQL_Affiliate_SSE_Token)
class AffiliateSSEAuthTokenUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {
    companion object {
        const val GQL_Affiliate_SSE_Token: String = """
            query GetAffiliateToken(){
                getAffiliateToken(){
                    Token
                }
            }
            """
    }

    suspend fun getAffiliateToken(): AffiliateSSEToken {
        return repository.getGQLData(
            GetAffiliateToken.GQL_QUERY,
            AffiliateSSEToken::class.java,
            mapOf()
        )
    }
}
