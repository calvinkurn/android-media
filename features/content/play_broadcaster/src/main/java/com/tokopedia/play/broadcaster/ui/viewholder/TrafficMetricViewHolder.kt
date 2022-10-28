package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.databinding.ItemPlaySummaryInfoBinding
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricType
import com.tokopedia.play.broadcaster.ui.model.TrafficMetricUiModel

/**
 * @author by jessica on 05/06/20
 */

class TrafficMetricViewHolder(
    val binding: ItemPlaySummaryInfoBinding,
    val listener: Listener
) : BaseViewHolder(binding.root) {

    fun bind(metric: TrafficMetricUiModel) {
        with(binding) {
            tvItemPlaySummaryDescription.text = MethodChecker.fromHtml(getString(metric.type.label))
            tvItemPlaySummaryCountInfo.text = metric.count

            tvItemPlaySummaryDescription.setOnClickListener {
                listener.onLabelClicked(metric.type)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener,
        ): TrafficMetricViewHolder {
            return TrafficMetricViewHolder(
                ItemPlaySummaryInfoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener,
            )
        }
    }


    interface Listener {
        fun onLabelClicked(metricType: TrafficMetricType)
    }
}