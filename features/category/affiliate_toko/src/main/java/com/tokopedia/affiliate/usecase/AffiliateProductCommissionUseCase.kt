package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliateProductCommissionData
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateProductCommissionUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(affiliateId: String, productDetails : ArrayList<String>): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_AFFILIATE_ID] = affiliateId
        request[PARAM_PRODUCT_DETAILS] = productDetails
        return request
    }

    suspend fun affiliateProductCommission(affiliateId: String, productDetails : ArrayList<String>): AffiliateProductCommissionData {
//        return repository.getGQLData(
//                GQLAffiliateProductCommission,
//                AffiliateProductCommissionData::class.java,
//                createRequestParams(affiliateId,productDetails)
//        )
        return getDummyData()
    }

    fun getDummyData(): AffiliateProductCommissionData {
        val commissions = arrayListOf<AffiliateProductCommissionData.Commission>()
        val commission = AffiliateProductCommissionData.Commission(
                222,
                13,
                2,
                "Rp16.000",
                16000.0,
                "Rp1.600",
                1600,
                "10%",
                10

        )
        commissions.add(commission)
        return AffiliateProductCommissionData(
                true,
                commissions,
                AffiliateProductCommissionData.Error("")
        )
    }

    companion object {
        private const val PARAM_AFFILIATE_ID = "affiliateID"
        private const val PARAM_PRODUCT_DETAILS = "productDetails"
    }
}