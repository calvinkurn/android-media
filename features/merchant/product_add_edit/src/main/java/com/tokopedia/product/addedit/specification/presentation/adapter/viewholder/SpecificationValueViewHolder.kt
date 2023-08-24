package com.tokopedia.product.addedit.specification.presentation.adapter.viewholder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.unifycomponents.TextAreaUnify

class SpecificationValueViewHolder(
    itemView: View,
    onSpecificationClickListener: OnSpecificationValueViewHolderClickListener
) : RecyclerView.ViewHolder(itemView) {

    interface OnSpecificationValueViewHolderClickListener {
        fun onSpecificationValueTextClicked(position: Int)
        fun onSpecificationValueTextCleared(position: Int)
        fun onTooltipRequiredClicked()
    }

    private val tfSpecification: TextAreaUnify? = itemView.findViewById(R.id.tfSpecification)
    private val tooltipRequired: View? = itemView.findViewById(R.id.tooltipRequired)

    init {
        tfSpecification?.textAreaInput?.setOnClickListener {
            onSpecificationClickListener.onSpecificationValueTextClicked(adapterPosition)
        }
        tfSpecification?.textAreaInput?.doOnTextChanged { text, _, count, _ ->
            if (count > Int.ZERO && text?.isBlank() == true)
                onSpecificationClickListener.onSpecificationValueTextCleared(adapterPosition)
        }
        tooltipRequired?.setOnClickListener {
            onSpecificationClickListener.onTooltipRequiredClicked()
        }
    }

    fun bindData(title: String, selectedSpecification: SpecificationInputModel) {
        tfSpecification?.apply {
            val errorMessage = if (selectedSpecification.errorMessageRes.isZero()) ""
                               else context.getString(selectedSpecification.errorMessageRes)
            val suffix = if (selectedSpecification.required)
                context.getString(R.string.label_asterisk) else ""
            textAreaInput.keyListener = null // disable editing text
            textAreaInput.isFocusable = false
            textAreaInput.isClickable = true
            textAreaLabel = title + suffix
            textAreaMessage = errorMessage
            isError = errorMessage.isNotEmpty()
            setText(selectedSpecification.data)
            setTextInputLayoutHintColor(textAreaWrapper, itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)
            tooltipRequired?.isVisible = selectedSpecification.required
        }
    }

    private fun setTextInputLayoutHintColor(textInputLayout: TextInputLayout, context: Context, @ColorRes colorIdRes: Int) {
        textInputLayout.defaultHintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, colorIdRes))
    }
}