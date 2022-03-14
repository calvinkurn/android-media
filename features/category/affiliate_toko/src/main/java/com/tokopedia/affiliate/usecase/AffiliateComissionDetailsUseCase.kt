package com.tokopedia.affiliate.usecase

import com.google.gson.Gson
import com.tokopedia.affiliate.model.raw.GQL_Affiliate_Commission
import com.tokopedia.affiliate.model.response.AffiliateCommissionDetailsData
import com.tokopedia.affiliate.model.response.AffiliateTrafficCommissionCardDetails
import com.tokopedia.affiliate.repository.AffiliateRepository
import javax.inject.Inject

class AffiliateCommissionDetailsUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParams(transactionID: String): HashMap<String, Any> {
        val request = HashMap<String, Any>()
        request[PARAM_TRANSACTION_ID] = transactionID
        return request
    }

    suspend fun affiliateCommissionDetails(transactionID: String): AffiliateCommissionDetailsData {
        return repository.getGQLData(
            GQL_Affiliate_Commission,
            AffiliateCommissionDetailsData::class.java,
            createRequestParams(transactionID)
        )

    }

    fun affiliateTrafficCardDetails(transactionDate: String?, lastItem: String?, type: String?): AffiliateTrafficCommissionCardDetails? {
        return Gson().fromJson("{\n" +
                "\t\"getAffiliateTrafficCommissionDetailCards\": {\n" +
                "\t\t\"Data\": {\n" +
                "\t\t\t\"LastID\": \"129312\",\n" +
                "\t\t\t\"HasNext\": true,\n" +
                "\t\t\t\"Status\": 1,\n" +
                "\t\t\t\"Error\": {\n" +
                "\t\t\t\t\"ErrorType\": 0,\n" +
                "\t\t\t\t\"Message\": \"\",\n" +
                "\t\t\t\t\"CtaText\": \"\",\n" +
                "\t\t\t\t\"CtaLink\": {\n" +
                "\t\t\t\t\t\"DesktopURL\": \"\",\n" +
                "\t\t\t\t\t\"MobileURL\": \"\",\n" +
                "\t\t\t\t\t\"AndroidURL\": \"\",\n" +
                "\t\t\t\t\t\"IosURL\": \"\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t\"TrafficCommissionCardDetail\": [{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"CardTitle\": \"Things you dream at night\",\n" +
                "\t\t\t\t\t\"CardDescription\": \"500 Total Kunjungan\",\n" +
                "\t\t\t\t\t\"Image\": {\n" +
                "\t\t\t\t\t\t\"DesktopURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"MobileURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"AndroidURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\",\n" +
                "\t\t\t\t\t\t\"IosURL\": \"https://images.tokopedia.net/img/cache/900/product-1/2018/8/6/14481426/14481426_78ac25e0-ae52-4095-92e6-c30026c378fe_386_500.jpg\"\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t]\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}",AffiliateTrafficCommissionCardDetails::class.java)
    }


    companion object {
        private const val PARAM_TRANSACTION_ID = "transactionID"
    }
}