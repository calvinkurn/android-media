package com.tokopedia.vouchercreation.create.view.viewholder

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.uimodel.FillVoucherNameUiModel
import kotlinx.android.synthetic.main.mvc_fill_voucher_name_widget.view.*

class FillVoucherNameViewHolder(itemView: View) : AbstractViewHolder<FillVoucherNameUiModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.mvc_fill_voucher_name_widget
        val TEXFIELD_ALERT_MINIMUM = R.string.mvc_create_alert_minimum

        private const val MIN_TEXTFIELD_LENGTH = 5
    }

    private var alertMinimumMessage = itemView.resources?.getString(TEXFIELD_ALERT_MINIMUM).toBlankOrString()

    override fun bind(element: FillVoucherNameUiModel?) {
        itemView.fillVoucherNameTextfield?.run {
            textFieldInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    //No op
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //No op
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    when {
                        s.length < MIN_TEXTFIELD_LENGTH -> {
                            setError(true)
                            setMessage(alertMinimumMessage)
                        }
                        else -> {
                            setError(false)
                            setMessage("")
                        }
                    }
                }
            })
        }
    }
}