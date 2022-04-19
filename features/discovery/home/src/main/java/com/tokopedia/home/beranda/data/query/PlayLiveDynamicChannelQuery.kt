package com.tokopedia.home.beranda.data.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.data.query.PlayLiveDynamicChannelQuery.PLAY_LIVE_DYNAMIC_QUERY
import com.tokopedia.home.beranda.data.query.PlayLiveDynamicChannelQuery.PLAY_LIVE_DYNAMIC_QUERY_NAME

@GqlQuery(PLAY_LIVE_DYNAMIC_QUERY_NAME, PLAY_LIVE_DYNAMIC_QUERY)
internal object PlayLiveDynamicChannelQuery {


    private const val source = "\$source"
    private const val page = "\$page"
    private const val limit = "\$limit"
    private const val device = "\$device"
    const val PLAY_LIVE_DYNAMIC_QUERY_NAME = "PlayLiveDynamicQuery"
    const val PLAY_LIVE_DYNAMIC_QUERY = "query play($source: String!,$page:Int!,$limit:Int!,$device:String!){\n" +
            "          playGetLiveDynamicChannels(req:{\n" +
            "            source: $source,\n" +
            "            current_page: $page,\n" +
            "            limit:$limit,\n" +
            "            device:$device\n" +
            "          }){\n" +
            "            header{\n" +
            "              process_time\n" +
            "            }\n" +
            "            data{\n" +
            "              channels{\n" +
            "                partner_type\n" +
            "                partner_id\n" +
            "                channel_id\n" +
            "                title\n" +
            "                description\n" +
            "                cover_url\n" +
            "                is_show_total_view\n" +
            "                total_view_formatted\n" +
            "                moderator_id\n" +
            "                moderator_name\n" +
            "                moderator_thumb_url\n" +
            "                slug\n" +
            "                video_stream{\n" +
            "                  orientation\n" +
            "                  type\n" +
            "                  is_live\n" +
            "                  config{\n" +
            "                    youtube_id\n" +
            "                    livestream_id\n" +
            "                    stream_url\n" +
            "                    is_playback\n" +
            "                    is_auto_play\n" +
            "                  }\n" +
            "                }\n" +
            "              }      \n" +
            "            }\n" +
            "          }\n" +
            "        }"
}