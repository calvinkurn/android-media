package com.tokopedia.feed_shop.shop.view.adapter.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feed_shop.R
import com.tokopedia.feed_shop.shop.view.contract.FeedShopContract
import com.tokopedia.feed_shop.shop.view.model.EmptyFeedShopUiModel

/**
 * @author by yfsx on 17/05/19.
 */
class EmptyFeedShopViewHolder(v: View,
                              private val mainView: FeedShopContract.View)
    : AbstractViewHolder<EmptyFeedShopUiModel>(v) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.empty_feed_shop
    }

    private val btnPost: View? = v.findViewById(R.id.btn_post)

    override fun bind(element: EmptyFeedShopUiModel?) {
        btnPost?.setOnClickListener { mainView.onEmptyFeedButtonClicked() }
    }
}