package com.tokopedia.officialstore.official.di.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.officialstore.official.di.query.QueryOSFeaturedShop.OS_FEATURED_SHOP_QUERY
import com.tokopedia.officialstore.official.di.query.QueryOSFeaturedShop.OS_FEATURED_SHOP_QUERY_NAME

/**
 * Created by dhaba
 */
@GqlQuery(OS_FEATURED_SHOP_QUERY_NAME, OS_FEATURED_SHOP_QUERY)
internal object QueryOSFeaturedShop {
    const val OS_FEATURED_SHOP_QUERY_NAME = "OSFeaturedShopQuery"
    const val OS_FEATURED_SHOP_QUERY =
        "query OfficialStoreFeaturedShop(\$categoryAliasID: Int) {\n" +
                "   OfficialStoreFeaturedShop(categoryAliasID: \$categoryAliasID, device: 4, size: 4){\n" +
                "      totalShops\n" +
                "      header{\n" +
                "        ctaText\n" +
                "        title\n" +
                "        link\n" +
                "      }\n" +
                "      shops{\n" +
                "        id\n" +
                "        name\n" +
                "        url\n" +
                "        logoUrl\n" +
                "        imageUrl\n" +
                "        additionalInformation\n" +
                "        featuredBrandId\n" +
                "        campaignCode\n" +
                "      }\n" +
                "    }\n" +
                "}"

}