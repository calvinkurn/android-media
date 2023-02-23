package com.tokopedia.shop.score.performance.presentation.adapter.viewholder.tablet

import android.content.res.Resources
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.PeriodDetailPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.model.tablet.PeriodDetailTabletUiModel
import com.tokopedia.shop.score.performance.presentation.widget.PeriodDetailPerformanceWidget
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class PeriodDetailPerformanceTabletViewHolder(view: View) :
    AbstractViewHolder<PeriodDetailTabletUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.period_detail_performance
    }

    private val binding: PeriodDetailPerformanceBinding? by viewBinding()

    override fun bind(element: PeriodDetailTabletUiModel?) {
        binding?.periodDetailPerformance?.run {
            setData(element)
            setContainerBackground()
        }
    }

    private fun PeriodDetailPerformanceWidget.setContainerBackground() {
        try {
            binding?.run {
                root.context?.let {
                    containerSectionDetailPerformance.setBackgroundResource(
                        R.drawable.corner_rounded_period_detail_tablet
                    )
                }
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }
}
