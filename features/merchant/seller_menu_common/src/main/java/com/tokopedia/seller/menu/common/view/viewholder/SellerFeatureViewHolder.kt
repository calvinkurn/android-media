package com.tokopedia.seller.menu.common.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.seller.menu.common.R
import com.tokopedia.seller.menu.common.view.uimodel.SellerFeatureUiModel
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import kotlinx.android.synthetic.main.item_seller_menu_feature_section.view.*

class SellerFeatureViewHolder(itemView: View): AbstractViewHolder<SellerFeatureUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_seller_menu_feature_section

        private const val SCREEN_NAME = "MA - Akun Toko"
    }

    override fun bind(feature: SellerFeatureUiModel) {
        itemView.cardStatistics.setOnClickListener {
            goToSellerMigrationPage(
                SellerMigrationFeatureName.FEATURE_SHOP_INSIGHT,
                arrayListOf(ApplinkConstInternalMarketplace.GOLD_MERCHANT_STATISTIC_DASHBOARD)
            )
        }

        itemView.cardPromo.setOnClickListener {
            goToSellerMigrationPage(
                SellerMigrationFeatureName.FEATURE_CENTRALIZED_PROMO,
                arrayListOf(ApplinkConstInternalSellerapp.CENTRALIZED_PROMO)
            )
        }

        itemView.cardFeedAndPlay.setOnClickListener {
            val appLinks = ArrayList<String>().apply {
                add(ApplinkConstInternalSellerapp.SELLER_HOME)
                add(UriUtil.buildUri(ApplinkConst.SHOP, feature.userSession.shopId))
                add(ApplinkConst.CONTENT_CREATE_POST)
            }
            goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_PLAY_FEED, appLinks)
        }

        itemView.cardFintech.setOnClickListener {
            val appLinks = ArrayList<String>().apply {
                add(ApplinkConstInternalSellerapp.SELLER_HOME)
                add(ApplinkConst.LAYANAN_FINANSIAL)
            }
            goToSellerMigrationPage(SellerMigrationFeatureName.FEATURE_FINANCIAL_SERVICES, appLinks)
        }
    }

    private fun goToSellerMigrationPage(@SellerMigrationFeatureName featureName: String, appLinks: ArrayList<String>) {
        itemView.context?.run {
            val intent = SellerMigrationActivity.createIntent(this, featureName, SCREEN_NAME, appLinks)
            startActivity(intent)
        }
    }
}