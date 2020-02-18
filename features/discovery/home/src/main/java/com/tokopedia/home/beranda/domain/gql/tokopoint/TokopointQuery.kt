package com.tokopedia.home.beranda.domain.gql.tokopoint

object TokopointQuery{
    val query = """
        {
            tokopointsDrawer{
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