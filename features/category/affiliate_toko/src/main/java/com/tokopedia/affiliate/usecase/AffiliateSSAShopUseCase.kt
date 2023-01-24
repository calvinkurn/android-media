package com.tokopedia.affiliate.usecase

import com.tokopedia.affiliate.model.response.AffiliateSSAShopListResponse
import com.tokopedia.affiliate.repository.AffiliateRepository
import com.tokopedia.affiliate.usecase.AffiliateSSAShopUseCase.Companion.GET_SSA_SHOP_LIST
import com.tokopedia.gql_query_annotation.GqlQuery
import javax.inject.Inject

@GqlQuery("GetSSAShopList", GET_SSA_SHOP_LIST)
internal class AffiliateSSAShopUseCase @Inject constructor(
    private val repository: AffiliateRepository
) {

    private fun createRequestParamsList(page: Int, limit: Int): HashMap<String, Any> {
        return hashMapOf(
            KEY_PAGE to page,
            KEY_LIMIT to limit,
            KEY_SITE_ID to SITE_ID,
            KEY_VERTICAL_ID to VERTICAL_ID
        )
    }

    suspend fun getSSAShopList(page: Int, limit: Int): AffiliateSSAShopListResponse {
        return repository.getGQLData(
            GET_SSA_SHOP_LIST,
            AffiliateSSAShopListResponse::class.java,
            createRequestParamsList(page, limit)
        )
    }

    companion object {
        private const val KEY_PAGE = "page"
        private const val KEY_LIMIT = "limit"
        private const val KEY_SITE_ID = "siteId"
        private const val KEY_VERTICAL_ID = "verticalId"
        private const val SITE_ID = 1
        private const val VERTICAL_ID = 1
        const val GET_SSA_SHOP_LIST =
            "query getSSAShopList(${'$'}page: Int, ${'$'}limit: Int, ${'$'}siteId: Int, ${'$'}verticalId: Int) {\n" +
                "  getSSAShopList(page: ${'$'}page, limit: ${'$'}limit, siteId: ${'$'}siteId, verticalId: ${'$'}verticalId) { \n" +
                "    Data {\n" +
                "      Status\n" +
                "      Error {\n" +
                "        Title\n" +
                "        Message\n" +
                "        ErrorCode\n" +
                "      }\n" +
                "      PageInfo {\n" +
                "        hasNext\n" +
                "        hasPrev\n" +
                "        currentPage\n" +
                "        totalPage\n" +
                "        totalCount\n" +
                "      }\n" +
                "      ShopData {\n" +
                "        SSAShopDetail {\n" +
                "          ShopId\n" +
                "          ShopName\n" +
                "          ShopType\n" +
                "          ShopLocation\n" +
                "          SSAStatus\n" +
                "          QuantitySold\n" +
                "          Rating\n" +
                "          Message\n" +
                "          SSAMessage\n" +
                "          BadgeURL\n" +
                "          Label {\n" +
                "            LabelType\n" +
                "            LabelText \n" +
                "          }\n" +
                "          URLDetail {\n" +
                "            DesktopURL\n" +
                "            MobileURL\n" +
                "            AndroidURL\n" +
                "            IosURL\n" +
                "          }\n" +
                "          ImageURL {\n" +
                "            DesktopURL\n" +
                "            MobileURL\n" +
                "            AndroidURL\n" +
                "            IosURL\n" +
                "          }\n" +
                "        }\n" +
                "        CommissionDetail {\n" +
                "          CumulativePercentage\n" +
                "          CumulativePercentageFormatted\n" +
                "          SellerPercentage\n" +
                "          SellerPercentageformatted\n" +
                "          ExpiredDate\n" +
                "          ExpiredDate_formatted\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}\n"
    }
}
