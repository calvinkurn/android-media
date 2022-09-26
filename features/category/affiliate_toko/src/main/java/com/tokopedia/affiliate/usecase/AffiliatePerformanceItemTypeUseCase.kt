package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Performance_Item_Type_List
import com.tokopedia.affiliate.model.response.AffiliatePerformanceItemTypeListData
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliatePerformanceItemTypeUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    suspend fun affiliatePerformanceItemTypeList(
    ): AffiliatePerformanceItemTypeListData {
        return repository.getGQLData(
            GQL_Affiliate_Performance_Item_Type_List,
            AffiliatePerformanceItemTypeListData::class.java,
            emptyMap()
        )
    }

}