package com.tokopedia.play.ui.explorewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.databinding.ViewWidgetChipsBinding
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play_common.R as commonR

/**
 * @author by astidhiyaa on 02/12/22
 */
class ExploreWidgetAdapterDelegate {
    internal class Chips :
        TypedAdapterDelegate<ChipWidgetUiModel, ChipWidgetUiModel, ChipsViewHolder>(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(item: ChipWidgetUiModel, holder: ChipsViewHolder) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ChipsViewHolder {
            val view = ViewWidgetChipsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ChipsViewHolder(view)
        }
    }
}
