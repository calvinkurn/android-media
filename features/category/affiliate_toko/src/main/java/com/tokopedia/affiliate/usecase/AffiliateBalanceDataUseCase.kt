package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateBalance
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Balance
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateBalanceDataUseCase @Inject constructor(
        private val repository: AffiliateRepository
) {
    suspend fun getAffiliateBalance(): AffiliateBalance {
        return repository.getGQLData(
                GQL_Affiliate_Balance,
                AffiliateBalance::class.java,
                mapOf()
        )
    }

}