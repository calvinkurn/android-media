package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.showWithCondition
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

        payloads.forEachIndexed { index, _ ->
            if (payloads.getOrNull(index) == PenaltyPageAdapter.PAYLOAD_SELECTED_FILTER) {
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
                itemDetailPenaltyListener.onItemPenaltyClick(element)
            }
            root.setOnClickListener {
                itemDetailPenaltyListener.onItemPenaltyClick(element)
            }
        }
        setSelectedTabletBackground(element.isSelected)
    }

    private fun setSelectedTabletBackground(isSelected: Boolean) {
        binding?.run {
            if (DeviceScreenInfo.isTablet(root.context)) {
                shopPenaltySelected?.showWithCondition(isSelected)
            }
        }
    }
}