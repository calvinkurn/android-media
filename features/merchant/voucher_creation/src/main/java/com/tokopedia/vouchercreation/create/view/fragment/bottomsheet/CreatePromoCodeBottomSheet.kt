package com.tokopedia.vouchercreation.create.view.fragment.bottomsheet

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.DialogFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.create.view.customview.bottomsheet.VoucherBottomView
import kotlinx.android.synthetic.main.mvc_create_promo_code_bottom_sheet_view.*


class CreatePromoCodeBottomSheet(bottomSheetContext: Context?,
                                 val onNextClick: (String) -> Unit = {},
                                 val getPromoCode: () -> String) : BottomSheetUnify(), VoucherBottomView {

    companion object {
        private val TEXFIELD_ALERT_MINIMUM = R.string.mvc_create_alert_minimum

        private const val MIN_TEXTFIELD_LENGTH = 5
        private const val MAX_TEXTFIELD_LENGTH = 10

        fun createInstance(context: Context?,
                           onNextClick: (String) -> Unit,
                           getPromoCode: () -> String = {""}) : CreatePromoCodeBottomSheet {
            return CreatePromoCodeBottomSheet(context, onNextClick, getPromoCode).apply {
                context?.run {
                    val view = View.inflate(this, R.layout.mvc_create_promo_code_bottom_sheet_view, null)
                    setChild(view)
                    setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                }
            }
        }
    }

    private var alertMinimumMessage = bottomSheetContext?.resources?.getString(TEXFIELD_ALERT_MINIMUM).toBlankOrString()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPromoCodeTextField?.run {
            textFieldInput.run {
                filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(MAX_TEXTFIELD_LENGTH))
                setOnFocusChangeListener { _, hasFocus ->
                    activity?.let {
                        if (hasFocus) {
                            KeyboardHandler.showSoftKeyboard(it)
                        } else {
                            KeyboardHandler.hideSoftKeyboard(it)
                        }
                    }
                }
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        //No op
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        //No op
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        when {
                            s.isEmpty() -> {
                                setError(false)
                                setMessage("")
                            }
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
                requestFocus()
            }

            createPromoCodeSaveButton?.setOnClickListener {
                onNextClick(createPromoCodeTextField?.textFieldInput?.text?.toString().toBlankOrString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        createPromoCodeTextField?.textFieldInput?.run {
            setText(getPromoCode())
            selectAll()
        }
    }

    override val title: String? = bottomSheetContext?.resources?.getString(R.string.mvc_create_target_create_promo_code_bottomsheet_title)

}