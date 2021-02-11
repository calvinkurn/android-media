package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollenceUtil {

    private const val SPECIFICATION_AB_TEST_KEY = "android_cat_spec"
    private const val SPECIFICATION_AB_TEST_VALUE_ENABLED = "android_cat_spec"

    fun getSpecificationRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getString(SPECIFICATION_AB_TEST_KEY, "")
                .equals(SPECIFICATION_AB_TEST_VALUE_ENABLED, ignoreCase = false)
    }
}