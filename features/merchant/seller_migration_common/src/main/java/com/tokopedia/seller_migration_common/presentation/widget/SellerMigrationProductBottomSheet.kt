package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_PRODUCT_IMAGE_LINK

class SellerMigrationProductBottomSheet : SellerMigrationBottomSheet() {

    companion object {
        fun createNewInstance(context: Context): SellerMigrationBottomSheet {
            return SellerMigrationProductBottomSheet().apply {
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setValues()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setValues() {
        titles = listOf(getString(R.string.seller_migration_product_bottom_sheet_title))
        contents = listOf(getString(R.string.seller_migration_product_bottom_sheet_content))
        images = arrayListOf(SELLER_MIGRATION_PRODUCT_IMAGE_LINK)
    }
}