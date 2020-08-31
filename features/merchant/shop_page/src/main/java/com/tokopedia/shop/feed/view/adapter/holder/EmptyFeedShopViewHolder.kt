package com.tokopedia.shop.feed.view.adapter.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shop.R
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopViewModel
import kotlinx.android.synthetic.main.empty_feed_shop.view.*

/**
 * @author by yfsx on 17/05/19.
 */
class EmptyFeedShopViewHolder(v: View,
                              private val mainView: FeedShopContract.View)
    : AbstractViewHolder<EmptyFeedShopViewModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.empty_feed_shop
    }

    override fun bind(element: EmptyFeedShopViewModel?) {
        if(GlobalConfig.isSellerApp()) {
            itemView.btn_post?.setOnClickListener { mainView.onEmptyFeedButtonClicked() }
        }
    }
}