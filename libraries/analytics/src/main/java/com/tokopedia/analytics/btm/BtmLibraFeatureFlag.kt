package com.tokopedia.analytics.btm

import com.bytedance.dataplatform.remote_config.Experiments

/**
 * Created by @ilhamsuaib on 5/21/24.
 */

object BtmLibraFeatureFlag {

    fun isBtmEnabled(): Boolean {
        return Experiments.getAndroidEnableBtm(false)
    }
}
