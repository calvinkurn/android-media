package com.tokopedia.affiliate.usecase

import com.google.gson.Gson
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementData
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Announcement
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateAnnouncementUseCase @Inject constructor(
    private val repository : AffiliateRepository) {

    private fun createRequestParams(): HashMap<String, Any> {
        return HashMap()
    }
    suspend fun getAffiliateAnnouncement(): AffiliateAnnouncementDataV2 {
        return repository.getGQLData(
            GQL_Affiliate_Announcement,
            AffiliateAnnouncementDataV2::class.java,
            createRequestParams()
        )
    }
}