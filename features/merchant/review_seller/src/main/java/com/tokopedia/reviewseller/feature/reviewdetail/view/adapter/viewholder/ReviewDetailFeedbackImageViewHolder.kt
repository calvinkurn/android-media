package com.tokopedia.reviewseller.feature.reviewdetail.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.unifycomponents.ImageUnify

class ReviewDetailFeedbackImageViewHolder(
        itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private var ivItemFeedback: ImageUnify? = null

    init {
         ivItemFeedback = itemView.findViewById(R.id.ivAttachmentFeedback)
    }

    fun bind(data: FeedbackUiModel.Attachment) {
        ivItemFeedback?.setImageUrl(data.thumbnailURL.orEmpty())
    }
}