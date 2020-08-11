package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.user.session.UserSession

class SellerMigrationReviewBottomSheet(titles: ArrayList<String> = arrayListOf(),
                                       contents: ArrayList<String> = arrayListOf(),
                                       images: ArrayList<String> = arrayListOf())
    : SellerMigrationBottomSheet(titles, contents, images, shouldDismissAfterRestore = false) {

    companion object {
        fun createNewInstance(context: Context): SellerMigrationBottomSheet {
            with(context) {
                val titles = arrayListOf(getString(R.string.seller_migration_review_bottom_sheet_title))
                val contents = arrayListOf(getString(R.string.seller_migration_review_bottom_sheet_content))
                val images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_REVIEW_IMAGE_LINK)
                return SellerMigrationReviewBottomSheet(titles, contents, images)
            }

        }
    }

    private var userId = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = UserSession(context).userId
    }

    override fun inflateChildView(context: Context) {
        val view = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
        setChild(view)
    }

    override fun trackGoToSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_REVIEW)
    }

    override fun trackGoToPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(this.userId, SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_REVIEW)
    }

    override fun trackLearnMore() { /* noop */ }
}