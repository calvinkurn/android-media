package com.tokopedia.common.travel.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatTextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.common.travel.R
import com.tokopedia.unifycomponents.BaseCustomView

/**
 * @author by alvarisi on 10/25/17.
 */
class NumberPickerWithCounterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var numberInputView: AppCompatTextView? = null
    private var minusImageButton: AppCompatImageButton? = null
    private var plusImageButton: AppCompatImageButton? = null
    private var mEnabled = false
    private var number = 0
    private var minValue = 0
    private var maxValue = 0
    private var onPickerActionListener: OnPickerActionListener? = null

    interface OnPickerActionListener {
        fun onNumberChange(num: Int)
    }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        init()
        attrs?.let {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerWithCounterView)
            try {
                maxValue = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_max_value, DEFAULT_MAX_VALUE)
                minValue = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_min_value, DEFAULT_MIN_VALUE)
                number = styledAttributes.getInteger(R.styleable.NumberPickerWithCounterView_npc_value, DEFAULT_VALUE)
                mEnabled = styledAttributes.getBoolean(R.styleable.NumberPickerWithCounterView_npc_enabled, DEFAULT_ENABLED_VALUE)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun init() {
        val view = inflate(context, R.layout.widget_travel_number_picker_with_counter_view, this)
        numberInputView = view.findViewById<View>(R.id.decimal_input_view) as AppCompatTextView
        plusImageButton = view.findViewById<View>(R.id.image_button_plus) as AppCompatImageButton
        minusImageButton = view.findViewById<View>(R.id.image_button_minus) as AppCompatImageButton
        plusImageButton?.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.bg_animated_action_counter_plus_24))
        minusImageButton?.setImageDrawable(MethodChecker.getDrawable(context, R.drawable.bg_animated_action_counter_minus_24))
        numberInputView?.isEnabled = false
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        numberInputView?.text = number.toString()
        plusImageButton?.setOnClickListener {
            validateMaxValue(numberInputView?.text.toString().trim { it <= ' ' }.toInt() + 1)
            updateButtonState()
        }
        minusImageButton?.setOnClickListener {
            validateMinValue(numberInputView?.text.toString().trim { it <= ' ' }.toInt() - 1)
            updateButtonState()
        }
        numberInputView?.isEnabled = mEnabled
        updateButtonState()
    }

    private fun validateMaxValue(newNumber: Int) {
        if (numberInputView?.text.toString().trim { it <= ' ' }.toInt() < maxValue) {
            numberInputView?.text = newNumber.toString()
        }
    }

    private fun validateMinValue(newNumber: Int) {
        if (numberInputView?.text.toString().trim { it <= ' ' }.toInt() > minValue) {
            numberInputView?.text = newNumber.toString()
        }
    }

    private fun updateButtonState() {
        minusImageButton?.isEnabled = mEnabled && numberInputView?.text.toString().trim { it <= ' ' }.toInt() > minValue
        plusImageButton?.isEnabled = mEnabled && numberInputView?.text.toString().trim { it <= ' ' }.toInt() < maxValue
        if (onPickerActionListener != null) {
            onPickerActionListener?.onNumberChange(numberInputView?.text.toString().trim { it <= ' ' }.toInt())
        }
    }

    fun setNumber(number: Int) {
        numberInputView?.text = number.toString()
        minusImageButton?.isEnabled = mEnabled && numberInputView?.text.toString().trim { it <= ' ' }.toInt() > minValue
        plusImageButton?.isEnabled = mEnabled && numberInputView?.text.toString().trim { it <= ' ' }.toInt() < maxValue
    }

    fun setOnPickerActionListener(onPickerActionListener: OnPickerActionListener?) {
        this.onPickerActionListener = onPickerActionListener
    }

    fun setMinValue(number: Int) {
        minValue = number
        invalidate()
        requestLayout()
    }

    fun setMaxValue(number: Int) {
        maxValue = number
        invalidate()
        requestLayout()
    }

    val value: Int
        get() = numberInputView?.text.toString().trim { it <= ' ' }.toInt()

    companion object {
        private const val DEFAULT_VALUE = 0
        private const val DEFAULT_MIN_VALUE = 0
        private const val DEFAULT_MAX_VALUE = 100
        private const val DEFAULT_ENABLED_VALUE = true
    }
}