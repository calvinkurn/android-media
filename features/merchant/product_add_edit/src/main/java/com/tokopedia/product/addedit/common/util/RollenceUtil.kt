package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollenceUtil {

    private const val SPECIFICATION_AB_TEST_KEY = "android_cat_spec"

    fun getSpecificationRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getBoolean(SPECIFICATION_AB_TEST_KEY, true)
    }
}