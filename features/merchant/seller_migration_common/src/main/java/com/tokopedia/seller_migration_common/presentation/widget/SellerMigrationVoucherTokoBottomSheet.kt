package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants

class SellerMigrationVoucherTokoBottomSheet(titles: List<String>,
                                            contents: List<String>,
                                            images: ArrayList<String>,
                                            onGoToSellerAppClicked: (type: String) -> Unit,
                                            onLearnMoreClicked: () -> Unit)
    : SellerMigrationBottomSheet(titles, contents, images, onGoToSellerAppClicked, onLearnMoreClicked) {

    val contentView: View = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)

    companion object {
        fun createNewInstance(context: Context,
                              onGoToSellerAppClicked: (type: String) -> Unit,
                              onLearnMoreClicked: () -> Unit
        ): SellerMigrationVoucherTokoBottomSheet {
            with(context) {
                val titles = listOf(getString(R.string.seller_migration_voucher_toko_title))
                val contents = listOf(getString(R.string.seller_migration_voucher_toko_content))
                val images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_VOUCHER_TOKO_IMAGE_LINK)
                return SellerMigrationVoucherTokoBottomSheet(titles, contents, images, onGoToSellerAppClicked, onLearnMoreClicked).apply {
                    setChild(contentView)
                }
            }
        }
    }
}