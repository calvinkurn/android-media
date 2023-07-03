package com.tokopedia.homenav.mainnav.view.adapter.viewholder.review

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.MePage
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyStateRevampBinding
import com.tokopedia.homenav.mainnav.view.datamodel.review.EmptyStateReviewDataModel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

@MePage(MePage.Widget.REVIEW)
class EmptyReviewViewHolder(itemView: View) : AbstractViewHolder<EmptyStateReviewDataModel>(itemView) {
    private var binding: HolderEmptyStateRevampBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_state_revamp
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_review.png"
    }

    override fun bind(emptyStateReviewDataModel: EmptyStateReviewDataModel) {
        val context = itemView.context
        binding?.cardEmpty?.cardType = CardUnify2.TYPE_BORDER
        val imageView = binding?.emptyImage
        imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
        imageView?.setImageUrl(EMPTY_IMAGE_LINK)
        binding?.emptyText?.text = context.getText(com.tokopedia.homenav.R.string.empty_state_review)
    }
}
