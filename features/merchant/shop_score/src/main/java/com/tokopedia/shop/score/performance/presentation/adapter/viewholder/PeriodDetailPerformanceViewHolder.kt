package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.rangeTotalDays
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

            if (element?.isNewSeller == true) {
                val totalRangeDays = rangeTotalDays(element.period).toString()
                tvPerformanceDetailLabel?.text = getString(R.string.title_detail_performance_new_seller)
                tvPerformanceDetailDate?.text = getString(R.string.title_update_date_new_seller, totalRangeDays)
                tvPerformanceDetailDateNewSeller?.text = getString(R.string.title_update_date, element.nextUpdate)
            } else {
                tvPerformanceDetailLabel?.text = getString(R.string.title_detail_performa, element?.period.orEmpty())
                tvPerformanceDetailDate?.text = getString(R.string.title_update_date, element?.nextUpdate.orEmpty())
            }

            tvPerformanceDetailDateNewSeller?.showWithCondition(element?.isNewSeller == true)
        }
    }
}