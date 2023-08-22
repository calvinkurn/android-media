package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import com.google.gson.annotations.SerializedName

data class UserGroupMetadata(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
) {

    companion object {
        const val KEY_PROMO_AB_TEST_USER_GROUP = "promo_revamp_ab_test_user_group"

        const val PROMO_USER_GROUP_A = "group-a"
        const val PROMO_USER_GROUP_B = "group-b"
        const val PROMO_USER_GROUP_C = "group-c"
    }
}
