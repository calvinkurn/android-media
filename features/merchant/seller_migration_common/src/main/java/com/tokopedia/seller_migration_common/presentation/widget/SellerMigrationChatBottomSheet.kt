package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R

class SellerMigrationChatBottomSheet(sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationBottomSheet(sellerMigrationBottomSheetListener) {

    companion object {
        fun createNewInstance(context: Context, sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationChatBottomSheet {
            return SellerMigrationChatBottomSheet(sellerMigrationBottomSheetListener).apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                setChild(view)
            }
        }
    }
}