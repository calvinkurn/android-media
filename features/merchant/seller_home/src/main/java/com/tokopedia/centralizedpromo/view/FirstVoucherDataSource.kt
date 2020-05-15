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

    fun getFirstVoucherInfoItems() = listOf(
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
}