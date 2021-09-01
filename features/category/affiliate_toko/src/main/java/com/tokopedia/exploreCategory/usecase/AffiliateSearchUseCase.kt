package com.tokopedia.exploreCategory.usecase

import com.tokopedia.exploreCategory.model.AffiliateSearchData
import com.tokopedia.exploreCategory.model.raw.GQL_Affiliate_Search
import com.tokopedia.exploreCategory.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateSearchUseCase @Inject constructor(
        private val repository: AffiliateRepository) {

    private fun createRequestParams(affiliateId: String, filter : ArrayList<String>): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_AFFILIATE_ID] = affiliateId
        request[PARAM_FILTER] = filter
        return request
    }

    suspend fun affiliateSearchWithLink(affiliateId: String, filter : ArrayList<String>): AffiliateSearchData {
//        return repository.getGQLData(
//                GQL_Affiliate_Search,
//                AffiliateSearchData::class.java,
//                createRequestParams(affiliateId,filter)
//        )
        return getDummyData()
    }

    fun getDummyData(): AffiliateSearchData {

        fun getAddtionalInfo(): ArrayList<AffiliateSearchData.Cards.Items.AdditionalInformation> {
            val additionalInformation = arrayListOf<AffiliateSearchData.Cards.Items.AdditionalInformation>()
            val addInfo1 = AffiliateSearchData.Cards.Items.AdditionalInformation("Komisi Rp5.400",
                    1, "#03AC0E")
            val addInfo2 = AffiliateSearchData.Cards.Items.AdditionalInformation("10%",
                    2, "#EF144A")
            val addInfo3 = AffiliateSearchData.Cards.Items.AdditionalInformation("Rp600.000",
                    3, "#31353B")
            val addInfo4 = AffiliateSearchData.Cards.Items.AdditionalInformation("Rp540.000",
                    4, "#31353B")
            additionalInformation.add(addInfo1)
            additionalInformation.add(addInfo2)
            additionalInformation.add(addInfo3)
            additionalInformation.add(addInfo4)
            return additionalInformation
        }

        fun getFooter(): ArrayList<AffiliateSearchData.Cards.Items.Footer> {
            val footers = arrayListOf<AffiliateSearchData.Cards.Items.Footer>()
            val footer1 = AffiliateSearchData.Cards.Items.Footer(1,"data:image/jpeg;base64,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", "Musix studio")
            val footer2 = AffiliateSearchData.Cards.Items.Footer(2,"star-icon.link", "4.5 | Terjual 8,8 rb")
            footers.add(footer1)
            footers.add(footer2)
            return footers
        }

        fun getItem(): AffiliateSearchData.Cards.Items {
            val imageUrl = "https://imagerouter.tokopedia.com/img/300/attachment/2019/9/13/43737554/43737554_b3d583de-47a7-45b0-8ca4-d1bd7719431e.jpg"
            val item = AffiliateSearchData.Cards.Items(
                    "OEM Speaker Harman Kardon Onyx Mini Black",
                    AffiliateSearchData.Cards.Items.Image(imageUrl, imageUrl, imageUrl, imageUrl),
                    getAddtionalInfo(),
                    AffiliateSearchData.Cards.Items.Commission("Rp5.400",
                            5400, "10%", 10),
                    getFooter(),
                    4.5,
                    AffiliateSearchData.Cards.Items.Status(true)
            )
            return item
        }

        val items = arrayListOf<AffiliateSearchData.Cards.Items>()
        items.add(getItem())
        val card = AffiliateSearchData.Cards("1", false, "Produk ditemukan", items)
        return AffiliateSearchData(true, card)
    }



    companion object {
        private const val PARAM_AFFILIATE_ID = "affiliateID"
        private const val PARAM_FILTER = "filter"
    }
}