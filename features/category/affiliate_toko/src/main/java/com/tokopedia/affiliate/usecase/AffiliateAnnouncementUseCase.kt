package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.AffiliateAnnouncementData
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateAnnouncementUseCase @Inject constructor(
    private val repository : AffiliateRepository) {
    companion object {
        private const val PARAM_USER_ID = "userID"
    }
    private fun createRequestParams(userID: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_USER_ID] = userID
        return request
    }
    suspend fun getAffiliateAnnouncement(userID: String): AffiliateAnnouncementData {
//        return repository.getGQLData(
//            GQL_Affiliate_Announcement,
//            AffiliateAnnouncementData::class.java,
//            createRequestParams(userID)
//        )
        return mockData()
    }

    private fun mockData(): AffiliateAnnouncementData {
        val mockData = AffiliateAnnouncementData.Data(
            1, "cca", "Tunggu belum dapet wordingnya",
            "Tunggu belum dapet wordingnya", "Klik Disini",
            AffiliateAnnouncementData.Data.URL("", "", "", ""),
            AffiliateAnnouncementData.Data.Error(
                "",
                0,
                "",
                AffiliateAnnouncementData.Data.URL("", "", "", "")
            )
        )
        return AffiliateAnnouncementData(mockData)
    }
}