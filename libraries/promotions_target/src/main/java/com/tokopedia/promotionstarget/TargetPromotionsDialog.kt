package com.tokopedia.targetpromotions

import com.tokopedia.promotionstarget.R


class TargetPromotionsDialog {

    enum class TargetPromotionsCouponType {
        SHOW_COUPON,
        COUPON_ACTION_TAKEN,
        COUPON_ERROR
    }

    class TargetPromotionsDialogData {

    }

    class setCouponUi(couponUiType: TargetPromotionsCouponType) {
        val layout = when (couponUiType) {
            TargetPromotionsCouponType.SHOW_COUPON -> R.layout.dialog_target_promotions
            else -> R.layout.dialog_target_promotions
        }
    }
}