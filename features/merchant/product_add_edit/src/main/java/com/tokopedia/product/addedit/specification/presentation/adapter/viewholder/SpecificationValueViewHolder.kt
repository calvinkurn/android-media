package com.tokopedia.product.addedit.specification.presentation.adapter.viewholder

import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.common.util.setText
import kotlinx.android.synthetic.main.item_specification_value.view.*

class SpecificationValueViewHolder(itemView: View, onSpecificationClickListener: OnSpecificationValueViewHolderClickListener) : RecyclerView.ViewHolder(itemView) {

    interface OnSpecificationValueViewHolderClickListener {
        fun onSpecificationValueTextClicked(position: Int)
        fun onSpecificationValueTextCleared(position: Int)
    }

    init {
        itemView.tfSpecification.textAreaInput.setOnClickListener {
            onSpecificationClickListener.onSpecificationValueTextClicked(adapterPosition)
        }
        itemView.tfSpecification.textAreaInput.doOnTextChanged { text, _, count, _ ->
            if (count > 0 && text?.isBlank() == true)
                onSpecificationClickListener.onSpecificationValueTextCleared(adapterPosition)
        }
    }

    fun bindData(title: String, value: String) {
        itemView.tfSpecification.textAreaInput.keyListener = null // disable editing
        itemView.tfSpecification.textAreaInput.isFocusable = false
        itemView.tfSpecification.textAreaInput.isClickable = true
        itemView.tfSpecification.textAreaLabel = title
        itemView.tfSpecification.setText(value)
    }
}