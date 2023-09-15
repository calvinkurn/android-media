package com.tokopedia.promousage.util

import com.tokopedia.kotlin.extensions.view.ifNull
import com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse.UserGroupMetadata

class PromoUsageRollenceManager {

    fun isRevamp(userGroupMetadata: List<UserGroupMetadata>): Boolean {
        val promoAbTestUserGroup = userGroupMetadata
            .firstOrNull { it.key == UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP }
            .ifNull {
                UserGroupMetadata(
                    key = UserGroupMetadata.KEY_PROMO_AB_TEST_USER_GROUP,
                    value = getDefaultUserGroup()
                )
            }
        return promoAbTestUserGroup.value == UserGroupMetadata.PROMO_USER_VARIANT_A ||
            promoAbTestUserGroup.value == UserGroupMetadata.PROMO_USER_VARIANT_B
    }

    fun getDefaultUserGroup(): String {
        return UserGroupMetadata.PROMO_USER_VARIANT_C
    }
}
