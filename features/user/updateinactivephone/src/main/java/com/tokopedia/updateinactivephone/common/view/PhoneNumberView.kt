package com.tokopedia.updateinactivephone.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.FrameLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.updateinactivephone.R

class PhoneNumberView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var textField: EditText
    private var textMessage: Typography
    private var textLabel: Typography
    private var line: View

    var text: String
        get() {
            val text = textField.text.toString()
            return if (text.isEmpty()) {
                text
            } else {
                "0${textField.text}"
            }
        }
        set(value) {
            textField.setText(value)
        }

    var message: String
        get() = textMessage.text.toString()
        set(value) {
            textMessage.text = value
            textMessage.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N300))
            line.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N300))
        }

    var error: String
        get() = textMessage.text.toString()
        set(value) {
            textMessage.text = value
            textMessage.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
            line.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_R500))
        }

    var label: String
        get() = textLabel.text.toString()
        set(value) {
            textLabel.text = value
        }

    init {
        val layout: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layout.inflate(R.layout.view_phone_number, this, true)

        textField = view.findViewById(R.id.textPhoneNumberView)
        textMessage = view.findViewById(R.id.messagePhoneNumberView)
        textLabel = view.findViewById(R.id.labelPhoneNumberView)
        line = view.findViewById(R.id.linePhoneNumberView)

        if (attrs != null) {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.PhoneNumberView, 0, 0)
            typedArray?.let {
                val labelAttr = it.getString(R.styleable.PhoneNumberView_label) ?: ""
                val messageAttr = it.getString(R.styleable.PhoneNumberView_message) ?: ""

                label = labelAttr
                message = messageAttr

                it.recycle()
            }
        }

        textField.onFocusChangeListener = focusListener()
        textField.afterTextChanged {
            if (it.isNotEmpty() && it.first().toString() == "0") {
                textField.setText(it.drop(1))
            }
        }
    }

    private fun focusListener(): OnFocusChangeListener {
        return OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                line.setBackgroundColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
            } else {
                line.setBackgroundColor(MethodChecker.getColor(context, R.color.updateinactivephone_dms_dark_n500))
            }
        }
    }
}