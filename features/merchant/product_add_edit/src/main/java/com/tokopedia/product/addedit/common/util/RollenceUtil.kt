package com.tokopedia.product.addedit.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey.PRODUCT_TITLE_AB_TEST_KEY

object RollenceUtil {
    fun getProductTitleRollence(): Boolean {
        return RemoteConfigInstance.getInstance().abTestPlatform
                .getString(PRODUCT_TITLE_AB_TEST_KEY, "")
                .isNotEmpty()
    }
}