package com.tokopedia.vouchercreation.shop.voucherlist.view.widget.filterbottomsheet

import androidx.annotation.StringDef

/**
 * Created By @ilhamsuaib on 22/04/20
 */

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(FilterBy.CASHBACK, FilterBy.FREE_SHIPPING,
        FilterBy.PUBLIC, FilterBy.SPECIAL)
annotation class FilterBy {

    companion object {
        const val CASHBACK = "cashback"
        const val FREE_SHIPPING = "free_shipping"
        const val PUBLIC = "public"
        const val SPECIAL = "special"
    }
}