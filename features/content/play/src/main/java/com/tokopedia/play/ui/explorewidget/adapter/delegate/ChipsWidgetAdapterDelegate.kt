package com.tokopedia.play.ui.explorewidget.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.databinding.ViewChipShimmeringBinding
import com.tokopedia.play.databinding.ViewWidgetChipsBinding
import com.tokopedia.play.ui.explorewidget.viewholder.ChipsViewHolder
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.ChipWidgetsUiModel
import com.tokopedia.play.view.uimodel.ChipsShimmering
import com.tokopedia.play_common.R as commonR

/**
 * @author by astidhiyaa on 02/12/22
 */
class ChipsWidgetAdapterDelegate {
    internal class Chips (private val listener : ChipsViewHolder.Chips.Listener) :
        TypedAdapterDelegate<ChipWidgetUiModel, ChipWidgetsUiModel, ChipsViewHolder.Chips>(commonR.layout.view_play_empty) {

        override fun onBindViewHolder(item: ChipWidgetUiModel, holder: ChipsViewHolder.Chips) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ChipsViewHolder.Chips {
            val view = ViewWidgetChipsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ChipsViewHolder.Chips(view, listener)
        }
    }

    internal class Shimmering: TypedAdapterDelegate<ChipsShimmering, ChipWidgetsUiModel, ChipsViewHolder.Shimmering>(commonR.layout.view_play_empty){
        override fun onBindViewHolder(
            item: ChipsShimmering,
            holder: ChipsViewHolder.Shimmering
        ) {}

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): ChipsViewHolder.Shimmering {
            val view = ViewChipShimmeringBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return ChipsViewHolder.Shimmering(view)
        }
    }
}
