package com.tokopedia.home.beranda.data.query

object HomeQuery{
    fun getQuery() = """
        {
        status
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
              campaignCode
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
              bu_identifier
            }
          }
          dynamicHomeChannel {
            channels {
              id
              group_id
              galaxy_attribution
              persona
              brand_id
              category_persona
              name
              layout
              type
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
                back_color
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
                shop{
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