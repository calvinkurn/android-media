package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_CHAT_FIRST_IMAGE
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_CHAT_SECOND_IMAGE

class SellerMigrationChatBottomSheet : SellerMigrationBottomSheet() {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationChatBottomSheet {
            return SellerMigrationChatBottomSheet().apply{
                val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                setChild(view)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setValues()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setValues() {
        titles = listOf(getString(R.string.seller_migration_chat_bottom_sheet_title), getString(R.string.seller_migration_chat_bottom_sheet_title_second_page))
        contents = listOf(getString(R.string.seller_migration_chat_bottom_sheet_first_page_description), getString(R.string.seller_migration_chat_bottom_sheet_second_page_description))
        images = arrayListOf(SELLER_MIGRATION_CHAT_FIRST_IMAGE, SELLER_MIGRATION_CHAT_SECOND_IMAGE)
    }
}