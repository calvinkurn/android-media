package com.tokopedia.tokopedianow.home.domain.query

internal object GetHomeLayoutList {

    val QUERY = """
         query getDynamicHomeChannel(${'$'}type: String, ${'$'}location: String) {
          status
          dynamicHomeChannel {
            channels(type: ${'$'}type, location: ${'$'}location) {
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