package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.ACCOUNT_SELLER_MIGRATION_IMAGE_LINK
import kotlinx.android.synthetic.main.widget_seller_migration_generic_bottom_sheet.*

class SellerMigrationGenericBottomSheet : SellerMigrationBottomSheet() {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationGenericBottomSheet {
            return SellerMigrationGenericBottomSheet().apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_generic_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        accountSellerMigrationBottomsheetImage?.loadImage(ACCOUNT_SELLER_MIGRATION_IMAGE_LINK)
    }

}