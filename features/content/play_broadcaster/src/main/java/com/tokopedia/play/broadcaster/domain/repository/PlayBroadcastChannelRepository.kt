package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.play.broadcaster.ui.model.ConfigurationUiModel

/**
 * Created by jegul on 01/10/21
 */
interface PlayBroadcastChannelRepository {

    suspend fun getChannelConfiguration(): ConfigurationUiModel
}