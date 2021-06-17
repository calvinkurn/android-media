package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.adapter.ItemPeriodDateFilterListener
import com.tokopedia.shop.score.penalty.presentation.adapter.PenaltyPageAdapter.Companion.PAYLOAD_DATE_FILTER
import com.tokopedia.shop.score.penalty.presentation.model.ItemPeriodDetailPenaltyUiModel
import kotlinx.android.synthetic.main.item_detail_penalty_period_date_filter.view.*

class ItemPeriodDateFilterViewHolder(view: View, private val itemPeriodDateFilterListener: ItemPeriodDateFilterListener): AbstractViewHolder<ItemPeriodDetailPenaltyUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_detail_penalty_period_date_filter
    }

    override fun bind(element: ItemPeriodDetailPenaltyUiModel?) {
        with(itemView) {
            tvPeriodDetailPenalty?.text = getString(R.string.period_date_detail_penalty, element?.periodDetail.orEmpty())

            ic_detail_penalty_filter?.setOnClickListener {
                itemPeriodDateFilterListener.onDateClick()
            }
        }
    }

    override fun bind(element: ItemPeriodDetailPenaltyUiModel?, payloads: MutableList<Any>) {
        super.bind(element, payloads)

        if (payloads.isNullOrEmpty() || element == null) return

        when (payloads.getOrNull(0) as Int) {
            PAYLOAD_DATE_FILTER -> {
                with(itemView) {
                    if (element.periodDetail.isNotBlank()) {
                        tvPeriodDetailPenalty?.text = element.periodDetail
                    }
                }
            }
        }
    }
}