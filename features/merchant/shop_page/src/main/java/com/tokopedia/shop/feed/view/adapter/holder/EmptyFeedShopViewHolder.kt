package com.tokopedia.shop.feed.view.adapter.holder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.EmptyFeedShopBinding
import com.tokopedia.shop.feed.view.contract.FeedShopContract
import com.tokopedia.shop.feed.view.model.EmptyFeedShopUiModel
import com.tokopedia.utils.view.binding.viewBinding

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

    private val viewBinding : EmptyFeedShopBinding? by viewBinding()
    private val btnPost: View? = viewBinding?.btnPost

    override fun bind(element: EmptyFeedShopUiModel?) {
        btnPost?.setOnClickListener { mainView.onEmptyFeedButtonClicked() }
    }
}