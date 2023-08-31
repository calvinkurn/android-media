package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserGroupMetadata(
    @SerializedName("key")
    val key: String = "",
    @SerializedName("value")
    val value: String = ""
) : Parcelable {

    companion object {
        const val KEY_PROMO_AB_TEST_USER_GROUP = "promo_revamp_ab_test_user_group"

        const val PROMO_USER_GROUP_A = "group-a"
        const val PROMO_USER_GROUP_B = "group-b"
        const val PROMO_USER_GROUP_C = "group-c"
    }
}
