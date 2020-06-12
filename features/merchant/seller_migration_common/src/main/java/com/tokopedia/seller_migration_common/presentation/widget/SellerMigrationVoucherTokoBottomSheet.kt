package com.tokopedia.seller_migration_common.presentation.widget

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

class SellerMigrationVoucherTokoBottomSheet(titles: List<String>,
                                            contents: List<String>,
                                            images: ArrayList<String>)
    : SellerMigrationBottomSheet(titles, contents, images) {

    companion object {
        fun createNewInstance(context: Context): SellerMigrationVoucherTokoBottomSheet {
            with(context) {
                val titles = listOf(getString(R.string.seller_migration_voucher_toko_title))
                val contents = listOf(getString(R.string.seller_migration_voucher_toko_content))
                val images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_VOUCHER_TOKO_IMAGE_LINK)
                return SellerMigrationVoucherTokoBottomSheet(titles, contents, images).apply {
                    val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
                    setChild(view)
                }
            }
        }
    }
}