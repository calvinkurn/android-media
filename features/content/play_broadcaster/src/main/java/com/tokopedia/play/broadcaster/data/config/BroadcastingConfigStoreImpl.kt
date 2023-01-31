package com.tokopedia.play.broadcaster.data.config

import com.tokopedia.play.broadcaster.ui.model.config.BroadcastingConfigUIModel
import javax.inject.Inject

class BroadcastingConfigStoreImpl @Inject constructor() : BroadcastingConfigStore {

    private var mConfig: BroadcastingConfigUIModel = BroadcastingConfigUIModel()

    override fun saveBroadcastingConfig(config: BroadcastingConfigUIModel) {
        mConfig = config
    }

    override fun getBroadcastingConfig(): BroadcastingConfigUIModel {
        return mConfig
    }

}
