package com.tokopedia.home.benchmark.network_request.request

object HomeQueryHomeFlag{
    fun getQuery() = """
        {
          homeFlag{
            flags(name: "has_recom_nav_button,dynamic_icon_wrap,has_tokopoints"){
              name
              is_active
            }
          }
        }
    """.trimIndent()
}