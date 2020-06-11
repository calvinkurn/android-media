package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_CHAT_FIRST_IMAGE
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_CHAT_SECOND_IMAGE

class SellerMigrationChatBottomSheet(titles: List<String>,
                                     contents: List<String>,
                                     images: ArrayList<String>)
    : SellerMigrationBottomSheet(titles, contents, images) {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationChatBottomSheet {
            with(context) {
                val titles = listOf(getString(R.string.seller_migration_chat_bottom_sheet_title), getString(R.string.seller_migration_chat_bottom_sheet_title_second_page))
                val contents = listOf(getString(R.string.seller_migration_chat_bottom_sheet_first_page_description), getString(R.string.seller_migration_chat_bottom_sheet_second_page_description))
                val images = arrayListOf(SELLER_MIGRATION_CHAT_FIRST_IMAGE, SELLER_MIGRATION_CHAT_SECOND_IMAGE)
                return SellerMigrationChatBottomSheet(titles, contents, images).apply{
                    val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
                    setChild(view)
                }
            }
        }
    }
}