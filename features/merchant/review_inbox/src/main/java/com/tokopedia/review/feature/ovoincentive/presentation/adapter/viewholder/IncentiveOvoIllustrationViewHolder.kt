package com.tokopedia.review.feature.ovoincentive.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.feature.ovoincentive.presentation.model.IncentiveOvoIllustrationUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemIncentiveOvoIllustrationBinding

class IncentiveOvoIllustrationViewHolder(
    itemView: View
) : AbstractViewHolder<IncentiveOvoIllustrationUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.item_incentive_ovo_illustration
    }

    private val binding = ItemIncentiveOvoIllustrationBinding.bind(itemView)

    override fun bind(element: IncentiveOvoIllustrationUiModel) {
        with(element) {
            bindIllustrationImage()
            bindIllustrationText()
        }
    }

    private fun IncentiveOvoIllustrationUiModel.bindIllustrationImage() {
        binding.ivIllustration.loadImage(imageUrl)
    }

    private fun IncentiveOvoIllustrationUiModel.bindIllustrationText() {
        binding.tvIllustration.text = text
    }
}
