package com.tokopedia.play.ui.explorewidget

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.databinding.ViewTabMenuBinding
import com.tokopedia.play.databinding.ViewWidgetHolderBinding
import com.tokopedia.play.view.uimodel.ChipWidgetUiModel
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetMedAdapter
import com.tokopedia.play.widget.ui.widget.medium.adapter.PlayWidgetMediumViewHolder

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetItemViewHolder {
    internal class Medium(
        binding: ViewWidgetHolderBinding
    ) : BaseViewHolder(binding.root) {

        private val layoutManager by lazy(LazyThreadSafetyMode.NONE) {
            GridLayoutManager(binding.root.context, 2)
        }

        private val cardListener = object : PlayWidgetMediumViewHolder.Channel.Listener{
            override fun onChannelImpressed(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            ) {}

            override fun onChannelClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            ) {}

            override fun onToggleReminderChannelClicked(
                item: PlayWidgetChannelUiModel,
                reminderType: PlayWidgetReminderType,
                position: Int
            ) {}

            override fun onMenuActionButtonClicked(
                view: View,
                item: PlayWidgetChannelUiModel,
                position: Int
            ) {}
        }

        private val cardAdapter = PlayWidgetMedAdapter(cardChannelListener = cardListener)

        val view = binding.root

        init {
            view.adapter = cardAdapter
            view.layoutManager = layoutManager
        }

        fun bind(item: PlayWidgetUiModel) {
            cardAdapter.setItemsAndAnimateChanges(item.items)
        }
    }

    internal class Chip(
        binding: ViewTabMenuBinding,
        listener: Listener
    ) : BaseViewHolder(binding.root) {

        private val chipsListener = object : ChipsViewHolder.Listener {
            override fun onChipsClicked(item: ChipWidgetUiModel) {
                listener.onChipsClicked(item)
            }
        }

        private val chipsAdapter by lazy(LazyThreadSafetyMode.NONE) {
            ChipsWidgetAdapter(chipsListener)
        }

        init {
            binding.root.adapter = chipsAdapter
        }

        fun bind(item: TabMenuUiModel) {
            chipsAdapter.setItemsAndAnimateChanges(item.items)
        }

        interface Listener {
            fun onChipsClicked(item: ChipWidgetUiModel)
        }
    }
}
