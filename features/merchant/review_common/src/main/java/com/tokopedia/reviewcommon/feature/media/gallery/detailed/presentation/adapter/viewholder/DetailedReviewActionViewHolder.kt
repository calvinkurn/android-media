package com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewcommon.R
import com.tokopedia.reviewcommon.databinding.ItemReviewActionMenuBinding
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.adapter.typefactory.DetailedReviewActionAdapterTypeFactory
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.presentation.uimodel.DetailedReviewActionMenuUiModel

class DetailedReviewActionViewHolder(
    view: View,
    private val listener: DetailedReviewActionAdapterTypeFactory.Listener
) : AbstractViewHolder<DetailedReviewActionMenuUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_review_action_menu
    }

    private val binding = ItemReviewActionMenuBinding.bind(view)
    private var element: DetailedReviewActionMenuUiModel? = null

    init {
        binding.root.setOnClickListener {
            element?.let {
                listener.onReviewActionMenuClicked(it.name.id)
            }
        }
    }

    override fun bind(element: DetailedReviewActionMenuUiModel) {
        this.element = element
        binding.root.text = element.name.getStringValue(binding.root.context)
    }
}