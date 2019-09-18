package com.tokopedia.product.manage.list.data.model

import androidx.annotation.StringRes
import com.tokopedia.product.manage.list.R

abstract class BulkBottomSheetType(
        @StringRes val labelTitle: Int = 0,
        var editValue: String = "",
        val shouldDisplayDrawable: Boolean = false
) {
    companion object {
        const val ETALASE_DEFAULT = -1
        const val STOCK_DEFAULT = -1
        const val STOCK_EMPTY = 0
        const val STOCK_UNLIMITED = 1
        const val STOCK_DELETED = 3
    }

    data class EtalaseType(var etalaseValue: String = "",
                           var etalaseId: Int = ETALASE_DEFAULT) : BulkBottomSheetType(
            R.string.product_bs_etalase_title, etalaseValue, false
    )

    data class StockType(var stockStatus: Int = STOCK_DEFAULT) : BulkBottomSheetType(
            R.string.product_bs_stock_title, "", false
    ) {
        fun getStockStatusProductView(): String {
            return when (stockStatus) {
                STOCK_UNLIMITED -> "Stok Tersedia"
                STOCK_EMPTY -> "Stok Kosong"
                else -> ""
            }
        }
    }

    class DeleteType : BulkBottomSheetType(
            R.string.product_bs_delete_title, "", true
    )

}