package com.tokopedia.play.domain.repository

import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.uimodel.recom.PlayChannelStatus

interface PlayViewerChannelRepository {

    fun getChannelData(channelId: String): PlayChannelData?

    fun setChannelData(data: PlayChannelData)

    suspend fun getChannelStatus(channelId: String): PlayChannelStatus
}