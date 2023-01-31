package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel

interface BroadcastingConfigStore {

    fun saveBroadcastingConfig(config: BroadcastingConfigUIModel)

    fun getBroadcastingConfig():BroadcastingConfigUIModel

}
