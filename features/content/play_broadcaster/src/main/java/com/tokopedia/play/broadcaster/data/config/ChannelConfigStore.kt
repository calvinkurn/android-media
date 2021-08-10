package com.tokopedia.play.broadcaster.data.config

/**
 * Created by jegul on 03/07/20
 */
interface ChannelConfigStore {

    fun setChannelId(id: String)

    fun getChannelId(): String

    fun setIngestUrl(ingestUrl: String)

    fun getIngestUrl(): String

    fun setMaxDurationDesc(desc: String)

    fun getMaxDurationDesc(): String
}