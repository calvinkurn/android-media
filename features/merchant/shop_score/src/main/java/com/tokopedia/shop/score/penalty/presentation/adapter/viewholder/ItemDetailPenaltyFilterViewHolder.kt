package com.tokopedia.shop.score.penalty.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.penalty.presentation.model.ItemDetailPenaltyFilterUiModel
import kotlinx.android.synthetic.main.item_shop_score_detail_penalty_filter.view.*

class ItemDetailPenaltyFilterViewHolder(view: View): AbstractViewHolder<ItemDetailPenaltyFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_shop_score_detail_penalty_filter
    }

    override fun bind(element: ItemDetailPenaltyFilterUiModel?) {
        with(itemView) {
            tvPeriodDetailPenalty?.text = element?.periodDetail.orEmpty()
            sortFilterDetailPenalty?.apply {

            }

            ic_detail_penalty_filter?.setOnClickListener {

            }
        }
    }

    private fun setupSortFilter() {

    }
}