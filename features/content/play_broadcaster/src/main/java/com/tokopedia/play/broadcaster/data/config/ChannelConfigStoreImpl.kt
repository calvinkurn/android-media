package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 03/07/20
 */
class ChannelConfigStoreImpl @Inject constructor(): ChannelConfigStore {

    private lateinit var mChannelId: String
    private lateinit var mIngestUrl: String
    private lateinit var mMaxDurationDesc: String

    override fun setChannelId(id: String) {
        mChannelId = id
    }

    override fun getChannelId(): String {
        return if (::mChannelId.isInitialized) mChannelId
        else throw IllegalStateException("Channel ID has not been set")
    }

    override fun setIngestUrl(ingestUrl: String) {
        mIngestUrl = ingestUrl
    }

    override fun getIngestUrl(): String {
        return if (::mIngestUrl.isInitialized) mIngestUrl
        else throw IllegalStateException("ingest URL has not been set")
    }

    override fun setMaxDurationDesc(desc: String) {
        mMaxDurationDesc = desc
    }

    override fun getMaxDurationDesc(): String {
        return if (::mMaxDurationDesc.isInitialized) mMaxDurationDesc
        else throw IllegalStateException("Duration desc has not been set")
    }
}