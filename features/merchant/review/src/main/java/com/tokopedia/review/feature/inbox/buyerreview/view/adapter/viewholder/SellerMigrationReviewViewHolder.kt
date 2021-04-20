package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel
import kotlinx.android.synthetic.main.item_seller_migration_review.view.*

class SellerMigrationReviewViewHolder(
        view: View,
        private val listener: SellerMigrationReviewClickListener
) : AbstractViewHolder<SellerMigrationReviewModel>(view) {

    companion object {
        const val SELLER_MIGRATION_IMAGE_URL = "https://ecs7.tokopedia.net/android/others/seller_migration_review_viewholder.png"
        val LAYOUT: Int = com.tokopedia.review.R.layout.item_seller_migration_review
    }

    override fun bind(element: SellerMigrationReviewModel?) {
        itemView.iv_seller_migration_review.loadImage(SELLER_MIGRATION_IMAGE_URL)
        itemView.setOnClickListener {
            listener.onSellerMigrationReviewClicked()
        }
    }

    interface SellerMigrationReviewClickListener {
        fun onSellerMigrationReviewClicked()
    }
}