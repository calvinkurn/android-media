package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.analytics.SellerMenuTracker
import com.tokopedia.seller.menu.common.constant.AdminFeature
import com.tokopedia.seller.menu.common.view.uimodel.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import kotlinx.android.synthetic.main.item_seller_menu_feature_section.view.*

class SellerFeatureViewHolder(
        itemView: View,
        private val sellerMenuTracker: SellerMenuTracker?
): AbstractViewHolder<SellerFeatureUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_feature_section

        private const val SCREEN_NAME = "MA - Akun Toko"
    }

    override fun bind(feature: SellerFeatureUiModel) {
        itemView.cardStatistics.setOnClickListener {
            if (feature.userSession.isShopOwner) {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(ApplinkConstInternalMechant.MERCHANT_STATISTIC_DASHBOARD)
                }
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT, appLinks)
            } else {
                goToToAdminAuthorizationPage(AdminFeature.STATISTIC)
            }
            sellerMenuTracker?.sendEventClickShopStatistic()
        }

        itemView.cardPromo.setOnClickListener {
            if (feature.userSession.isShopOwner) {
                val appLinks = ArrayList<String>().apply {
                    add(ApplinkConstInternalSellerapp.SELLER_HOME)
                    add(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO)
                }
                goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_CENTRALIZED_PROMO, appLinks)
            } else {
                goToToAdminAuthorizationPage(AdminFeature.ADS_AND_PROMOTION)
            }
            sellerMenuTracker?.sendEventClickCentralizePromo()
        }

        itemView.cardFeedAndPlay.setOnClickListener {
            val appLinks = ArrayList<String>().apply {
                add(ApplinkConstInternalSellerapp.SELLER_HOME)
                add(UriUtil.buildUri(ApplinkConst.SHOP, feature.userSession.shopId))
                add(ApplinkConst.CONTENT_CREATE_POST)
            }
            goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_PLAY_FEED, appLinks)
            sellerMenuTracker?.sendEventClickFeedAndPlay()
        }

        itemView.cardFintech.setOnClickListener {
            val appLinks = ArrayList<String>().apply {
                add(ApplinkConstInternalSellerapp.SELLER_HOME)
                add("${ApplinkConst.LAYANAN_FINANSIAL}/")
            }
            goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_FINANCIAL_SERVICES, appLinks)
            sellerMenuTracker?.sendEventClickFintech()
        }
    }

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        itemView.context?.run {
            val intent = SellerMigrationActivity.createIntent(this, featureName, SCREEN_NAME, appLinks)
            startActivity(intent)
        }
    }

    private fun goToToAdminAuthorizationPage(@AdminFeature featureName: String) {
        itemView.context?.run {
            RouteManager.route(this, UriUtil.buildUri(ApplinkConstInternalSellerapp.ADMIN_AUTHORIZE, featureName))
        }
    }
}