package com.tokopedia.shopdiscount.subsidy.presentation.adapter.viewholder

import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.LayoutItemShopDiscountOptOutReasonBinding
import com.tokopedia.shopdiscount.subsidy.model.uimodel.ShopDiscountOptOutReasonUiModel
import com.tokopedia.unifycomponents.TextAreaUnify2
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify

class ShopDiscountOptOutOptionItemViewHolder(
    private val viewBinding: LayoutItemShopDiscountOptOutReasonBinding,
    private val listener: Listener
) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_item_shop_discount_opt_out_reason
        private const val MINIMUM_CUSTOM_REASON_LENGTH = 20
        private const val MAXIMUM_CUSTOM_REASON_LENGTH = 200
        private const val TEXT_AREA_LINE = 3
    }

    interface Listener {
        fun onReasonSelected(uiModel: ShopDiscountOptOutReasonUiModel, isSelected: Boolean)
        fun onCustomReasonChanged(
            uiModel: ShopDiscountOptOutReasonUiModel,
            customReason: String,
            inputError: Boolean
        )
    }

    private val radioButtonReason: RadioButtonUnify = viewBinding.radioButtonReason
    private val textAreaCustomReason: TextAreaUnify2 = viewBinding.textareaReasonCustom
    private var uiModel: ShopDiscountOptOutReasonUiModel? = null
    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            uiModel?.let {
                val customReason = s.toString()
                if (customReason.length < MINIMUM_CUSTOM_REASON_LENGTH && customReason.isNotEmpty()) {
                    textAreaCustomReason.isInputError = true
                    textAreaCustomReason.setMessage(
                        viewBinding.root.context.getString(
                            R.string.sd_subsidy_opt_out_reason_bottom_sheet_other_reason_minimum_character_format,
                            MINIMUM_CUSTOM_REASON_LENGTH
                        )
                    )
                } else {
                    textAreaCustomReason.isInputError = false
                    textAreaCustomReason.setMessage("")
                }
                listener.onCustomReasonChanged(it, s.toString(), textAreaCustomReason.isInputError)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun bind(uiModel: ShopDiscountOptOutReasonUiModel) {
        this.uiModel = uiModel
        setupRadioButton(uiModel)
        setupTextArea(uiModel)
        setItemClickListener(uiModel)
    }

    private fun setItemClickListener(uiModel: ShopDiscountOptOutReasonUiModel) {
        viewBinding.root.setOnClickListener {
            if (radioButtonReason.isChecked) return@setOnClickListener
            radioButtonReason.isChecked = true
            listener.onReasonSelected(uiModel, true)
        }
    }

    private fun setupTextArea(uiModel: ShopDiscountOptOutReasonUiModel) {
        textAreaCustomReason.apply {
            if (!uiModel.isReasonFromResponse && uiModel.isSelected) {
                editText.addTextChangedListener(textWatcher)
                if (uiModel.reason.isNotEmpty()) {
                    editText.setText(uiModel.reason)
                }
                setCounter(MAXIMUM_CUSTOM_REASON_LENGTH)
                minLine = TEXT_AREA_LINE
                maxLine = TEXT_AREA_LINE
                show()
            } else {
                editText.clearFocus()
                editText.removeTextChangedListener(textWatcher)
                hideKeyboard()
                hide()
            }
        }
    }

    private fun setupRadioButton(uiModel: ShopDiscountOptOutReasonUiModel) {
        radioButtonReason.apply {
            text = if (!uiModel.isReasonFromResponse) {
                viewBinding.root.context.getString(R.string.sd_subsidy_opt_out_reason_bottom_sheet_other_reason)
            } else {
                uiModel.reason
            }
            isChecked = uiModel.isSelected
        }
    }

}
