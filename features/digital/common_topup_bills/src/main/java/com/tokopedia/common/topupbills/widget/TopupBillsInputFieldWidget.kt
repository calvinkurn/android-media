package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.view.model.TopupBillsInputDropdownData
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.view_voucher_game_input_field.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 20/08/19.
 */
class TopupBillsInputFieldWidget @JvmOverloads constructor(@NotNull context: Context,
                                                           attrs: AttributeSet? = null,
                                                           defStyleAttr: Int = 0,
                                                           var listener: ActionListener? = null)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var isCustomInput = false
        set(value) {
            field = value
            if (value) iv_input_dropdown.show() else iv_input_dropdown.hide()
        }

    init {
        View.inflate(context, getLayout(), this)

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.TopupBillsInputFieldWidget, 0, 0)
            try {
                isCustomInput = ta.getBoolean(R.styleable.TopupBillsInputFieldWidget_isDropdown, false)
            } finally {
                ta.recycle()
            }
        }

        ac_input.clearFocus()

        btn_clear_input.setOnClickListener {
            ac_input.setText("")
            listener?.onFinishInput("")
            error_label.visibility = View.GONE
        }

        ac_input.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0 || isCustomInput) {
                    btn_clear_input.visibility = View.GONE
                } else {
                    if (count > 1) {
                        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(ac_input, InputMethod.SHOW_FORCED)
                    }
                    btn_clear_input.visibility = View.VISIBLE
                }
            }

        })

        ac_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                listener?.onFinishInput(getInputText())
                clearFocus()
            }
            false
        }
        ac_input.setKeyImeChangeListener { _, event ->
            if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                listener?.onFinishInput(getInputText())
                clearFocus()
            }
        }
        ac_input.setOnFocusChangeListener { _, b ->
            onFocusChangeDropdown(b)
        }
    }

    fun setLabel(label: String) {
        input_label.text = label
    }

    fun setHint(hint: String) {
        ac_input.hint = hint
    }

    fun getInputText(): String {
        return ac_input.text.toString()
    }

    fun setInputText(input: String, triggerListener: Boolean = true) {
        ac_input.setText(input)
        if (triggerListener) listener?.onFinishInput(input)
    }

    fun setErrorMessage(message: String) {
        error_label.text = message
        error_label.visibility = View.VISIBLE
    }

    fun hideErrorMessage() {
        error_label.text = ""
        error_label.visibility = View.GONE
    }

    open fun getLayout(): Int {
        return R.layout.view_voucher_game_input_field
    }

    fun setActionListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setInputType(type: String) {
        ac_input.inputType = when (type) {
            INPUT_NUMERIC ->  InputType.TYPE_CLASS_NUMBER
            INPUT_ALPHANUMERIC ->  InputType.TYPE_CLASS_TEXT
            else -> InputType.TYPE_CLASS_NUMBER
        }
    }

    fun resetState() {
        isCustomInput = false
        ac_input.setText("")
        hideErrorMessage()
    }

    private fun onFocusChangeDropdown(hasFocus: Boolean) {
        if (hasFocus && isCustomInput) {
            ac_input.clearFocus()
            listener?.onCustomInputClick()
        }
    }

    interface ActionListener {
        fun onFinishInput(input: String)
        fun onCustomInputClick()
    }

    companion object {
        const val INPUT_NUMERIC = "input_numeric"
        const val INPUT_ALPHANUMERIC = "input_alphanumeric"
    }
}
