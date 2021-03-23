package com.tokopedia.product.addedit.specification.presentation.adapter.viewholder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
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
        setTextInputLayoutHintColor(itemView.tfSpecification.textAreaWrapper, itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
    }

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }
}