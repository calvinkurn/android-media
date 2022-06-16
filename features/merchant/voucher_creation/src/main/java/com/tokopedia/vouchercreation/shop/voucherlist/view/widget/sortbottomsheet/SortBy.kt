package com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sortbottomsheet

import androidx.annotation.StringDef

/**
 * Created By @ilhamsuaib on 22/04/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SortBy.NEWEST_DONE_DATE, SortBy.OLDEST_DONE_DATE)
annotation class SortBy {

    companion object {
        const val NEWEST_DONE_DATE = "tanggal_selesai_terkini"
        const val OLDEST_DONE_DATE = "tanggal_selesai_terlama"
    }
}