package com.tokopedia.universal_sharing.view.bottomsheet.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_SELECTED
import com.tokopedia.unifycomponents.ChipsUnify.Companion.TYPE_NORMAL
import com.tokopedia.universal_sharing.R
import com.tokopedia.universal_sharing.view.model.ChipProperties

class ChipViewHolder(
    view: View,
    private val listener: Listener?
) : RecyclerView.ViewHolder(view) {

    private val chip = view.findViewById<ChipsUnify>(R.id.view_chip)

    fun onBind(element: ChipProperties) {
        // chip title
        chip.chipText = element.title

        // state bind
        if (element.isSelected) {
            chip.chipType = TYPE_SELECTED
        } else {
            chip.chipType = TYPE_NORMAL
        }

        // on click bind
        chip.setOnClickListener {
            listener?.onChipSelected(element)
            toggleChipState(element)
        }

        bindState(element)
    }

    private fun bindState(element: ChipProperties) {
        val isSelected = listener?.isSelected(element) ?: false

        val state = if (isSelected) {
            TYPE_SELECTED
        } else {
            TYPE_NORMAL
        }

        chip.chipType = state
    }

    private fun toggleChipState(element: ChipProperties) {
        element.isSelected = !element.isSelected

        val state = when (chip.chipType) {
            TYPE_NORMAL -> TYPE_SELECTED
            TYPE_SELECTED -> TYPE_NORMAL
            else -> null
        }

        state?.let {
            chip.chipType = it
        }
    }

    interface Listener {
        fun onChipSelected(chip: ChipProperties)
        fun isSelected(chip: ChipProperties): Boolean
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_chip

        fun create(viewGroup: ViewGroup, listener: Listener?): ChipViewHolder {
            val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(LAYOUT, viewGroup, false)

            return ChipViewHolder(view, listener)
        }
    }

}
