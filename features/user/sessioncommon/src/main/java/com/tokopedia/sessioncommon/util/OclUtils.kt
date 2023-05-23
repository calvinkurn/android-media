package com.tokopedia.sessioncommon.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

class OclUtils {
    private val OCL_ROLLENCE = "ocl_an"
    fun isOclEnabled(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform.getString(OCL_ROLLENCE, "").isNotEmpty()
    }
}
