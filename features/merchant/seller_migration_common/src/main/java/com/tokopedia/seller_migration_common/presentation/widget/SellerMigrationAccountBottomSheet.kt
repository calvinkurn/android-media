package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.seller_migration_common.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.widget_seller_migration_account_bottom_sheet.*

class SellerMigrationAccountBottomSheet(sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationBottomSheet(sellerMigrationBottomSheetListener) {

    companion object {
        const val ACCOUNT_SELLER_MIGRATION_IMAGE_LINK = "https://ecs7.tokopedia.net/other/seller_migration_account.png"
        fun createNewInstance(context: Context, sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationAccountBottomSheet {
            return SellerMigrationAccountBottomSheet(sellerMigrationBottomSheetListener).apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_account_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        accountSellerMigrationBottomsheetImage.loadImage(ACCOUNT_SELLER_MIGRATION_IMAGE_LINK)
    }

}