package com.tokopedia.shop.feed.view.adapter.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.widget_shop_page_tab_feed_no_post_seller_migration.view.*

class EmptyFeedShopSellerMigrationViewHolder(view: View,
                                             private val mainView: FeedShopContract.View): AbstractViewHolder<EmptyFeedShopViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_shop_page_tab_feed_no_post_seller_migration
    }

    override fun bind(element: EmptyFeedShopViewModel?) {
        with(itemView) {
            ivTabFeedNoPost?.setImageUrl(SellerMigrationConstants.SELLER_MIGRATION_POST_FEED_BANNER_LINK)
            tvTitleTabFeedNoPost?.text = getString(R.string.seller_migration_tab_feed_no_post_title)
            tvDescTabFeedNoPost?.text = getString(R.string.seller_migration_tab_feed_no_post_description)
            sellerMigrationLearnMoreLink?.text = context?.let { HtmlLinkHelper(it, getString(com.tokopedia.seller_migration_common.R.string.seller_migration_bottom_sheet_footer)).spannedString }
            btnPlayStoreTabFeedNoPost?.setOnClickListener {
                mainView.onGotoPlayStoreClicked()
            }
            sellerMigrationLearnMoreLink?.setOnTouchListener(SellerMigrationTouchListener {
                mainView.onGotoLearnMoreClicked(it)
            })
        }
    }
}