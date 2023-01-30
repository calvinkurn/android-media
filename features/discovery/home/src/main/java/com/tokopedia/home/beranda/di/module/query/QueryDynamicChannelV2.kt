package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryDynamicChannelV2.DYNAMIC_CHANNEL_V2_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryDynamicChannelV2.DYNAMIC_CHANNEL_V2_QUERY_NAME

@GqlQuery(DYNAMIC_CHANNEL_V2_QUERY_NAME, DYNAMIC_CHANNEL_V2_QUERY)
internal object QueryDynamicChannelV2 {
    const val DYNAMIC_CHANNEL_V2_QUERY_NAME = "DynamicChannelQueryV2"
    const val DYNAMIC_CHANNEL_V2_QUERY = "query getDynamicChannelV2(\$groupIDs: String!, \$channelIDs: String!, \$param: String!, \$location: String){\n" +
        "  getHomeChannelV2(groupIDs: \$groupIDs, channelIDs: \$channelIDs, param: \$param, location: \$location) {\n" +
        "    channels {\n" +
        "      id\n" +
        "      name\n" +
        "      type\n" +
        "      token\n" +
        "      grids {\n" +
        "        id\n" +
        "        url\n" +
        "        name\n" +
        "        shop {\n" +
        "          id\n" +
        "          url\n" +
        "          city\n" +
        "          name\n" +
        "          domain\n" +
        "          applink\n" +
        "          imageUrl\n" +
        "          reputation\n" +
        "        }\n" +
        "        price\n" +
        "        label\n" +
        "        stock\n" +
        "        param\n" +
        "        rating\n" +
        "        badges {\n" +
        "          title\n" +
        "          imageUrl\n" +
        "        }\n" +
        "        applink\n" +
        "        benefit {\n" +
        "          type\n" +
        "          value\n" +
        "        }\n" +
        "        discount\n" +
        "        imageUrl\n" +
        "        cashback\n" +
        "        isTopads\n" +
        "        minOrder\n" +
        "        maxOrder\n" +
        "        clusterID\n" +
        "        backColor\n" +
        "        textColor\n" +
        "        impression\n" +
        "        freeOngkir {\n" +
        "          isActive\n" +
        "          imageUrl\n" +
        "        }\n" +
        "        labelGroup {\n" +
        "          url\n" +
        "          type\n" +
        "          title\n" +
        "          position\n" +
        "        }\n" +
        "        attribution\n" +
        "        warehouseID\n" +
        "        countReview\n" +
        "        expiredTime\n" +
        "        slashedPrice\n" +
        "        isOutOfStock\n" +
        "        hasBuyButton\n" +
        "        campaignCode\n" +
        "        ratingAverage\n" +
        "        labelTextColor\n" +
        "        soldPercentage\n" +
        "        productClickUrl\n" +
        "        productImageUrl\n" +
        "        parentProductID\n" +
        "        labelGroupVariant {\n" +
        "          type\n" +
        "          title\n" +
        "          hexColor\n" +
        "          typeVariant\n" +
        "        }\n" +
        "        discountPercentage\n" +
        "        recommendationType\n" +
        "        categoryBreadcrumbs\n" +
        "        productViewCountFormatted\n" +
        "      }\n" +
        "      layout\n" +
        "      header {\n" +
        "        id\n" +
        "        url\n" +
        "        name\n" +
        "        applink\n" +
        "        subtitle\n" +
        "        boxColor\n" +
        "        backColor\n" +
        "        backImage\n" +
        "        textColor\n" +
        "        timerColor\n" +
        "        serverTime\n" +
        "        expiredTime\n" +
        "        desktopName\n" +
        "        timerVariant\n" +
        "      }\n" +
        "      banner {\n" +
        "        id\n" +
        "        cta {\n" +
        "          type\n" +
        "          mode\n" +
        "          text\n" +
        "          couponCode\n" +
        "        }\n" +
        "        url\n" +
        "        title\n" +
        "        applink\n" +
        "        imageUrl\n" +
        "        textColor\n" +
        "        backColor\n" +
        "        description\n" +
        "        attribution\n" +
        "        gradientColor\n" +
        "      }\n" +
        "      persona\n" +
        "      brandID\n" +
        "      groupID\n" +
        "      pageName\n" +
        "      persoType\n" +
        "      categoryID\n" +
        "      campaignID\n" +
        "      widgetParam\n" +
        "      dividerType\n" +
        "      viewAllCard {\n" +
        "        id\n" +
        "        title\n" +
        "        imageUrl\n" +
        "        contentType\n" +
        "        description\n" +
        "        gradientColor\n" +
        "      }\n" +
        "      campaignCode\n" +
        "      campaignType\n" +
        "      showPromoBadge\n" +
        "      pgCampaignType\n" +
        "      hasCloseButton\n" +
        "      contextualInfo\n" +
        "      categoryPersona\n" +
        "      galaxyAttribution\n" +
        "      isAutoRefreshAfterExpired\n" +
        "    }\n" +
        "  }\n" +
        "}"
}
