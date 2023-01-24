package com.tokopedia.review.feature.bulk_write_review.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.databinding.ItemBulkReviewAnnouncementCardBinding
import com.tokopedia.review.feature.bulk_write_review.presentation.uimodel.BulkReviewAnnouncementUiModel

class BulkReviewAnnouncementViewHolder(
    itemView: View
) : AbstractViewHolder<BulkReviewAnnouncementUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_bulk_review_announcement_card
        private const val IMAGE_URL = "https://images.tokopedia.net/img/android/review/ic_bulk_review_announcement.png"
    }

    private val binding = ItemBulkReviewAnnouncementCardBinding.bind(itemView)

    init {
        initViews()
    }

    override fun bind(element: BulkReviewAnnouncementUiModel) {
        binding.tvBulkReviewAnnouncement.text = element.text
    }

    private fun initViews() {
        binding.icBulkReviewAnnouncement.loadImage(IMAGE_URL)
    }
}
