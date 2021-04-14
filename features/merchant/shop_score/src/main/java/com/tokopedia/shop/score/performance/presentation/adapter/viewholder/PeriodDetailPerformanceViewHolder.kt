package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import kotlinx.android.synthetic.main.item_section_detail_performance.view.*

class PeriodDetailPerformanceViewHolder(view: View) :
        AbstractViewHolder<PeriodDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_section_detail_performance
    }

    override fun bind(element: PeriodDetailPerformanceUiModel?) {
        with(itemView) {
            setBackgroundColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
            tvPerformanceDetailLabel?.text = getString(R.string.title_detail_performa, element?.period.orEmpty())
            tvPerformanceDetailDate?.text = getString(R.string.title_update_date, element?.nextUpdate.orEmpty())
        }
    }
}