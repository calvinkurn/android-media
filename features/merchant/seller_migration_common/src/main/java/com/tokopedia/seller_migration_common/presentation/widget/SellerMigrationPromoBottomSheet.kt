package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

class SellerMigrationPromoBottomSheet(
        titles: List<String>,
        contents: List<String>,
        images: ArrayList<String>)
    : SellerMigrationBottomSheet(titles, contents, images) {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationPromoBottomSheet {
            with(context) {
                val titles = listOf(getString(R.string.seller_migration_promo_bottom_sheet_title), getString(R.string.seller_migration_promo_bottom_sheet_title_second_page))
                val contents = listOf(getString(R.string.seller_migration_promo_bottom_sheet_content), getString(R.string.seller_migration_promo_bottom_sheet_content_second_page))
                val images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_PROMO_FIRST_IMAGE, SellerMigrationConstants.SELLER_MIGRATION_PROMO_SECOND_IMAGE)
                return SellerMigrationPromoBottomSheet(titles, contents, images).apply{
                    val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                    setChild(view)
                }
            }
        }
    }
}