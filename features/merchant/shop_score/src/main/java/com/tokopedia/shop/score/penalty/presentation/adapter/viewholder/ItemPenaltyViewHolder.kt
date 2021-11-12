package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.getColoredIndicator
import com.tokopedia.shop.score.databinding.ItemShopScorePenaltyBinding
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemDetailPenaltyListener
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter
import com.tokopedia.shop.score.penalty.presentation.model.ItemPenaltyUiModel
import com.tokopedia.utils.view.binding.viewBinding


class ItemPenaltyViewHolder(
    view: View,
    private val itemDetailPenaltyListener: ItemDetailPenaltyListener
) : AbstractViewHolder<ItemPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_score_penalty
    }

    private val binding: ItemShopScorePenaltyBinding? by viewBinding()

    override fun bind(element: ItemPenaltyUiModel?) {
        if (element == null) return
        setupViews(element)
    }

    override fun bind(element: ItemPenaltyUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)
        if (payloads.isNullOrEmpty() || element == null) return

        when (payloads.getOrNull(0) as? Int) {
            PenaltyPageAdapter.PAYLOAD_SELECTED_FILTER -> {
                setSelectedTabletBackground(element.isSelected)
            }
        }
    }

    private fun setupViews(element: ItemPenaltyUiModel) = binding?.run {
        binding?.run {
            element.colorPenalty?.let {
                penaltyIndicator.background = getColoredIndicator(root.context, it)
                tvTitleStatusPenalty.setTextColor(ContextCompat.getColor(root.context, it))
            }
            tvTitleStatusPenalty.text = element.statusPenalty
            tvEndDateStatusPenalty.text = element.endDate
            tvDateStatusPenalty.text = element.startDate
            tvTitleTypePenalty.text = element.typePenalty

            tvInvoiceTransactionPenalty.text = element.invoicePenalty

            icTransactionPenaltyToDetail.setOnClickListener {
                itemDetailPenaltyListener.onItemPenaltyClick(element, adapterPosition)
            }
            root.setOnClickListener {
                itemDetailPenaltyListener.onItemPenaltyClick(element, adapterPosition)
            }
        }
    }

    private fun setSelectedTabletBackground(isSelected: Boolean) {
        binding?.shopPenaltySelected?.isVisible = isSelected
    }
}