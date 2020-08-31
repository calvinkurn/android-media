package com.tokopedia.home.beranda.data.query

object HomeQuery{
    fun getQuery() = """
        query homeData
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
              ticker_type
              title
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
              image_url
              redirect_url
              applink
              topads_view_url
              promo_code
              creative_name
              type
              category_id
              campaignCode
            }
          }
          dynamicHomeIcon {
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
                has_buy_button
                rating
                count_review
                benefit {
                    type
                    value
                }
                textColor
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
          homeFlag{
                event_time
                server_time
                flags(name: "has_recom_nav_button,dynamic_icon_wrap,has_tokopoints,is_autorefresh"){
                    name
                    is_active
                }
            }
        }
    """.trimIndent()
}

