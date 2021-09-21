package com.tokopedia.shop_showcase.common.util

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.shop_showcase.common.AB_TEST_ROLLOUT_ETALASE_REVAMP
import com.tokopedia.shop_showcase.common.ShopType

object ShopShowcaseAbTestUtil {

    fun isShouldCheckShopType(): Boolean {
        val shopEtalaseRevampKey = RemoteConfigInstance.getInstance().abTestPlatform?.getString(
                AB_TEST_ROLLOUT_ETALASE_REVAMP,
                ""
        )
        return shopEtalaseRevampKey.equals(AB_TEST_ROLLOUT_ETALASE_REVAMP, true)
    }

    fun isNotRegularMerchant(type: String): Boolean {
        return type.equals(ShopType.GOLD_MERCHANT, true) || type.equals(ShopType.OFFICIAL_STORE, true)
    }

}