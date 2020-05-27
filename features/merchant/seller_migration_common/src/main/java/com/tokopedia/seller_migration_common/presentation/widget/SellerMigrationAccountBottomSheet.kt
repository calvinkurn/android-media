package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class SellerMigrationAccountBottomSheet : BottomSheetUnify() {
    companion object {
        fun createNewInstance(context: Context) : SellerMigrationAccountBottomSheet {
            return SellerMigrationAccountBottomSheet().apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_account_bottom_sheet,null)
                setChild(view)
            }
        }
    }
}