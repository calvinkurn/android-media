package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoInputUiModel
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
        val LAYOUT = R.layout.item_promo_input
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
            textFieldInputPromo.setFirstIcon(R.drawable.unify_chips_ic_close)
        } else {
            buttonApplyPromo.isEnabled = false
            textFieldInputPromo.setFirstIcon(R.color.white)
        }

        textFieldInputPromo.textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
        textFieldInputPromo.textFieldInput.setText(element.uiData.promoCode)
        textFieldInputPromo.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                element.uiData.promoCode = text.toString()
                if (text?.isNotEmpty() == true) {
                    element.uiState.isButtonSelectEnabled = true
                    buttonApplyPromo.isEnabled = true
                    textFieldInputPromo.setFirstIcon(R.drawable.unify_chips_ic_close)
                } else {
                    element.uiState.isButtonSelectEnabled = false
                    buttonApplyPromo.isEnabled = false
                    textFieldInputPromo.setFirstIcon(R.color.white)
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
                listener.onClickApplyManualInputPromo(promoCode)
            }
        }
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
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(R.dimen.dp_0)?.toInt() ?: 0
        )
    }

    private fun setPaddingViewHasError() {
        view.setPadding(
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0,
                itemView.context?.resources?.getDimension(R.dimen.dp_16)?.toInt() ?: 0
        )
    }

    private fun renderPromoInputNotError(element: PromoInputUiModel) {
        textFieldInputPromo.setError(false)
        textFieldInputPromo.setMessage("")
        setPaddingViewHasNoError()
    }

}