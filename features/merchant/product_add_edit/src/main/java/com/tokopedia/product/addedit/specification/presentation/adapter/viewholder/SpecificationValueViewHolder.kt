package com.tokopedia.product.addedit.specification.presentation.adapter.viewholder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.unifycomponents.TextAreaUnify

class SpecificationValueViewHolder(
    itemView: View,
    onSpecificationClickListener: OnSpecificationValueViewHolderClickListener
) : RecyclerView.ViewHolder(itemView) {

    interface OnSpecificationValueViewHolderClickListener {
        fun onSpecificationValueTextClicked(position: Int)
        fun onSpecificationValueTextCleared(position: Int)
    }

    private val tfSpecification: TextAreaUnify? = itemView.findViewById(R.id.tfSpecification)

    init {
        tfSpecification?.textAreaInput?.setOnClickListener {
            onSpecificationClickListener.onSpecificationValueTextClicked(adapterPosition)
        }
        tfSpecification?.textAreaInput?.doOnTextChanged { text, _, count, _ ->
            if (count > 0 && text?.isBlank() == true)
                onSpecificationClickListener.onSpecificationValueTextCleared(adapterPosition)
        }
    }

    fun bindData(title: String, value: String) {
        tfSpecification?.apply {
            textAreaInput.keyListener = null // disable editing
            textAreaInput.isFocusable = false
            textAreaInput.isClickable = true
            textAreaLabel = title
            setText(value)
            setTextInputLayoutHintColor(textAreaWrapper, itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
        }
    }

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }
}