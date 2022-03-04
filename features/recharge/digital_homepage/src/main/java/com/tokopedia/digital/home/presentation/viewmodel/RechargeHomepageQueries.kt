package com.tokopedia.digital.home.presentation.viewmodel

object RechargeHomepageQueries {
    val SKELETON_QUERY by lazy {
        val platformID = "\$platformID"
        val enablePersonalize = "\$enablePersonalize"

        """
                query rechargeGetDynamicPageSkeleton($platformID: Int!, $enablePersonalize: Boolean) {
                  rechargeGetDynamicPageSkeleton(platformID: $platformID, enablePersonalize: $enablePersonalize){
                    search_bar_placeholder
                    search_bar_app_link
                    search_bar_web_link
                    search_bar_screen_name
                    search_bar_redirection
                    sections {
                      id
                      template
                    }
                  }
                }
            """.trimIndent()
    }

    val SECTION_QUERY by lazy {
        val platformID = "\$platformID"
        val sectionIDs = "\$sectionIDs"
        val enablePersonalize = "\$enablePersonalize"

        """
                query rechargeGetDynamicPage($platformID: Int!, $sectionIDs: [Int], $enablePersonalize: Boolean) {
                  rechargeGetDynamicPage(platformID: $platformID, sectionIDs: $sectionIDs, enablePersonalize: $enablePersonalize){
                    sections {
                      id
                      object_id
                      title
                      sub_title
                      template
                      app_link
                      text_link
                      media_url
                      label_1
                      label_2
                      tracking {
                        action
                        data
                      }
                      items {
                        id
                        object_id
                        title
                        sub_title
                        tracking {
                          action
                          data
                        }
                        content
                        app_link
                        web_link
                        text_link
                        media_url
                        template
                        button_type
                        label_1
                        label_2
                        label_3
                        server_date
                        due_date
                        attributes {
                            title_color
                            sub_title_color
                            media_url_title
                            media_url_type
                            icon_url
                            sold_value
                            sold_percentage_value
                            sold_percentage_label
                            sold_percentage_label_color
                            show_sold_percentage
                            campaign_label_text
                            campaign_label_text_color
                            campaign_label_background_url
                            rating_type
                            rating
                            review
                            special_info_text
                            special_info_color
                            special_discount
                            cashback
                            price_prefix
                            price_suffix
                        }
                      }
                    }
                  }
                }
            """.trimIndent()
    }

    val ACTION_QUERY by lazy {
        val sectionID = "\$sectionID"
        val action = "\$action"

        """
                mutation rechargePostDynamicPageAction($sectionID: Int!, $action: String!) {
                  rechargePostDynamicPageAction(sectionID: $sectionID, action: $action) {
                    message
                  }
                }
            """.trimIndent()
    }

    val TICKER_QUERY by lazy {
        val categoryIDs = "\$categoryIDs"
        val deviceID = "\$deviceID"

        """query rechargeTicker($categoryIDs: [Int], $deviceID: Int!) {
                rechargeTicker(categoryIDs:$categoryIDs, deviceID: $deviceID) {
                     ID
                     Name
                     Content
                     Type
                     Environment
                     ActionText
                     ActionLink
                     StartDate
                     ExpireDate
                     Status
              }
           }
            """.trimIndent()
    }
}