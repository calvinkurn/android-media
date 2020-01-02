package com.tokopedia.v2.home.data.query

object PlayCardQuery{
    fun getQuery() = """
        {
          playGetCardHome{
            header{
              process_time
            }
            data{
              card{
                card_id
                broadcaster_type
                broadcaster_id
                broadcaster_name
                broadcaster_image
                broadcaster_badge_type
                broadcaster_app_link
                broadcaster_web_link
                title
                description
                image_url
                app_link
                web_link
                is_show_live
                is_show_total_view
                is_lite
                og_title
                og_description
                og_url
                og_image
                start_date
                end_date
                created_date
                updated_date
                campaign_name
                total_view_formatted
                livestream_id
                channel_id
              }
            }
          }
        }
    """.trimIndent()
}