package com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertarget.widgets

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.MvcFillVoucherNameWidgetBinding
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertarget.widgets.FillVoucherNameUiModel

class FillVoucherNameViewHolder(itemView: View) : AbstractViewHolder<FillVoucherNameUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.mvc_fill_voucher_name_widget
        @StringRes
        val TEXFIELD_ALERT_MINIMUM = R.string.mvc_create_alert_minimum

        private const val MIN_TEXTFIELD_LENGTH = 5
    }

    private var binding: MvcFillVoucherNameWidgetBinding? by viewBinding()

    private var alertMinimumMessage = itemView.resources?.getString(TEXFIELD_ALERT_MINIMUM).toBlankOrString()

    override fun bind(element: FillVoucherNameUiModel?) {
        binding?.fillVoucherNameTextfield?.run {
            textFieldInput.imeOptions = EditorInfo.IME_ACTION_DONE
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