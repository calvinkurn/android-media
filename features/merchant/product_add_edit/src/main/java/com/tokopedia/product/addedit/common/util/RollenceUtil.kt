package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollenceUtil {

    private const val SPECIFICATION_AB_TEST_KEY = "android_cat_spec"
    private const val SPECIFICATION_AB_TEST_VALUE_ENABLED = "android_cat_spec"
    private const val HAMPERS_AB_TEST_KEY = "hampers_android"
    private const val HAMPERS_AB_TEST_VALUE_ENABLED = "hampers_android"

    fun getSpecificationRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getString(SPECIFICATION_AB_TEST_KEY, "")
                .equals(SPECIFICATION_AB_TEST_VALUE_ENABLED, ignoreCase = false)
    }

    fun getHampersRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getString(HAMPERS_AB_TEST_KEY, "")
                .equals(HAMPERS_AB_TEST_VALUE_ENABLED, ignoreCase = false)
    }
}