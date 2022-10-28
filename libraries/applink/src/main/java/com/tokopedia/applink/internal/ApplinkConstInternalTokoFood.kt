package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

object ApplinkConstInternalTokoFood {

    @JvmField
    val HOST_FOOD = "food"

    //tokopedia-android-internal://food
    @JvmField
    val INTERNAL_TOKO_FOOD = "${DeeplinkConstant.SCHEME_INTERNAL}://$HOST_FOOD"

    //TokoFoodOrderTrackingActivity
    @JvmField
    val POST_PURCHASE = "$INTERNAL_TOKO_FOOD/postpurchase"

    //TokoFoodHomeFragment
    @JvmField
    val HOME = "$INTERNAL_TOKO_FOOD/home"

    //TokoFoodCategoryFragment
    @JvmField
    val CATEGORY = "$INTERNAL_TOKO_FOOD/category"

    //TokoFoodMerchantFragment
    @JvmField
    val MERCHANT = "$INTERNAL_TOKO_FOOD/merchant"

    //SearchContainerFragment
    @JvmField
    val SEARCH = "$INTERNAL_TOKO_FOOD/search"

}