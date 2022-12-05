package com.tokopedia.play.ui.explorewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play.databinding.ViewTabMenuBinding
import com.tokopedia.play.databinding.ViewWidgetHolderBinding
import com.tokopedia.play.view.uimodel.*

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapterDelegate {
    internal class Chips(private val listener: WidgetItemViewHolder.Chip.Listener) :
        TypedAdapterDelegate<TabMenuUiModel, WidgetUiModel, WidgetItemViewHolder.Chip>(R.layout.view_widget_holder) {
        override fun onBindViewHolder(item: TabMenuUiModel, holder: WidgetItemViewHolder.Chip) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): WidgetItemViewHolder.Chip {
            val view = ViewTabMenuBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WidgetItemViewHolder.Chip(view, listener)
        }

    }

    internal class Widget (private val listener: WidgetItemViewHolder.Medium.Listener):
        TypedAdapterDelegate<WidgetItemUiModel, WidgetUiModel, WidgetItemViewHolder.Medium>(R.layout.view_widget_holder) {
        override fun onBindViewHolder(
            item: WidgetItemUiModel,
            holder: WidgetItemViewHolder.Medium
        ) {
            holder.bind(item.item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): WidgetItemViewHolder.Medium {
            val view = ViewWidgetHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WidgetItemViewHolder.Medium(view, listener)
        }
    }
    internal class NextPage : TypedAdapterDelegate<PageConfig, WidgetUiModel, ViewHolder>(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(item: PageConfig, holder: ViewHolder) {}

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return BaseViewHolder(basicView)
        }

    }

    internal class SubSlot : TypedAdapterDelegate<SubSlotUiModel, WidgetUiModel, ViewHolder>(commonR.layout.view_play_empty) {
        override fun onBindViewHolder(item: SubSlotUiModel, holder: ViewHolder) {}

        override fun onCreateViewHolder(parent: ViewGroup, basicView: View): ViewHolder {
            return BaseViewHolder(basicView)
        }

    }
}
