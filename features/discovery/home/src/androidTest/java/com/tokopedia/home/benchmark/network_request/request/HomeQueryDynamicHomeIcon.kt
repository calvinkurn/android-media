package com.tokopedia.home.benchmark.network_request.request

object HomeQueryDynamicHomeIcon{
    fun getQuery() = """
        {
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
        }
    """.trimIndent()
}