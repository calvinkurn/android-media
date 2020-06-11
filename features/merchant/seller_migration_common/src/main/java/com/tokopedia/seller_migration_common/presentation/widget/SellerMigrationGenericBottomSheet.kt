package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.ACCOUNT_SELLER_MIGRATION_IMAGE_LINK
import com.tokopedia.seller_migration_common.getSellerMigrationDate
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.partial_seller_migration_warning.*
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

    override fun trackGoToSellerApp() {
        // No Op
    }

    override fun trackGoToPlayStore() {
        // No Op
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        accountSellerMigrationBottomsheetImage?.loadImage(ACCOUNT_SELLER_MIGRATION_IMAGE_LINK)
        setupPadding()
        setupWarningCard()
    }

    private fun setupWarningCard() {
        val remoteConfigDate = getSellerMigrationDate(context)
        if(remoteConfigDate.isNotBlank()) {
            sellerMigrationWarningCard.show()
            sellerMigrationWarningDate.text = remoteConfigDate
        }
    }

    private fun setupPadding() {
        setShowListener {
            val headerMargin = 16.toPx()
            bottomSheetWrapper.setPadding(0,0,0,0)
            (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(headerMargin,headerMargin,headerMargin,headerMargin)
        }
    }

}