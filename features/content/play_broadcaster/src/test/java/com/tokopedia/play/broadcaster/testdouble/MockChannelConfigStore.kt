package com.tokopedia.play.broadcaster.testdouble

import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore

/**
 * Created by jegul on 25/09/20
 */
class MockChannelConfigStore(
        private val channelId: String
) : ChannelConfigStore {

    override fun setChannelId(id: String) {

    }

    override fun getChannelId(): String {
        return channelId
    }

    override fun setIngestUrl(ingestUrl: String) {

    }

    override fun getIngestUrl(): String {
        return ""
    }

    override fun setMaxDurationDesc(desc: String) {

    }

    override fun getMaxDurationDesc(): String {
        return ""
    }
}