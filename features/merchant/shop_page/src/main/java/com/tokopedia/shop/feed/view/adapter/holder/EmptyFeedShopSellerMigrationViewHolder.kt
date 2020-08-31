package com.tokopedia.shop.feed.view.adapter.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import kotlinx.android.synthetic.main.widget_shop_page_tab_feed_no_post_seller_migration.view.*

class EmptyFeedShopSellerMigrationViewHolder(view: View,
                                             private val mainView: FeedShopContract.View): AbstractViewHolder<EmptyFeedShopViewModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_shop_page_tab_feed_no_post_seller_migration
    }

    override fun bind(element: EmptyFeedShopViewModel?) {
        with(itemView) {
            tvTitleSellerMigration?.text = getString(R.string.seller_migration_tab_feed_no_post_title)
            tvDescSellerMigration?.text = getString(R.string.seller_migration_tab_feed_no_post_description)
            sellerMigrationPlayStoreButton?.setOnClickListener {
                mainView.onGotoPlayStoreClicked()
            }
            sellerMigrationLearnMoreLink?.setOnTouchListener(SellerMigrationTouchListener {
                mainView.onGotoLearnMoreClicked(it)
            })
        }
    }
}