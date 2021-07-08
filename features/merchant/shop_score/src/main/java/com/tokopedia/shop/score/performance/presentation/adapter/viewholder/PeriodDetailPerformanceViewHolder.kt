package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor

import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import kotlinx.android.synthetic.main.item_section_detail_performance.view.*
import timber.log.Timber

class PeriodDetailPerformanceViewHolder(view: View) :
        AbstractViewHolder<PeriodDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_section_detail_performance
    }

    override fun bind(element: PeriodDetailPerformanceUiModel?) {
        with(itemView) {
            setContainerBackground()
            tvPerformanceDetailLabel?.text = getString(R.string.title_detail_performa, element?.period.orEmpty())
            tvPerformanceDetailDate?.text = getString(R.string.title_update_date, element?.nextUpdate.orEmpty())

            if (element?.isNewSeller == true) {
                tvPerformanceDetailLabel?.text = getString(R.string.title_detail_performance_new_seller)
                tvPerformanceDetailDate?.text = context?.getString(R.string.title_update_date_new_seller, element.period)
                tvPerformanceDetailDateNewSeller?.text = getString(R.string.title_update_date, element.nextUpdate)
            } else {
                tvPerformanceDetailLabel?.text = getString(R.string.title_detail_performa, element?.period.orEmpty())
                tvPerformanceDetailDate?.text = getString(R.string.title_update_date, element?.nextUpdate.orEmpty())
            }

            tvPerformanceDetailDateNewSeller?.showWithCondition(element?.isNewSeller == true)
        }
    }

    private fun setContainerBackground() {
        try {
            with(itemView) {
                context?.let {
                    containerSectionDetailPerformance?.setBackgroundColor(it.getResColor(R.color.shop_score_penalty_dms_container))
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }
}