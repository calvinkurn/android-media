package com.tokopedia.shop.score.performance.presentation.adapter.viewholder

import android.content.res.Resources
import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.databinding.PeriodDetailPerformanceBinding
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.widget.PeriodDetailPerformanceWidget
import com.tokopedia.utils.view.binding.viewBinding
import timber.log.Timber

class PeriodDetailPerformanceViewHolder(view: View) :
    AbstractViewHolder<PeriodDetailPerformanceUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.period_detail_performance
    }

    private val binding: PeriodDetailPerformanceBinding? by viewBinding()

    override fun bind(element: PeriodDetailPerformanceUiModel?) {
        binding?.periodDetailPerformance?.run {
            setData(element)
            setContainerBackground()
        }
    }

    private fun PeriodDetailPerformanceWidget.setContainerBackground() {
        try {
            binding?.run {
                containerSectionDetailPerformance.setBackgroundColor(
                    ContextCompat.getColor(
                        root.context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N0
                    )
                )
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e)
        }
    }
}