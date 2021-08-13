package com.tokopedia.digital.home.util

object DigitalHomepageGqlQuery {

    val digitalHomeSection = """
        query rechargeSubHomePageSection(${'$'}sectionType: SubHomePageSection!) {
            rechargeSubHomePageSection(sectionType:${'$'}sectionType){
            slug_name
            section {
              title
              items {
                id
                title
                media_url
                navigate_url
                app_link
                html_content
                status
              }
            }
          }
        }
    """.trimIndent()

    val digitalHomeCategory = """
        {
          rechargeCatalogMenu(platformID:31){
            name
            label
            app_link
            sub_menu{
              id
              name
              label
              app_link
              icon
              category_ids
            }
          }
        }
    """.trimIndent()

    val digitalHomeBanner = """
        {
          rechargeBanner(platformID: 31) {
            id
            img_url
            title
            app_link
            filename
          }
        }
    """.trimIndent()
}