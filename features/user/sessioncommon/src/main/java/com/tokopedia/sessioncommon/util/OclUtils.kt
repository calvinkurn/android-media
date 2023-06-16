package com.tokopedia.sessioncommon.util

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

@ActivityScope
class OclUtils @Inject constructor(val abTestPlatform: AbTestPlatform) {
    fun isOclEnabled(): Boolean {
        return abTestPlatform.getString(OCL_ROLLENCE, "").isNotEmpty()
    }

    companion object {
        const val OCL_ROLLENCE = "ocl_an"
    }
}
