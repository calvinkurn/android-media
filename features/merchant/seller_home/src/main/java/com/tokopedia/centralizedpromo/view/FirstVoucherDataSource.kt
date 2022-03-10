package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.FirstVoucherUiModel
import com.tokopedia.sellerhome.R

object FirstVoucherDataSource {

    private val INCREASE_SELLS_TITLE = R.string.centralized_promo_bottomsheet_increase_sell
    private val INCREASE_SELLS_DESCRIPTION = R.string.centralized_promo_bottomsheet_increase_sell_desc
    private val TIME_TITLE = R.string.centralized_promo_bottomsheet_time
    private val TIME_DESCRIPTION = R.string.centralized_promo_bottomsheet_time_desc
    private val FLEXIBLE_PROMOTION_TITLE = R.string.centralized_promo_bottomsheet_flexible
    private val FLEXIBLE_PROMOTION_DESCRIPTION = R.string.centralized_promo_bottomsheet_flexible_desc
    private val SELECTED_PRODUCT_TITLE = R.string.centralized_promo_bottomsheet_selected_product
    private val SELECTED_PRODUCT_DESCRIPTION = R.string.centralized_promo_bottomsheet_selected_product_desc
    private val FLEXIBLE_DISCOUNT_TITLE = R.string.centralized_promo_bottomsheet_flexible_discount
    private val FLEXIBLE_DISCOUNT_DESCRIPTION = R.string.centralized_promo_bottomsheet_flexible_discount_desc
    private val QUICK_SOLD_OUT_TITLE = R.string.centralized_promo_bottomsheet_quick_sold_out
    private val QUICK_SOLD_OUT_DESCRIPTION = R.string.centralized_promo_bottomsheet_quick_sold_out_desc

    const val IS_MVC_FIRST_TIME = "is_mvc_first_time"
    const val IS_PRODUCT_COUPON_FIRST_TIME = "is_product_coupon_first_time"

    fun getFirstVoucherCashbackInfoItems() = listOf(
            FirstVoucherUiModel(
                    iconDrawableRes = R.drawable.ic_voucher_increase_sells,
                    titleRes = INCREASE_SELLS_TITLE,
                    descriptionRes = INCREASE_SELLS_DESCRIPTION
            ),
            FirstVoucherUiModel(
                    iconDrawableRes = R.drawable.ic_voucher_waktu,
                    titleRes = TIME_TITLE,
                    descriptionRes = TIME_DESCRIPTION
            ),
            FirstVoucherUiModel(
                    iconDrawableRes = R.drawable.ic_voucher_promosi_fleksibel,
                    titleRes = FLEXIBLE_PROMOTION_TITLE,
                    descriptionRes = FLEXIBLE_PROMOTION_DESCRIPTION
            )
    )

    fun getFirstProductCouponInfoItems() = listOf(
        FirstVoucherUiModel(
            iconDrawableRes = R.drawable.ic_sah_hand_picked,
            titleRes = SELECTED_PRODUCT_TITLE,
            descriptionRes = SELECTED_PRODUCT_DESCRIPTION
        ),
        FirstVoucherUiModel(
            iconDrawableRes = R.drawable.ic_sah_manual_flexible,
            titleRes = FLEXIBLE_DISCOUNT_TITLE,
            descriptionRes = FLEXIBLE_DISCOUNT_DESCRIPTION
        ),
        FirstVoucherUiModel(
            iconDrawableRes = R.drawable.ic_sah_insight,
            titleRes = QUICK_SOLD_OUT_TITLE,
            descriptionRes = QUICK_SOLD_OUT_DESCRIPTION
        )
    )

}