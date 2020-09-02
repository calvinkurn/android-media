package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.view.View
import android.os.Bundle
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_CHAT_FIRST_IMAGE
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants.SELLER_MIGRATION_CHAT_SECOND_IMAGE
import com.tokopedia.user.session.UserSession

class SellerMigrationChatBottomSheet(titles: ArrayList<String> = arrayListOf(),
                                     contents: ArrayList<String> = arrayListOf(),
                                     images: ArrayList<String> = arrayListOf())
    : SellerMigrationBottomSheet(titles, contents, images, shouldDismissAfterRestore = false) {

    companion object {
        fun createNewInstance(context: Context) : SellerMigrationChatBottomSheet {
            with(context) {
                val titles = arrayListOf(getString(R.string.seller_migration_chat_bottom_sheet_title), getString(R.string.seller_migration_chat_bottom_sheet_title_second_page))
                val contents = arrayListOf(getString(R.string.seller_migration_chat_bottom_sheet_first_page_description), getString(R.string.seller_migration_chat_bottom_sheet_second_page_description))
                val images = arrayListOf(SELLER_MIGRATION_CHAT_FIRST_IMAGE, SELLER_MIGRATION_CHAT_SECOND_IMAGE)
                return SellerMigrationChatBottomSheet(titles, contents, images)
            }
        }
    }

    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = UserSession(context).userId
    }

    override fun inflateChildView(context: Context) {
        val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet,null)
        setChild(view)
    }

    override fun trackGoToSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_CHAT)
    }

    override fun trackGoToPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_CHAT)
    }

    override fun trackLearnMore() { /* noop */ }
}