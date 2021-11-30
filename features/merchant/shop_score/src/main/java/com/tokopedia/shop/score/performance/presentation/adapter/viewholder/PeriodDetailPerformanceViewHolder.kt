package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.ItemSectionDetailPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class PeriodDetailPerformanceViewHolder(view: View) :
    AbstractViewHolder<PeriodDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_section_detail_performance
    }

    private val binding: ItemSectionDetailPerformanceBinding? by viewBinding()

    override fun bind(element: PeriodDetailPerformanceUiModel?) {
        binding?.run {
            setContainerBackground()
            tvPerformanceDetailLabel.text =
                getString(R.string.title_detail_performa, element?.period.orEmpty())
            tvPerformanceDetailDate.text =
                getString(R.string.title_update_date, element?.nextUpdate.orEmpty())

            if (element?.isNewSeller == true) {
                tvPerformanceDetailLabel.text =
                    getString(R.string.title_detail_performance_new_seller)
                tvPerformanceDetailDate.text =
                    root.context?.getString(R.string.title_update_date_new_seller, element.period)
                tvPerformanceDetailDateNewSeller.text =
                    getString(R.string.title_update_date, element.nextUpdate)
            } else {
                tvPerformanceDetailLabel.text =
                    getString(R.string.title_detail_performa, element?.period.orEmpty())
                tvPerformanceDetailDate.text =
                    getString(R.string.title_update_date, element?.nextUpdate.orEmpty())
            }

            tvPerformanceDetailDateNewSeller.showWithCondition(element?.isNewSeller == true)
        }
    }

    private fun setContainerBackground() {
        try {
            binding?.run {
                root.context?.let {
                    containerSectionDetailPerformance.setBackgroundColor(
                        it.getResColor(R.color.shop_score_penalty_dms_container)
                    )
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }
}