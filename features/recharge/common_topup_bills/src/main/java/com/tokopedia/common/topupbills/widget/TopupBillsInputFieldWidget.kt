package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.tokopedia.common.topupbills.R
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_topup_bills_input_field.view.*
import kotlinx.coroutines.*
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 20/08/19.
 */
open class TopupBillsInputFieldWidget @JvmOverloads constructor(@NotNull context: Context,
                                                                attrs: AttributeSet? = null,
                                                                defStyleAttr: Int = 0,
                                                                var actionListener: ActionListener? = null)
    : FrameLayout(context, attrs, defStyleAttr) {

    var isCustomInput = false
        set(value) {
            field = value
            toggleDropdownIcon(value)
        }

    var infoListener: InfoListener? = null
        set(value) {
            field = value
            if (value != null) input_info.show() else input_info.hide()
        }

    private var finishInputJob: Job? = null
    private var delayTextChanged: Long = DEFAULT_DELAY_TEXT_CHANGED_MILLIS

    init {
        View.inflate(context, getLayout(), this)

        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopupBillsInputFieldWidget, 0, 0)
            try {
                isCustomInput = styledAttributes.getBoolean(R.styleable.TopupBillsInputFieldWidget_isDropdown, false)
            } finally {
                styledAttributes.recycle()
            }
        }

        ac_input.clearFocus()

        btn_clear_input.setOnClickListener {
            ac_input.setText("")
            actionListener?.onFinishInput("")
            error_label.visibility = View.GONE
        }

        getTextWatcher()?.run { ac_input.addTextChangedListener(this) }

        ac_input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                actionListener?.onFinishInput(getInputText())
                ac_input.clearFocus()
            }
            false
        }
        ac_input.keyImeChangeListener = object : TopupBillsBackEditText.KeyImeChange {
            override fun onKeyIme(keyCode: Int, event: KeyEvent) {
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    actionListener?.onFinishInput(getInputText())
                    ac_input.clearFocus()
                }
            }
        }
        ac_input.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (isCustomInput) {
                    actionListener?.onCustomInputClick()
                    return@setOnTouchListener true
                }
            }
            false
        }

        input_info.setOnClickListener {
            infoListener?.onInfoClick()
        }
        // Enlarge info button touch area with TouchDelegate
        input_field_container.post {
            val delegateArea = Rect()
            input_info.getHitRect(delegateArea)

            delegateArea.top -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.left -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.bottom += INFO_TOUCH_AREA_SIZE_PX
            delegateArea.right += INFO_TOUCH_AREA_SIZE_PX

            input_field_container.apply { touchDelegate = TouchDelegate(delegateArea, input_info) }
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
        if (triggerListener) actionListener?.onFinishInput(input)
    }

    fun setErrorMessage(message: String) {
        error_label.text = message
        error_label.visibility = View.VISIBLE
    }

    fun hideErrorMessage() {
        error_label.text = ""
        error_label.visibility = View.GONE
    }

    fun toggleDropdownIcon(value: Boolean) {
        if (value) iv_input_dropdown.show() else iv_input_dropdown.hide()
    }

    protected fun getLayout(): Int {
        return R.layout.view_topup_bills_input_field
    }

    protected fun getTextWatcher(): TextWatcher? {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                finishInputJob?.cancel()
                finishInputJob = CoroutineScope(Dispatchers.Main).launch {
                    delay(delayTextChanged)
                    actionListener?.onFinishInput(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty() || isCustomInput) {
                    btn_clear_input.visibility = View.GONE
                } else {
                    if (count > 1) {
                        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(ac_input, InputMethod.SHOW_FORCED)
                    }
                    btn_clear_input.visibility = View.VISIBLE
                    actionListener?.onTextChangeInput()
                }
            }
        }
    }

    fun setDelayTextChanged(delay: Long) {
        delayTextChanged = delay
    }

    fun setInputType(type: String) {
        ac_input.inputType = when (type) {
            INPUT_NUMERIC -> InputType.TYPE_CLASS_NUMBER
            INPUT_ALPHANUMERIC -> InputType.TYPE_CLASS_TEXT
            INPUT_TELCO -> InputType.TYPE_CLASS_PHONE
            else -> InputType.TYPE_CLASS_NUMBER
        }
    }

    fun resetState() {
        isCustomInput = false
        ac_input.setText("")
        hideErrorMessage()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        finishInputJob?.cancel()
    }

    interface ActionListener {
        fun onFinishInput(input: String)
        fun onTextChangeInput()
        fun onCustomInputClick()
    }

    interface InfoListener {
        fun onInfoClick()
    }

    companion object {
        const val INPUT_ALPHANUMERIC = "input_alpanumeric"
        const val INPUT_NUMERIC = "input_numeric"
        const val INPUT_TELCO = "input_tel"

        const val INFO_TOUCH_AREA_SIZE_PX = 20
        const val DEFAULT_DELAY_TEXT_CHANGED_MILLIS: Long = 300
    }
}
