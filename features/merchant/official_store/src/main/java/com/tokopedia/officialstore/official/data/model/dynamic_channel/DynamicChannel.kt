package com.tokopedia.officialstore.official.data.model.dynamic_channel

data class DynamicChannel(val channels: MutableList<Channel> = mutableListOf()) {
    data class Response(val dynamicHomeChannel: DynamicChannel = DynamicChannel())
}
