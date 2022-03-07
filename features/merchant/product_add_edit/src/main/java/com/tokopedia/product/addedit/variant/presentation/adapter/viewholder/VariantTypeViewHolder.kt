package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.unifycomponents.ChipsUnify

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

    private val chipsVariantTypeName: ChipsUnify? = itemView.findViewById(R.id.chipsVariantTypeName)
    private var viewHolderState = ViewHolderState.NORMAL

    init {
        chipsVariantTypeName?.setOnClickListener {
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
                    chipsVariantTypeName.chipType = ChipsUnify.TYPE_DISABLE
            }
        }
    }

    fun bindData(variantDetail: VariantDetail, state: ViewHolderState) {
        chipsVariantTypeName?.chip_text?.text = variantDetail.name
        viewHolderState = state
        when (state) {
            ViewHolderState.SELECTED ->
                chipsVariantTypeName?.chipType = ChipsUnify.TYPE_SELECTED
            ViewHolderState.NORMAL ->
                chipsVariantTypeName?.chipType = ChipsUnify.TYPE_NORMAL
            ViewHolderState.DISABLED ->
                chipsVariantTypeName?.chipType = ChipsUnify.TYPE_DISABLE
        }
    }
}