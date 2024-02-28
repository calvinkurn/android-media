package com.tokopedia.product.addedit.specification.presentation.adapter.viewholder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.setText
import com.tokopedia.product.addedit.specification.presentation.model.SpecificationInputModel
import com.tokopedia.unifycomponents.TextAreaUnify
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class SpecificationValueViewHolder(
    itemView: View,
    onSpecificationClickListener: OnSpecificationValueViewHolderClickListener
) : RecyclerView.ViewHolder(itemView) {

    interface OnSpecificationValueViewHolderClickListener {
        fun onSpecificationValueTextClicked(position: Int)
        fun onSpecificationValueTextCleared(position: Int)
        fun onTooltipRequiredClicked()
    }

    private val tfSpecification: TextAreaUnify2? = itemView.findViewById(R.id.tfSpecification)
    private val tooltipRequired: View? = itemView.findViewById(R.id.tooltipRequired)
    private var selectedSpecification: SpecificationInputModel = SpecificationInputModel()

    init {
        val iconColor = MethodChecker.getColor(itemView.context, unifyprinciplesR.color.Unify_NN900)
        val iconDrawable = getIconUnifyDrawable(itemView.context, IconUnify.CHEVRON_DOWN, iconColor)
        tfSpecification?.icon2?.setImageDrawable(iconDrawable)
        tfSpecification?.icon2?.setOnClickListener {
            onSpecificationClickListener.onSpecificationValueTextClicked(bindingAdapterPosition)
        }
        tfSpecification?.editText?.setOnClickListener {
            onSpecificationClickListener.onSpecificationValueTextClicked(bindingAdapterPosition)
        }
        tfSpecification?.editText?.doOnTextChanged { text, _, count, _ ->
            selectedSpecification.data = text.toString()
            if (count > Int.ZERO && text?.isBlank() == true)
                onSpecificationClickListener.onSpecificationValueTextCleared(bindingAdapterPosition)
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
            val isTextInput = selectedSpecification.isTextInput

            editText.isFocusable = isTextInput
            editText.isFocusableInTouchMode = isTextInput
            editText.isClickable = !isTextInput
            icon2.isVisible = !isTextInput && selectedSpecification.data.isEmpty()
            setLabel(title + suffix)
            setMessage(errorMessage)
            isInputError = errorMessage.isNotEmpty()
            setText(selectedSpecification.data)
            tooltipRequired?.isVisible = selectedSpecification.required
        }
        this.selectedSpecification = selectedSpecification
    }
}
