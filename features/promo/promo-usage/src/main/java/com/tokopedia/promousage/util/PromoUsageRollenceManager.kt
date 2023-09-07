package com.tokopedia.promousage.util

import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata
import javax.inject.Inject

class PromoUsageRollenceManager @Inject constructor() {

    fun isRevamp(userGroupMetadata: List<UserGroupMetadata>): Boolean {
        val promoAbTestUserGroup = userGroupMetadata
            .firstOrNull { it.key == UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP }
        if (promoAbTestUserGroup != null) {
            return promoAbTestUserGroup.value == UserGroupMetadata.PROMO_USER_VARIANT_A
                || promoAbTestUserGroup.value == UserGroupMetadata.PROMO_USER_VARIANT_B
        } else {
            return false
        }
    }

    fun getDefaultVariant() : String {
        return UserGroupMetadata.PROMO_USER_VARIANT_A
    }
}
