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
    @JvmField
    val HOME_OLD = "$INTERNAL_TOKO_FOOD/old-home"

    //TokoFoodCategoryFragment
    @JvmField
    val CATEGORY = "$INTERNAL_TOKO_FOOD/category"
    @JvmField
    val CATEGORY_OLD = "$INTERNAL_TOKO_FOOD/old-category"

    //TokoFoodMerchantFragment
    @JvmField
    val MERCHANT = "$INTERNAL_TOKO_FOOD/merchant"
    @JvmField
    val MERCHANT_OLD = "$INTERNAL_TOKO_FOOD/old-merchant"

    //SearchContainerFragment
    @JvmField
    val SEARCH = "$INTERNAL_TOKO_FOOD/search"
    @JvmField
    val SEARCH_OLD = "$INTERNAL_TOKO_FOOD/old-search"

}
