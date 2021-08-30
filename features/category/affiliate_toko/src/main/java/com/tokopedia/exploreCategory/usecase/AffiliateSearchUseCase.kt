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
            val footer1 = AffiliateSearchData.Cards.Items.Footer("os-icon.link", "Musix studio")
            val footer2 = AffiliateSearchData.Cards.Items.Footer("star-icon.link", "4.5 | Terjual 8,8 rb")
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