package com.tokopedia.review.feature.inbox.buyerreview.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.SellerMigrationReviewModel
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemSellerMigrationReviewBinding

class SellerMigrationReviewViewHolder(
        view: View,
        private val listener: SellerMigrationReviewClickListener
) : AbstractViewHolder<SellerMigrationReviewModel>(view) {

    companion object {
        const val SELLER_MIGRATION_IMAGE_URL = "https://images.tokopedia.net/android/others/seller_migration_review_viewholder.png"
        val LAYOUT: Int = R.layout.item_seller_migration_review
    }

    private val binding = ItemSellerMigrationReviewBinding.bind(view)

    override fun bind(element: SellerMigrationReviewModel?) {
        binding.ivSellerMigrationReview.loadImage(SELLER_MIGRATION_IMAGE_URL)
        binding.root.setOnClickListener {
            listener.onSellerMigrationReviewClicked()
        }
    }

    interface SellerMigrationReviewClickListener {
        fun onSellerMigrationReviewClicked()
    }
}
