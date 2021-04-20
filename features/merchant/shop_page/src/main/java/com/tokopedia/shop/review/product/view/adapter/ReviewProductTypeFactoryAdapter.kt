package com.tokopedia.shop.review.product.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * Created by zulfikarrahman on 1/16/18.
 */
open class ReviewProductTypeFactoryAdapter(protected val viewListener: ReviewProductContentViewHolder.ListenerReviewHolder) : BaseAdapterTypeFactory() {
    fun type(productReviewModelTitleHeader: ReviewProductModelTitleHeader?): Int {
        return ReviewProductTitleHeaderViewHolder.Companion.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return if (type == ReviewProductTitleHeaderViewHolder.Companion.LAYOUT) {
            ReviewProductTitleHeaderViewHolder(parent)
        } else if (type == ReviewProductContentViewHolder.Companion.LAYOUT) {
            ReviewProductContentViewHolder(parent, viewListener)
        } else {
            super.createViewHolder(parent, type)
        }
    }

    fun type(productReviewModelContent: ReviewProductModelContent?): Int {
        return ReviewProductContentViewHolder.Companion.LAYOUT
    }

}