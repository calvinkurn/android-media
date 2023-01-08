package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.PAGE_ANNOUNCEMENT_ALL
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Announcement
import com.tokopedia.affiliate.model.response.AffiliateAnnouncementDataV2
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateAnnouncementUseCase @Inject constructor(
    private val repository : AffiliateRepository) {

    companion object{
        private const val PARAM_PAGE = "Page"
    }

    private fun createRequestParams(page:Int): Map<String, Int> {
        return mapOf(PARAM_PAGE to page)
    }
    suspend fun getAffiliateAnnouncement(page:Int = PAGE_ANNOUNCEMENT_ALL): AffiliateAnnouncementDataV2 {
        return repository.getGQLData(
            GQL_Affiliate_Announcement,
            AffiliateAnnouncementDataV2::class.java,
            createRequestParams(page)
        )
    }
}