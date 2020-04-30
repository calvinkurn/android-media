package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_PRODUCT_IMAGE_LINK
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

class SellerMigrationProductBottomSheet(sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationBottomSheet(sellerMigrationBottomSheetListener) {

    companion object {
        fun createNewInstance(context: Context, sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener): SellerMigrationBottomSheet {
            return SellerMigrationProductBottomSheet(sellerMigrationBottomSheetListener).apply {
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        sellerMigrationBottomSheetProductImage.loadImage(SELLER_MIGRATION_PRODUCT_IMAGE_LINK)
        sellerMigrationBottomSheetProductImage.visibility = View.VISIBLE
        sellerMigrationBottomSheetContent.text = getString(R.string.seller_migration_product_bottom_sheet_content)
        sellerMigrationBottomSheetTitle.text = getString(R.string.seller_migration_product_bottom_sheet_title)
    }
}