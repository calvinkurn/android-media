package com.tokopedia.promocheckoutmarketplace.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.promocheckoutmarketplace.R
import com.tokopedia.promocheckoutmarketplace.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.promocheckoutmarketplace.presentation.uimodel.PromoInputUiModel
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.UnifyButton

class PromoInputViewHolder(private val view: View,
                           private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoInputUiModel>(view) {

    private val buttonApplyPromo by lazy {
        view.findViewById<UnifyButton>(R.id.button_apply_promo)
    }
    private val textFieldInputPromo by lazy {
        view.findViewById<TextFieldUnify>(R.id.text_field_input_promo)
    }

    companion object {
        val LAYOUT = R.layout.promo_checkout_marketplace_module_item_promo_input
    }

    override fun bind(element: PromoInputUiModel) {
        if (element.uiState.isError) {
            renderPromoInputError(element)
        } else {
            renderPromoInputNotError(element)
        }

        if (element.uiState.isLoading) {
            buttonApplyPromo.isLoading = true
            buttonApplyPromo.isClickable = false
        } else {
            buttonApplyPromo.isLoading = false
            buttonApplyPromo.isClickable = true
        }

        if (element.uiData.promoCode.isNotBlank()) {
            buttonApplyPromo.isEnabled = true
            textFieldInputPromo.setFirstIcon(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_close)
        } else {
            buttonApplyPromo.isEnabled = false
            textFieldInputPromo.setFirstIcon(com.tokopedia.unifyprinciples.R.color.Unify_N0)
        }

        textFieldInputPromo.textFieldInput.setOnClickListener {
            listener.onClickPromoManualInputTextField()
        }
        textFieldInputPromo.textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
        textFieldInputPromo.textFieldInput.setText(element.uiData.promoCode)
        textFieldInputPromo.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (element.uiState.isValidLastSeenPromo) {
                    element.uiState.isValidLastSeenPromo = text.toString() == element.uiData.validLastSeenPromoCode
                }
                element.uiData.promoCode = text?.toString() ?: ""
                if (text?.isNotEmpty() == true) {
                    element.uiState.isButtonSelectEnabled = true
                    buttonApplyPromo.isEnabled = true
                    textFieldInputPromo.setFirstIcon(com.tokopedia.unifycomponents.R.drawable.unify_chips_ic_close)
                } else {
                    element.uiState.isButtonSelectEnabled = false
                    buttonApplyPromo.isEnabled = false
                    textFieldInputPromo.setFirstIcon(com.tokopedia.unifyprinciples.R.color.Unify_N0)
                }
            }
        })

        textFieldInputPromo.getFirstIcon().setOnClickListener {
            textFieldInputPromo.textFieldInput.text.clear()
            listener.onCLickClearManualInputPromo()
        }

        buttonApplyPromo.setOnClickListener {
            val promoCode = textFieldInputPromo.textFieldInput.text.toString()
            if (promoCode.isNotEmpty()) {
                listener.onClickApplyManualInputPromo(promoCode, element.uiState.isValidLastSeenPromo)
            }
        }

        element.uiState.viewHeight = itemView.height
    }

    private fun renderPromoInputError(element: PromoInputUiModel) {
        textFieldInputPromo.setError(true)
        if (element.uiData.exception != null) {
            textFieldInputPromo.setMessage(element.uiData.exception?.message
                    ?: "Terjadi kesalahan. Ulangi beberapa saat lagi")
            setPaddingViewHasError()
        } else {
            setPaddingViewHasNoError()
        }
    }

    private fun setPaddingViewHasNoError() {
        view.setPadding(
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_0)?.toInt() ?: 0
        )
    }

    private fun setPaddingViewHasError() {
        view.setPadding(
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(com.tokopedia.abstraction.R.dimen.dp_16)?.toInt() ?: 0
        )
    }

    private fun renderPromoInputNotError(element: PromoInputUiModel) {
        textFieldInputPromo.setError(false)
        textFieldInputPromo.setMessage("")
        setPaddingViewHasNoError()
    }

}