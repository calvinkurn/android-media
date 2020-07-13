package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

class SellerMigrationPromoBottomSheet(
        titles: ArrayList<String> = arrayListOf(),
        contents: ArrayList<String> = arrayListOf(),
        images: ArrayList<String> = arrayListOf())
    : SellerMigrationBottomSheet(titles, contents, images) {

    companion object {
        fun createNewInstance(context: Context): SellerMigrationPromoBottomSheet {
            with(context) {
                val titles = arrayListOf(getString(R.string.seller_migration_promo_bottom_sheet_title), getString(R.string.seller_migration_promo_bottom_sheet_title_second_page))
                val contents = arrayListOf(getString(R.string.seller_migration_promo_bottom_sheet_content), getString(R.string.seller_migration_promo_bottom_sheet_content_second_page))
                val images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_PROMO_FIRST_IMAGE, SellerMigrationConstants.SELLER_MIGRATION_PROMO_SECOND_IMAGE)
                return SellerMigrationPromoBottomSheet(titles, contents, images)
            }
        }
    }

    override fun inflateChildView(context: Context) {
        val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
        setChild(view)
    }

    override fun trackGoToSellerApp() {
        // No op
    }

    override fun trackGoToPlayStore() {
        // No op
    }

    override fun trackLearnMore() { /* noop */
    }
}