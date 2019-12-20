package com.tokopedia.v2.home.data.query

object TokopointQuery {
    fun getQuery() = """
    {
        tokopointsDrawer{
          offFlag
          iconImageURL
          redirectURL
          redirectAppLink
          sectionContent{
              type
              textAttributes{
                  text
                  color
                  isBold
              }
              tagAttributes{
                  text
                  backgroundColor
              }
          }
        }
    }
    """.trimIndent()
}