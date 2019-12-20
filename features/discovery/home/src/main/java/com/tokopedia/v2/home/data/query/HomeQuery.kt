package com.tokopedia.v2.home.data.query

/**
 * Created by Lukas on 16/11/19
 */

object HomeQuery{
    fun getQuery() = """
        {
          ticker {
            meta {
              total_data
            }
            tickers
            {
              id
              title
              message
              color
              layout
            }
          }
          slides(device: 32) {
            meta { total_data }
            slides {
              id
              title
              image_url
              redirect_url
              applink
              topads_view_url
              promo_code
              message
              creative_name
              start_time
              expire_time
              slide_index
              type
            }
          }
          dynamicHomeIcon {
            useCaseIcon {
              id
              name
              url
              imageUrl
              applinks
            }
            dynamicIcon {
              id
              name
              url
              imageUrl
              applinks
            }
          }
          dynamicHomeChannel {
            channels {
              id
              name
              layout
              type
              header {
                id
                name
                url
                applink
                serverTime
                expiredTime
                backColor
                backImage
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
                freeOngkir {
                  isActive
                  imageUrl
                }
              }
              banner {
                id
                title
                description
                url
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

              }
            }
          }
          spotlight {
            spotlights {
              id
              title
              description
              background_image_url
              tag_name
              tag_name_hexcolor
              tag_hexcolor
              cta_text
              cta_text_hexcolor
              url
              applink
            }
          }
          homeFlag{
            flags(name: "has_recom_nav_button,dynamic_icon_wrap,has_tokopoints"){
              name
              is_active
            }
          }
        }
    """.trimIndent()
}