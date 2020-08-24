package com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.SellerMigrationReviewModel

class SellerMigrationReviewViewHolder(
        view: View,
        private val listener: SellerMigrationReviewClickListener
) : AbstractViewHolder<SellerMigrationReviewModel>(view) {

    companion object {
        val LAYOUT: Int = com.tokopedia.tkpd.tkpdreputation.R.layout.item_seller_migration_review
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