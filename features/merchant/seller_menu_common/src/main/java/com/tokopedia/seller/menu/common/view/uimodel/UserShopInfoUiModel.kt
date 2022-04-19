package com.tokopedia.seller.menu.common.view.uimodel

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.seller.menu.common.view.uimodel.base.ShopType

data class UserShopInfoWrapper(val shopType: ShopType?,
                               val userShopInfoUiModel: UserShopInfoUiModel? = UserShopInfoUiModel()) {
    data class UserShopInfoUiModel(
            var isBeforeOnDate: Boolean = false,
            var dateCreated: String = "",
            var onDate: String = "",
            var totalTransaction: Long = 0L,
            var badge: String = "",
            var shopTierName: String = "",
            var shopTier: Int = -1,
            var pmProGradeName: String = "",
            var periodTypePmPro: String = "",
            var isNewSeller: Boolean = false,
            var isEligiblePmPro: Boolean = false,
            var isEligiblePm: Boolean = false
    ) {
        fun getPowerMerchantEligibleIcon(): Int? {
            return if (isEligiblePm) {
                IconUnify.BADGE_PM_FILLED
            } else {
                null
            }
        }

        fun getPowerMerchantProEligibleIcon(): Int? {
            return if (isEligiblePmPro) {
                IconUnify.BADGE_PMPRO_FILLED
            } else {
                null
            }
        }
    }
}