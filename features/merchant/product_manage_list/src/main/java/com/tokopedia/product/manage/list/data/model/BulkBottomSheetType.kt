package com.tokopedia.product.manage.list.data.model

import android.support.annotation.StringRes
import com.tokopedia.product.manage.list.R

abstract class BulkBottomSheetType(
        @StringRes val labelTitle: Int = 0,
        var editValue: String = "",
        val shouldDisplayDrawable: Boolean = false
) {
    companion object {
        private const val SELECT_ALL_ETALASE = -1
        const val STOCK_DEFAULT = 0
        const val STOCK_UNLIMITED = 1
        const val STOCK_LIMITED = 2
        const val STOCK_DELETED = 3
    }

    data class EtalaseType(var etalaseValue: String,
                           var etalaseId: Int = SELECT_ALL_ETALASE) : BulkBottomSheetType(
            R.string.product_bs_etalase_title, etalaseValue, false
    )

    data class StockType(var stockStatus: Int = STOCK_DEFAULT, var totalStock: Int = 0) : BulkBottomSheetType(
            R.string.product_bs_stock_title, stockStatus.toString(), false
    ) {
        fun getStatusStock(): String {
            return when (stockStatus) {
                STOCK_LIMITED -> "Stock Terbatas"
                STOCK_UNLIMITED -> "Tersedia"
                else -> ""
            }
        }
    }

    class DeleteType : BulkBottomSheetType(
            R.string.product_bs_delete_title, "", true
    )

}