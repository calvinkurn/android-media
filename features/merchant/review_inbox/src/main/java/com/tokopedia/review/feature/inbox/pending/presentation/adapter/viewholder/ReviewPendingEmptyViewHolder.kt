package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.ReviewPendingEmptyUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.unifyprinciples.Typography

class ReviewPendingEmptyViewHolder(view: View) :
    AbstractViewHolder<ReviewPendingEmptyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.partial_review_empty
    }

    private var emptyImage: ImageView? = null
    private var emptyTitle: Typography? = null
    private var emptySubtitle: Typography? = null

    init {
        bindViews()
    }

    override fun bind(element: ReviewPendingEmptyUiModel) {
        with(element) {
            emptyImage?.loadImage(imageUrl)
            emptyTitle?.text = title
            emptySubtitle?.text = subtitle
        }
    }

    private fun bindViews() {
        with(itemView) {
            emptyImage = findViewById(R.id.reviewEmptyImage)
            emptyTitle = findViewById(R.id.reviewEmptyTitle)
            emptySubtitle = findViewById(R.id.reviewEmptySubtitle)
        }
    }
}