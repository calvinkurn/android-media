package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel

class SellerMigrationReviewViewHolder(
        view: View,
        private val listener: SellerMigrationReviewClickListener
) : AbstractViewHolder<SellerMigrationReviewModel>(view) {

    companion object {
        val LAYOUT: Int = com.tokopedia.review.R.layout.item_seller_migration_review
    }

    override fun bind(element: SellerMigrationReviewModel?) {
        itemView.setOnClickListener {
            listener.onSellerMigrationReviewClicked()
        }
    }

    interface SellerMigrationReviewClickListener {
        fun onSellerMigrationReviewClicked()
    }
}