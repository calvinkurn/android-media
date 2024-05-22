package com.tokopedia.applink

import com.bytedance.dataplatform.remote_config.Experiments

/**
 * Created by @ilhamsuaib on 5/22/24.
 */

internal object RouteManagerFeatureFlag {

    @JvmStatic
    fun isBtmEnabled(): Boolean {
        return Experiments.getAndroidEnableBtm(false)
    }
}
