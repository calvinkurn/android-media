package com.tokopedia.sessioncommon.util

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import javax.inject.Inject

@ActivityScope
class OclUtils @Inject constructor(val abTestPlatform: AbTestPlatform) {
    private val OCL_ROLLENCE = "ocl_an"
    fun isOclEnabled(): Boolean {
        return abTestPlatform.getString(OCL_ROLLENCE, "").isNotEmpty()
    }
}
