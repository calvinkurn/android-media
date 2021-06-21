package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance

object RollenceUtil {

    private const val HAMPERS_AB_TEST_KEY = "hampers_android"
    private const val HAMPERS_AB_TEST_VALUE_ENABLED = "hampers_android"
    private const val PRODUCT_LIMITATION_AB_TEST_KEY = "product_limit"

    fun getHampersRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getString(HAMPERS_AB_TEST_KEY, "")
                .equals(HAMPERS_AB_TEST_VALUE_ENABLED, ignoreCase = false)
    }

    fun getProductLimitationRollence(): Boolean{
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getString(PRODUCT_LIMITATION_AB_TEST_KEY, "")
                .isNotEmpty()
    }
}