package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R

class SellerMigrationPromoBottomSheet(sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationBottomSheet(sellerMigrationBottomSheetListener) {

    companion object {

        fun createNewInstance(context: Context, sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationPromoBottomSheet {
            return SellerMigrationPromoBottomSheet(sellerMigrationBottomSheetListener).apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                setChild(view)
            }
        }
    }
}