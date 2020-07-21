package com.tokopedia.seller_migration_common.presentation.widget

import android.content.Context
import android.os.Bundle
import android.view.View
import com.tokopedia.seller_migration_common.R
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTracking
import com.tokopedia.seller_migration_common.analytics.SellerMigrationTrackingConstants.EVENT_CLICK_GO_TO_SELLER_APP_VOUCHER
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.user.session.UserSession

class SellerMigrationVoucherTokoBottomSheet(titles: ArrayList<String> = arrayListOf(),
                                            contents: ArrayList<String> = arrayListOf(),
                                            images: ArrayList<String> = arrayListOf())
    : SellerMigrationBottomSheet(titles, contents, images, false) {

    private val userId by lazy { UserSession(context).userId }

    override fun inflateChildView(context: Context) {
        val view: View = View.inflate(context, R.layout.widget_seller_migration_bottom_sheet, null)
        setChild(view)
    }

    override fun trackGoToSellerApp() {
        SellerMigrationTracking.eventGoToSellerApp(userId, EVENT_CLICK_GO_TO_SELLER_APP_VOUCHER)
    }

    override fun trackGoToPlayStore() {
        SellerMigrationTracking.eventGoToPlayStore(userId, EVENT_CLICK_GO_TO_SELLER_APP_VOUCHER)
    }

    override fun trackLearnMore() {
        SellerMigrationTracking.eventLearnMoreVoucher(userId)
    }

    companion object {
        fun createNewInstance(context: Context): SellerMigrationVoucherTokoBottomSheet {
            with(context) {
                val titles = arrayListOf(getString(R.string.seller_migration_voucher_toko_title))
                val contents = arrayListOf(getString(R.string.seller_migration_voucher_toko_content))
                val images = arrayListOf(SellerMigrationConstants.SELLER_MIGRATION_VOUCHER_TOKO_IMAGE_LINK)
                return SellerMigrationVoucherTokoBottomSheet(titles, contents, images)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SellerMigrationTracking.eventOnClickVoucherMenu(userId)
    }
}