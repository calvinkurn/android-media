package com.tokopedia.tokopedianow.home.domain.query

import com.tokopedia.gql_query_annotation.GqlQueryInterface

internal object GetHomeLayoutData: GqlQueryInterface {

    private const val OPERATION_NAME = "dynamicHomeChannel"

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
          $OPERATION_NAME {
            channels(
              type:"tokonow", 
              token:${'$'}token, 
              numOfChannel:${'$'}numOfChannel, 
              location: ${'$'}location
            ) {
              id
              group_id
              galaxy_attribution
              persona
              brand_id
              category_persona
              name
              layout
              type
              campaignID
              showPromoBadge
              categoryID
              perso_type
              pageName
              campaignCode
              has_close_button
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
              hero {
                id
                name
                url
                applink
                imageUrl
                attribution
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
                shop {
                  shopID
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
                back_color
                cta {
                  type
                  mode
                  text
                  coupon_code
                }
                applink
                text_color
                image_url
                attribution
                gradient_color
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
