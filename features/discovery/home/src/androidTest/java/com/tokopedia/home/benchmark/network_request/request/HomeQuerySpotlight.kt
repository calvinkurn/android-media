package com.tokopedia.home.benchmark.network_request.request

object HomeQuerySpotlight{
    fun getQuery() = """
        {
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
        }
    """.trimIndent()
}