package com.tokopedia.affiliate.usecase


import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Kyc
import com.tokopedia.affiliate.model.response.AffiliateKycDetailsData
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateKycUseCase @Inject constructor(
        private val repository: AffiliateRepository
) {

    private fun createRequestParamsList(projectId: Int): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PROJECT_ID] = projectId
        return request
    }

    suspend fun getKycInformation(projectId: Int): AffiliateKycDetailsData {
        return repository.getGQLData(
            GQL_Affiliate_Kyc,
            AffiliateKycDetailsData::class.java,
            createRequestParamsList(projectId)
        )
    }

    companion object {
        private const val PROJECT_ID = "projectId"
    }

}