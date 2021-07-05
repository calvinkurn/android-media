package com.tokopedia.shop.feed.view.adapter.holder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isValidGlideContext
import com.tokopedia.seller_migration_common.constants.SellerMigrationConstants
import com.tokopedia.seller_migration_common.presentation.util.touchlistener.SellerMigrationTouchListener
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopSellerMigrationUiModel
import com.tokopedia.unifycomponents.HtmlLinkHelper
import kotlinx.android.synthetic.main.widget_shop_page_tab_feed_no_post_seller_migration.view.*

class EmptyFeedShopSellerMigrationViewHolder(view: View,
                                             private val mainView: FeedShopContract.View): AbstractViewHolder<EmptyFeedShopSellerMigrationUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.widget_shop_page_tab_feed_no_post_seller_migration
    }

    override fun bind(element: EmptyFeedShopSellerMigrationUiModel?) {
        with(itemView) {
            try {
                if(ivTabFeedNoPost?.context?.isValidGlideContext() == true)
                    ivTabFeedNoPost?.setImageUrl(SellerMigrationConstants.SELLER_MIGRATION_POST_FEED_BANNER_LINK)
            } catch (e: Throwable) { }
            tvSellerMigrationLearnMoreLink?.text = context?.let {
                HtmlLinkHelper(it, getString(com.tokopedia.seller_migration_common.R.string.seller_migration_bottom_sheet_footer)).spannedString
            }
            btnPlayStoreTabFeedNoPost?.setOnClickListener {
                mainView.onGotoPlayStoreClicked()
            }
            tvSellerMigrationLearnMoreLink?.setOnTouchListener(SellerMigrationTouchListener {
                mainView.onGotoLearnMoreClicked(it)
            })
        }
    }
}