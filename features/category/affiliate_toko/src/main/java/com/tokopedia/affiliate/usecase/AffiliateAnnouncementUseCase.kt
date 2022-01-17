package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateAnnouncementData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Announcement
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateAnnouncementUseCase @Inject constructor(
    private val repository : AffiliateRepository) {

    private fun createRequestParams(): HashMap<String, Any> {
        return HashMap()
    }
    suspend fun getAffiliateAnnouncement(): AffiliateAnnouncementData {
        return repository.getGQLData(
            GQL_Affiliate_Announcement,
            AffiliateAnnouncementData::class.java,
            createRequestParams()
        )
    }
}