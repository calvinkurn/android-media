package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

class SellerMigrationPromoBottomSheet(sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationBottomSheet(sellerMigrationBottomSheetListener) {

    companion object {
        fun createNewInstance(context: Context, sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationPromoBottomSheet {
            return SellerMigrationPromoBottomSheet(sellerMigrationBottomSheetListener).apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setValues()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setValues() {
        titles = listOf(getString(R.string.seller_migration_promo_bottom_sheet_title), getString(R.string.seller_migration_promo_bottom_sheet_title_second_page))
        contents = listOf(getString(R.string.seller_migration_promo_bottom_sheet_content), getString(R.string.seller_migration_promo_bottom_sheet_content_second_page))
        images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_PROMO_FIRST_IMAGE, SellerMigrationConstants.SELLER_MIGRATION_PROMO_SECOND_IMAGE)
    }
}