package com.tokopedia.home.beranda.data.query

object PlayLiveDynamicChannelQuery {

    private const val source = "\$source"
    private const val page = "\$page"
    private const val limit = "\$limit"
    private const val device = "\$device"
    fun getQuery() = """
        query play($source: String!,$page:Int!,$limit:Int!,$device:String!){
          playGetLiveDynamicChannels(req:{
            source: $source,
            current_page: $page,
            limit:$limit,
            device:$device
          }){
            header{
              process_time
            }
            data{
              channels{
                partner_type
                partner_id
                channel_id
                title
                description
                cover_url
                total_view_formatted
                moderator_id
                moderator_name
                moderator_thumb_url
                slug
                video_stream{
                  orientation
                  type
                  is_live
                  config{
                    youtube_id
                    livestream_id
                    stream_url
                    is_playback
                  }
                }
              }      
            }
          }
        }
    """.trimIndent()
}