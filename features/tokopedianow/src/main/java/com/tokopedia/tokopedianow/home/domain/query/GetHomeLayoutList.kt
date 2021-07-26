package com.tokopedia.tokopedianow.home.domain.query

internal object GetHomeLayoutList {

    val QUERY = """
        {
          status
          dynamicHomeChannel {
            channels(type: "tokonow") {
              id
              layout
              header {
                 name 
              }
            }
          }
        }
    """.trimIndent()
}