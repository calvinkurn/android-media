package com.tokopedia.common.topupbills.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethod
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.databinding.ViewTopupBillsInputFieldBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.view_topup_bills_input_field.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull

/**
 * Created by resakemal on 20/08/19.
 */
@SuppressLint("ClickableViewAccessibility")
class TopupBillsInputFieldWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var actionListener: ActionListener? = null
) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = ViewTopupBillsInputFieldBinding.inflate(LayoutInflater.from(context), this, true)

    var isCustomInput = false
        set(value) {
            field = value
            toggleDropdownIcon(value)
        }

    var infoListener: InfoListener? = null
        set(value) {
            field = value
            if (value != null) binding.inputInfo.show() else binding.inputInfo.hide()
        }

    private var finishInputJob: Job? = null
    private var delayTextChanged: Long = DEFAULT_DELAY_TEXT_CHANGED_MILLIS

    init {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TopupBillsInputFieldWidget, 0, 0)
            try {
                isCustomInput = styledAttributes.getBoolean(R.styleable.TopupBillsInputFieldWidget_isDropdown, false)
            } finally {
                styledAttributes.recycle()
            }
        }

        binding.acInput.clearFocus()

        binding.btnClearInput.setOnClickListener {
            binding.acInput.setText("")
            actionListener?.onFinishInput("")
            binding.errorLabel.visibility = View.GONE
        }

        getTextWatcher()?.run { binding.acInput.addTextChangedListener(this) }

        binding.acInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                actionListener?.onFinishInput(getInputText())
                binding.acInput.clearFocus()
            }
            false
        }
        binding.acInput.keyImeChangeListener = object : TopupBillsBackEditText.KeyImeChange {
            override fun onKeyIme(keyCode: Int, event: KeyEvent) {
                if (event.keyCode == KeyEvent.KEYCODE_BACK) {
                    actionListener?.onFinishInput(getInputText())
                    binding.acInput.clearFocus()
                }
            }
        }

        binding.acInput.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                if (isCustomInput) {
                    actionListener?.onCustomInputClick()
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.inputInfo.setOnClickListener {
            infoListener?.onInfoClick()
        }
        // Enlarge info button touch area with TouchDelegate
        binding.inputFieldContainer.post {
            val delegateArea = Rect()
            binding.inputInfo.getHitRect(delegateArea)

            delegateArea.top -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.left -= INFO_TOUCH_AREA_SIZE_PX
            delegateArea.bottom += INFO_TOUCH_AREA_SIZE_PX
            delegateArea.right += INFO_TOUCH_AREA_SIZE_PX

            binding.inputFieldContainer.apply { touchDelegate = TouchDelegate(delegateArea, binding.inputInfo) }
        }
    }

    fun setLabel(label: String) {
        binding.inputLabel.text = label
    }

    fun getLabel(): String {
        return binding.inputLabel.text.toString()
    }

    fun setHint(hint: String) {
        binding.acInput.hint = hint
    }

    fun getInputText(): String {
        return binding.acInput.text.toString()
    }

    fun setInputText(input: String, triggerListener: Boolean = true) {
        binding.acInput.setText(input)
        if (triggerListener) actionListener?.onFinishInput(input)
    }

    fun setErrorMessage(message: String) {
        binding.errorLabel.text = message
        binding.errorLabel.visibility = View.VISIBLE
    }

    fun hideErrorMessage() {
        binding.errorLabel.text = ""
        binding.errorLabel.visibility = View.GONE
    }

    fun toggleDropdownIcon(value: Boolean) {
        if (value) binding.ivInputDropdown.show() else binding.ivInputDropdown.hide()
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
                    binding.btnClearInput.visibility = View.GONE
                } else {
                    if (count > 1) {
                        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.showSoftInput(binding.acInput, InputMethod.SHOW_FORCED)
                    }
                    binding.btnClearInput.visibility = View.VISIBLE
                    actionListener?.onTextChangeInput()
                }
            }
        }
    }

    fun setDelayTextChanged(delay: Long) {
        delayTextChanged = delay
    }

    fun setInputType(type: String) {
        binding.acInput.inputType = when (type) {
            INPUT_NUMERIC -> InputType.TYPE_CLASS_NUMBER
            INPUT_ALPHANUMERIC -> InputType.TYPE_CLASS_TEXT
            INPUT_TELCO -> InputType.TYPE_CLASS_PHONE
            else -> InputType.TYPE_CLASS_NUMBER
        }
    }

    fun resetState() {
        isCustomInput = false
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
