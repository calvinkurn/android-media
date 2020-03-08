package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.promo.presentation.listener.PromoCheckoutActionListener
import com.tokopedia.purchase_platform.features.promo.presentation.uimodel.PromoInputUiModel
import kotlinx.android.synthetic.main.item_promo_input.view.*

class PromoInputViewHolder(private val view: View,
                           private val listener: PromoCheckoutActionListener
) : AbstractViewHolder<PromoInputUiModel>(view) {

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
            itemView.button_apply_promo.isLoading = true
            itemView.button_apply_promo.isClickable = false
        } else {
            itemView.button_apply_promo.isLoading = false
            itemView.button_apply_promo.isClickable = true
        }

        if (element.uiData.promoCode.isNotBlank()) {
            itemView.button_apply_promo.isEnabled = true
            itemView.text_field_input_promo.setFirstIcon(R.drawable.unify_chips_ic_close)
        } else {
            itemView.button_apply_promo.isEnabled = false
            itemView.text_field_input_promo.setFirstIcon(R.color.white)
        }

        itemView.text_field_input_promo.textFieldInput.setText(element.uiData.promoCode)
        itemView.text_field_input_promo.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                element.uiData.promoCode = text.toString()
                if (text?.isNotEmpty() == true) {
                    element.uiState.isButtonSelectEnabled = true
                    itemView.button_apply_promo.isEnabled = true
                    itemView.text_field_input_promo.setFirstIcon(R.drawable.unify_chips_ic_close)
                } else {
                    element.uiState.isButtonSelectEnabled = false
                    itemView.button_apply_promo.isEnabled = false
                    itemView.text_field_input_promo.setFirstIcon(R.color.white)
                }
            }
        })

        itemView.text_field_input_promo.getFirstIcon().setOnClickListener {
            itemView.text_field_input_promo.textFieldInput.text.clear()
        }

        itemView.button_apply_promo.setOnClickListener {
            val promoCode = itemView.text_field_input_promo.textFieldInput.text.toString()
            if (promoCode.isNotEmpty()) {
                listener.onClickApplyManualInputPromo(promoCode)
            }
        }
    }

    private fun renderPromoInputError(element: PromoInputUiModel) {
        itemView.text_field_input_promo.setError(true)
        if (element.uiData.exception != null) {
            itemView.text_field_input_promo.setMessage(ErrorHandler.getErrorMessage(itemView.context, element.uiData.exception))
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
        itemView.text_field_input_promo.setError(false)
        itemView.text_field_input_promo.setMessage("")
        setPaddingViewHasNoError()
    }

}