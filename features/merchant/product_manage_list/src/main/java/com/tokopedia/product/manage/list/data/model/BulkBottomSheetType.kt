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
    }

    data class EtalaseType(var etalaseValue: String,
                           var etalaseId: Int = SELECT_ALL_ETALASE) : BulkBottomSheetType(
            R.string.product_bs_etalase_title, etalaseValue, false
    )

    data class StockType(var stockValue: String, var totalStock: Int = 0) : BulkBottomSheetType(
            R.string.product_bs_stock_title, stockValue, false
    )

    class DeleteType : BulkBottomSheetType(
            R.string.product_bs_delete_title, "", true
    )

}