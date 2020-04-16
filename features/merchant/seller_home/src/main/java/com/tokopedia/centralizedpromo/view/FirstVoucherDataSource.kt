package com.tokopedia.centralizedpromo.view

import com.tokopedia.centralizedpromo.view.model.FirstVoucherUiModel
import com.tokopedia.sellerhome.R

object FirstVoucherDataSource {

    private const val INCREASE_SELLS_TITLE = "Tingkatkan penjualanmu"
    private const val INCREASE_SELLS_DESCRIPTION = "Terbukti ampuh menarik menarik perhatian pembeli dan meningkatkan penjualan."
    private const val TIME_TITLE = "Kelola waktu aktif voucher"
    private const val TIME_DESCRIPTION = "Cukup atur anggaran dan periode promosi sesuai strategi penjualanmu."
    private const val FLEXIBLE_PROMOTION_TITLE = "Penggunaan fleksibel"
    private const val FLEXIBLE_PROMOTION_DESCRIPTION = "Voucher berlaku untuk seluruh produkmu. Download & bagikan ke sosial media."

    fun getFirstVoucherInfoItems() = listOf(
            FirstVoucherUiModel(
                    iconDrawableRes = R.drawable.ic_voucher_increase_sells,
                    title = INCREASE_SELLS_TITLE,
                    description = INCREASE_SELLS_DESCRIPTION
            ),
            FirstVoucherUiModel(
                    iconDrawableRes = R.drawable.ic_voucher_waktu,
                    title = TIME_TITLE,
                    description = TIME_DESCRIPTION
            ),
            FirstVoucherUiModel(
                    iconDrawableRes = R.drawable.ic_voucher_promosi_fleksibel,
                    title = FLEXIBLE_PROMOTION_TITLE,
                    description = FLEXIBLE_PROMOTION_DESCRIPTION
            )
    )
}