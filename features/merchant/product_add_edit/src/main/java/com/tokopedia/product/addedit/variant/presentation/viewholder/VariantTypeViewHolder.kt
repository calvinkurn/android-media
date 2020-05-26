package com.tokopedia.product.addedit.variant.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.variant.data.model.VariantDetail
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.item_variant_type.view.*

class VariantTypeViewHolder(itemView: View, clickListener: OnVariantTypeViewHolderClickListener)
    : RecyclerView.ViewHolder(itemView) {

    interface OnVariantTypeViewHolderClickListener {
        fun onVariantTypeClicked(position: Int, state: ViewHolderState) {

        }
    }

    enum class ViewHolderState {
        SELECTED,
        NORMAL,
        DISABLED
    }

    var viewHolderState = ViewHolderState.NORMAL

    init {
        itemView.chipsVariantTypeName.setOnClickListener {
            when(viewHolderState) {
                ViewHolderState.SELECTED -> {
                    itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_NORMAL
                    viewHolderState = ViewHolderState.NORMAL
                    clickListener.onVariantTypeClicked(adapterPosition, viewHolderState)
                }
                ViewHolderState.NORMAL -> {
                    itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_SELECTED
                    viewHolderState = ViewHolderState.SELECTED
                    clickListener.onVariantTypeClicked(adapterPosition, viewHolderState)
                }
                ViewHolderState.DISABLED ->
                    itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_DISABLE
            }
        }
    }

    fun bindData(variantDetail: VariantDetail, state: ViewHolderState) {
        itemView.chipsVariantTypeName.chip_text.text = variantDetail.name
        viewHolderState = state
        when(state) {
            ViewHolderState.SELECTED ->
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_SELECTED
            ViewHolderState.NORMAL ->
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_NORMAL
            ViewHolderState.DISABLED ->
                itemView.chipsVariantTypeName.chipType = ChipsUnify.TYPE_DISABLE
        }
    }
}