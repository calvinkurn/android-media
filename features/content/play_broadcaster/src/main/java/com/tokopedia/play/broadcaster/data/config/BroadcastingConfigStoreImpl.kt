package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUiModel
import javax.inject.Inject

class BroadcastingConfigStoreImpl @Inject constructor() : BroadcastingConfigStore {

    private var mConfig: BroadcastingConfigUiModel = BroadcastingConfigUiModel()

    override fun saveBroadcastingConfig(config: BroadcastingConfigUiModel) {
        mConfig = config
    }

    override fun getBroadcastingConfig(): BroadcastingConfigUiModel {
        return mConfig
    }

}
