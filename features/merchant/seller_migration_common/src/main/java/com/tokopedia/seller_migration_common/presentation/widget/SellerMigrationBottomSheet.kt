package com.tokopedia.seller_migration_common.presentation.widget

import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.partial_seller_migration_footer.*

abstract class SellerMigrationBottomSheet(private val sellerMigrationBottomSheetListener: SellerMigrationBottomSheetListener) : BottomSheetUnify() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpButtons()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpButtons() {
        sellerMigrationBottomSheetButton.setOnClickListener {
            sellerMigrationBottomSheetListener.onClickGoToSellerApp()
        }
        sellerMigrationBottomSheetLink.text = getString(R.string.seller_migration_bottom_sheet_footer)
        sellerMigrationBottomSheetLink.setOnClickListener {
            sellerMigrationBottomSheetListener.onClickBottomSheetFooter()
        }
    }
}