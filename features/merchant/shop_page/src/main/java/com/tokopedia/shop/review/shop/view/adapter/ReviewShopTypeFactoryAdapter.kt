package com.tokopedia.shop.review.shop.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.review.product.view.adapter.ReviewProductContentViewHolder
import com.tokopedia.shop.review.product.view.adapter.ReviewProductTypeFactoryAdapter

/**
 * Created by zulfikarrahman on 1/19/18.
 */
class ReviewShopTypeFactoryAdapter(viewListener: ReviewProductContentViewHolder.ListenerReviewHolder,
                                   private val shopReviewHolderListener: ReviewShopViewHolder.ShopReviewHolderListener) : ReviewProductTypeFactoryAdapter(viewListener) {
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ReviewShopViewHolder.Companion.LAYOUT) {
            ReviewShopViewHolder(parent, viewListener, shopReviewHolderListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    fun type(shopReviewModelContent: ReviewShopModelContent?): Int {
        return ReviewShopViewHolder.Companion.LAYOUT
    }

}