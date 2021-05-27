package com.tokopedia.tokomart.home.domain.query

internal object GetHomeLayoutList {

    val QUERY = """
        {
          status
          dynamicHomeChannel {
            channels(type: "tokonow") {
              id
              layout
            }
          }
        }
    """.trimIndent()
}