package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel

interface BroadcastingConfigStore {

    fun saveBroadcastingConfig(config: BroadcastingConfigUiModel)

    fun getBroadcastingConfig(): BroadcastingConfigUiModel

}
