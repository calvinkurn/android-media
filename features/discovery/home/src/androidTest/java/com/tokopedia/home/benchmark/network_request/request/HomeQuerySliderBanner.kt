package com.tokopedia.home.benchmark.network_request.request

object HomeQuerySliderBanner{
    fun getQuery() = """
        {
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
        }
    """.trimIndent()
}