package com.tokopedia.home.beranda.data.query

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
              galaxy_attribution
              persona
              brand_id
              category_persona
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
              galaxy_attribution
              persona
              brand_id
              category_persona
              name
              url
              imageUrl
              applinks
            }
          }
          dynamicHomeChannel {
            channels {
              id
              galaxy_attribution
              persona
              brand_id
              category_persona
              name
              layout
              type
              showPromoBadge
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
                galaxy_attribution
                persona
                brand_id
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
              galaxy_attribution
              persona
              brand_id
              category_persona
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