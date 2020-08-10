package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_variant_type.view.*

class VariantTypeViewHolder(itemView: View, clickListener: OnVariantTypeViewHolderClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnVariantTypeViewHolderClickListener {
        fun onVariantTypeSelected(position: Int)
        fun onVariantTypeDeselected(position: Int): Boolean
    }

    enum class ViewHolderState {
        SELECTED,
        NORMAL,
        DISABLED
    }

    var viewHolderState = ViewHolderState.NORMAL

    init {
        itemView.chipsVariantTypeName.setOnClickListener {
            when (viewHolderState) {
                // from selected to normal state (deselection)
                ViewHolderState.SELECTED -> {
                    // change the chip state when the cancellation confirmed
                    val isConfirmed = clickListener.onVariantTypeDeselected(adapterPosition)
                    // save the state for further event handling
                    if (isConfirmed) viewHolderState = ViewHolderState.NORMAL
                }
                // from normal to selected state (selection)
                ViewHolderState.NORMAL -> {
                    // change the chip state from adapter
                    clickListener.onVariantTypeSelected(adapterPosition)
                    // save the state for further event handling
                    viewHolderState = ViewHolderState.SELECTED
                }
                ViewHolderState.DISABLED ->
                    itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_DISABLE
            }
        }
    }

    fun bindData(variantDetail: VariantDetail, state: ViewHolderState) {
        itemView.chipsVariantTypeName.chip_text.text = variantDetail.name
        viewHolderState = state
        when (state) {
            ViewHolderState.SELECTED ->
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_SELECTED
            ViewHolderState.NORMAL ->
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_NORMAL
            ViewHolderState.DISABLED ->
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_DISABLE
        }
    }
}