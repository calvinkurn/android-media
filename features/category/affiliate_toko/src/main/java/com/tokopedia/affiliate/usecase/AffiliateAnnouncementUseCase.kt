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
        return Gson().fromJson("{\n" +
                "    \"getAffiliateAnnouncementV2\": {\n" +
                "      \"Data\": {\n" +
                "        \"Status\": 1,\n" +
                "        \"TickerType\": \"warning\",\n" +
                "        \"TickerData\": [\n" +
                "          {\n" +
                "            \"AnnouncementTitle\": \"Saldo Affiliate kamu ditahan sementara\",\n" +
                "            \"AnnouncementDescription\": \"Kamu juga tidak bisa tarik saldo karena akunmu terdeteksi bermasalah.\",\n" +
                "            \"CtaText\": \"Pelajari Selengkapnya\",\n" +
                "            \"CtaLink\": {\n" +
                "              \"IosURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\",\n" +
                "              \"AndroidURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\",\n" +
                "              \"DesktopURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\",\n" +
                "              \"MobileURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"AnnouncementTitle\": \"Segera cek e-mail kamu, ya\",\n" +
                "            \"AnnouncementDescription\": \"Kami telah mengirim e-mail untuk bantu perbaiki akun Tokopedia Affiliate kamu.\",\n" +
                "            \"CtaText\": \"Cek E-mail\",\n" +
                "            \"CtaLink\": {\n" +
                "              \"IosURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\",\n" +
                "              \"AndroidURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\",\n" +
                "              \"DesktopURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\",\n" +
                "              \"MobileURL\": \"https://www.tokopedia.com/help/article/syarat-ketentuan-tokopedia-affiliate\"\n" +
                "            }\n" +
                "          }\n" +
                "        ],\n" +
                "        \"Error\": {\n" +
                "          \"Message\": \"\",\n" +
                "          \"CtaText\": \"\",\n" +
                "          \"CtaLink\": {\n" +
                "            \"IosURL\": \"\",\n" +
                "            \"AndroidURL\": \"\",\n" +
                "            \"DesktopURL\": \"\",\n" +
                "            \"MobileURL\": \"\"\n" +
                "          }\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }",AffiliateAnnouncementDataV2::class.java)
//        return repository.getGQLData(
//            GQL_Affiliate_Announcement,
//            AffiliateAnnouncementData::class.java,
//            createRequestParams()
//        )
    }
}