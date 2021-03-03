package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import kotlinx.android.synthetic.main.item_section_detail_performance.view.*

class PeriodDetailPerformanceViewHolder(view: View):
        AbstractViewHolder<PeriodDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_section_detail_performance
    }

    override fun bind(element: PeriodDetailPerformanceUiModel?) {
        with(itemView) {
            tvPerformanceDetailDate.text = String.format(
                    getString(R.string.title_period_performance_detail),
                    element?.period.orEmpty(),
                    element?.nextUpdate.orEmpty())
        }
    }
}