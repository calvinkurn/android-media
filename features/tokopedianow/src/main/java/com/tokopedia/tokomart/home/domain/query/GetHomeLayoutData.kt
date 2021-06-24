package com.tokopedia.tokomart.home.domain.query

internal object GetHomeLayoutData {

    val QUERY = """
       query getDynamicHomeChannel(${'$'}channelId: String) {
         dynamicHomeChannel {
           channels(type:"tokonow", channelIDs:${'$'}channelId       ) {
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
               freeOngkir {
                 isActive
                 imageUrl
               }
               productViewCountFormatted
               isOutOfStock
               warehouseID
               minOrder
               shop {
                 shopID
               }
               labelGroup {
                 title
                 position
                 type
               }
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
           }
         }
       }
    """.trimIndent()
}