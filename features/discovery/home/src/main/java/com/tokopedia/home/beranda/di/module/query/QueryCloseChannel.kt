package com.tokopedia.home.beranda.di.module.query

import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.home.beranda.di.module.query.QueryCloseChannel.CLOSE_CHANNEL_QUERY
import com.tokopedia.home.beranda.di.module.query.QueryCloseChannel.CLOSE_CHANNEL_QUERY_NAME

@GqlQuery(CLOSE_CHANNEL_QUERY_NAME, CLOSE_CHANNEL_QUERY)
internal object QueryCloseChannel {
    const val CLOSE_CHANNEL_QUERY_NAME = "CloseChannelQuery"
    const val CLOSE_CHANNEL_QUERY = "mutation closeChannel(\$channelID: Int!){\n" +
            "  close_channel(channelID: \$channelID){\n" +
            "    success\n" +
            "    message\n" +
            "  }\n" +
            "}"
}