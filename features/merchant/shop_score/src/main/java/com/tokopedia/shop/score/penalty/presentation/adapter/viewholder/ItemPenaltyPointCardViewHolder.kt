package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.imageassets.TokopediaImageUrl
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemPenaltyPointCardBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPenaltyPointCardListener
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyPointCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ItemPenaltyPointCardViewHolder(
    view: View,
    private val listener: ItemPenaltyPointCardListener?
) : AbstractViewHolder<ItemPenaltyPointCardUiModel>(view) {

    private val binding: ItemPenaltyPointCardBinding? by viewBinding()

    override fun bind(element: ItemPenaltyPointCardUiModel) {
        val score = element.result.penaltyDynamic
        binding?.tvPenaltyCardPoint?.text = score.toString()
        binding?.tvPenaltyCardDate?.text = element.date
        binding?.btnPenaltyCardPoint?.showWithCondition(score < Int.ZERO)
        setImageBackground(score)
        setButtonClickListener(element)
    }

    private fun setImageBackground(score: Int) {
        val imageUrl =
            if (score < Int.ZERO) {
                TokopediaImageUrl.IMG_SHOP_SCORE_PENALTY_RED
            } else {
                TokopediaImageUrl.IMG_SHOP_SCORE_PENALTY_GREEN
            }

        binding?.imgPenaltyCard?.loadImage(imageUrl)
    }

    private fun setButtonClickListener(element: ItemPenaltyPointCardUiModel) {
        binding?.btnPenaltyCardPoint?.setOnClickListener {
            listener?.onPenaltyPointsButtonClicked(element)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_penalty_point_card
    }

}
