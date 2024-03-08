package com.tokopedia.feedplus.browse.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedplus.browse.data.model.WidgetMenuModel
import com.tokopedia.feedplus.browse.presentation.model.ChipsModel
import com.tokopedia.feedplus.databinding.ItemFeedBrowseChipBinding
import com.tokopedia.play_common.util.ImpressionListener
import com.tokopedia.play_common.util.addImpressionListener
import com.tokopedia.play_common.util.removeImpressionListener
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by meyta.taliti on 11/08/23.
 */
internal class ChipViewHolder private constructor(
    binding: ItemFeedBrowseChipBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(binding.root) {

    private val chipView = binding.cuFeedBrowseLabel

    private var mImpressionListener: ImpressionListener? = null

    fun bind(item: ChipsModel) {
        mImpressionListener?.let { chipView.removeImpressionListener(it) }
        mImpressionListener = chipView.addImpressionListener {
            listener.onChipImpressed(this, item.menu)
        }

        chipView.chipText = item.menu.label

        onChipSelected(item.menu, item.isSelected)

        chipView.setOnClickListener {
            if (chipView.chipType == ChipsUnify.TYPE_SELECTED) return@setOnClickListener
            listener.onChipClicked(this, item.menu)
        }
    }

    private fun onChipSelected(item: WidgetMenuModel, isSelected: Boolean) {
        val newChipType = if (isSelected) {
            ChipsUnify.TYPE_SELECTED
        } else {
            ChipsUnify.TYPE_NORMAL
        }
        if (chipView.chipType != newChipType) {
            chipView.chipType = newChipType
        }

        if (isSelected) {
            listener.onChipSelected(this, item)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: Listener
        ): ChipViewHolder {
            return ChipViewHolder(
                ItemFeedBrowseChipBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                listener
            )
        }
    }

    interface Listener {
        fun onChipImpressed(viewHolder: ChipViewHolder, model: WidgetMenuModel)

        fun onChipClicked(viewHolder: ChipViewHolder, model: WidgetMenuModel)

        fun onChipSelected(viewHolder: ChipViewHolder, model: WidgetMenuModel)
    }
}
