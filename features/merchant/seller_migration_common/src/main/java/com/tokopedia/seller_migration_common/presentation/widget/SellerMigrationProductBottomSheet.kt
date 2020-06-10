package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_PRODUCT_IMAGE_LINK

class SellerMigrationProductBottomSheet(titles: List<String>,
                                        contents: List<String>,
                                        images: ArrayList<String>)
    : SellerMigrationBottomSheet(titles, contents, images) {

    companion object {
        fun createNewInstance(context: Context): SellerMigrationBottomSheet {
            with(context) {
                val titles = listOf(getString(R.string.seller_migration_product_bottom_sheet_title))
                val contents = listOf(getString(R.string.seller_migration_product_bottom_sheet_content))
                val images = arrayListOf(SELLER_MIGRATION_PRODUCT_IMAGE_LINK)
                return SellerMigrationProductBottomSheet(titles, contents, images).apply {
                    val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
                    setChild(view)
                }
            }
        }
    }
}