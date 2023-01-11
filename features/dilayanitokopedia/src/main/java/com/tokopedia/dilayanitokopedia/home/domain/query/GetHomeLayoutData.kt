package com.tokopedia.dilayanitokopedia.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetHomeLayoutData : GqlQueryInterface {

    private const val OPERATION_NAME = "getHomeChannelV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
            query $OPERATION_NAME(
                ${'$'}page: String, 
                ${'$'}param: String,
                ${'$'}groupIDs: String, 
                ${'$'}channelIDs: String, 
                ${'$'}location: String) {
                getHomeChannelV2(
                    page: ${'$'}page, 
                    param: ${'$'}param, 
                    groupIDs: ${'$'}groupIDs, 
                    channelIDs: ${'$'}channelIDs, 
                    location: ${'$'}location) {
                    channels {
                        id
                        name
                        type
                        token
                        layout
                        persona
                        brandID
                        groupID
                        pageName
                        persoType
                        categoryID
                        campaignID
                        widgetParam
                        dividerType
                        campaignCode
                        campaignType
                        showPromoBadge
                        pgCampaignType
                        hasCloseButton
                        contextualInfo
                        categoryPersona
                        galaxyAttribution
                        isAutoRefreshAfterExpired
                        grids {
                        id
                        url
                        name
                        price
                        label
                        stock
                        param
                        rating
                        applink
                        discount
                        imageUrl
                        cashback
                        isTopads
                        minOrder
                        maxOrder
                        clusterID
                        backColor
                        textColor
                        impression
                        attribution
                        warehouseID
                        countReview
                        expiredTime
                        slashedPrice
                        isOutOfStock
                        hasBuyButton
                        campaignCode
                        ratingAverage
                        labelTextColor
                        soldPercentage
                        productClickUrl
                        productImageUrl
                        parentProductID
                        discountPercentage
                        recommendationType
                        categoryBreadcrumbs
                        productViewCountFormatted
                        shop {
                          id
                          url
                          city
                          name
                          domain
                          applink
                          imageUrl
                          reputation
                        }
                        badges {
                          title
                          imageUrl
                        }
                        benefit {
                          type
                          value
                        }
                        freeOngkir {
                          isActive
                          imageUrl
                        }
                        labelGroup {
                          url
                          type
                          title
                          position
                        }
                        labelGroupVariant {
                          type
                          title
                          hexColor
                          typeVariant
                        }
                        }
                        header {
                        id
                        url
                        name
                        applink
                        subtitle
                        boxColor
                        backColor
                        backImage
                        textColor
                        timerColor
                        serverTime
                        expiredTime
                        desktopName
                        timerVariant
                        }
                        banner {
                        id
                        url
                        title
                        applink
                        imageUrl
                        textColor
                        backColor
                        description
                        attribution
                        gradientColor
                        cta {
                          type
                          mode
                          text
                          couponCode
                        }
                        }
                        viewAllCard {
                        id
                        title
                        imageUrl
                        contentType
                        description
                        gradientColor
                        }
                    }
                }
        }
        """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
