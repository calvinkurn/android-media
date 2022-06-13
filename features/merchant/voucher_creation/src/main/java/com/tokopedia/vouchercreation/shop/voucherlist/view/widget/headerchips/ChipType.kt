package com.tokopedia.vouchercreation.shop.voucherlist.view.widget.headerchips

import androidx.annotation.IntDef

/**
 * Created By @ilhamsuaib on 20/04/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@IntDef(ChipType.CHIP_SORT, ChipType.CHIP_FILTER)
annotation class ChipType {

    companion object {
        const val CHIP_RESET = -1
        const val CHIP_SORT = 0
        const val CHIP_FILTER = 1
    }
}