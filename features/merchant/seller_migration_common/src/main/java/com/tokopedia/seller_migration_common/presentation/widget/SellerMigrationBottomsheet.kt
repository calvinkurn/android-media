package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.seller_migration_common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.widget_seller_migration_bottom_sheet.*

class SellerMigrationBottomsheet(private val sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : BottomSheetUnify() {

    companion object {
        const val ACCOUNT_SELLER_MIGRATION_IMAGE_LINK = "https://ecs7.tokopedia.net/other/seller_migration_account.png"
        fun createNewInstance(context: Context, sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : SellerMigrationBottomsheet {
            return SellerMigrationBottomsheet(sellerMigrationBottomSheetListener).apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initView() {
        accountSellerMigrationBottomsheetImage.loadImage(ACCOUNT_SELLER_MIGRATION_IMAGE_LINK)
        accountSellerMigrationBottomSheetButton.setOnClickListener {
            sellerMigrationBottomSheetListener.onClickGoToSellerApp()
        }
        accountSellerMigrationBottomSheetFooter.setOnClickListener {
            sellerMigrationBottomSheetListener.onClickBottomSheetFooter()
        }
    }

}