package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 03/07/20
 */
class ChannelConfigStoreImpl @Inject constructor(

): ChannelConfigStore {

    private lateinit var mChannelId: String

    override fun setChannelId(id: String) {
        mChannelId = id
    }

    override fun getChannelId(): String {
        return if (::mChannelId.isInitialized) mChannelId
        else throw IllegalStateException("Channel ID has not been set")
    }
}