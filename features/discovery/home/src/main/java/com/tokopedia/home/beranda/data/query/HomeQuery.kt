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

