package com.tokopedia.purchase_platform.features.promo.presentation.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
        itemView.text_field_input_promo.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                itemView.button_apply_promo.isEnabled = text?.length ?: 0 > 0
            }
        })

        itemView.button_apply_promo.setOnClickListener {
            val promoCode = itemView.text_field_input_promo.textFieldInput.text.toString()
            if (promoCode.isNotEmpty()) {
                listener.onClickApplyManualInputPromo(promoCode)
            }
        }
    }


}