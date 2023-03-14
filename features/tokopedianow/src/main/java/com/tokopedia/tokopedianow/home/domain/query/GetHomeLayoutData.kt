package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetHomeLayoutData: GqlQueryInterface {

    private const val OPERATION_NAME = "getHomeChannelV2"

    override fun getOperationNameList(): List<String> {
        return listOf(OPERATION_NAME)
    }

    override fun getQuery(): String {
        return """
        query $OPERATION_NAME(
          ${'$'}token: String, 
          ${'$'}numOfChannel: Int, 
          ${'$'}location: String
        ) {
          $OPERATION_NAME(
              page:"tokonow", 
              token:${'$'}token, 
              numOfChannel:${'$'}numOfChannel, 
              location: ${'$'}location
            ) {
            channels {
              id
              groupID
              galaxyAttribution
              persona
              brandID
              categoryPersona
              name
              layout
              type
              campaignID
              showPromoBadge
              categoryID
              persoType
              pageName
              campaignCode
              hasCloseButton
              header {
                id
                name
                subtitle
                url
                applink
                serverTime
                expiredTime
                backColor
                backImage
                textColor
              }
              grids {
                id
                name
                url
                applink
                price
                slashedPrice
                discount
                imageUrl
                label
                labelTextColor
                soldPercentage
                attribution
                productClickUrl
                impression
                cashback
                isTopads
                ratingAverage
                categoryBreadcrumbs
                recommendationType
                productViewCountFormatted
                isOutOfStock
                warehouseID
                parentProductID
                minOrder
                maxOrder
                stock
                shop {
                  id
                }
                labelGroup {
                  title
                  position
                  type
                  url
                }
                param
              }
              banner {
                id
                title
                description
                url
                backColor
                cta {
                  type
                  mode
                  text
                  couponCode
                }
                applink
                textColor
                imageUrl
                attribution
                gradientColor
              }
              token
              widgetParam
            }
          }
        }
    """.trimIndent()
    }

    override fun getTopOperationName(): String {
        return OPERATION_NAME
    }
}
