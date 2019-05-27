package com.tokopedia.instantloan.view.ui

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.tokopedia.instantloan.R
import kotlinx.android.synthetic.main.widget_add_remove.view.*

open class WidgetAddRemove @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val defaultEnableColor: String = "#42B549"
    private val defaultDisableColor: String = "#e0e0e0"
    private var enableBtnTintColor: Int? = null
    private var disableBtnTintColor: Int? = null
    private var minQuantity: Int = 1
    private var maxQuantity: Int = 1
    private var currentQuantity: Int = 0
    private var loanValue: Long = 0
    private lateinit var buttonClickListener: OnButtonClickListener


    fun setButtonClickListener(buttonClickListener: OnButtonClickListener) {
        this.buttonClickListener = buttonClickListener
    }

    interface OnButtonClickListener {
        fun onIncreaseButtonClicked(currentQuantity: Int)
        fun onDecreaseButtonClicked(currentQuantity: Int)
    }

    init {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {

        attrs?.let { attributes ->
            val typedArray = context.obtainStyledAttributes(attributes, R.styleable.WidgetAddRemove)
            try {
                enableBtnTintColor = typedArray.getColor(
                        R.styleable.WidgetAddRemove_war_btnEnableTintColor,
                        Color.parseColor(defaultEnableColor)
                )
                disableBtnTintColor = typedArray.getColor(
                        R.styleable.WidgetAddRemove_war_btnDisableTintColor,
                        Color.parseColor(defaultDisableColor)
                )
                minQuantity = typedArray.getInt(
                        R.styleable.WidgetAddRemove_war_minQuantity,
                        minQuantity
                )
                maxQuantity = typedArray.getInt(
                        R.styleable.WidgetAddRemove_war_maxQuantity,
                        maxQuantity
                )
            } finally {
                typedArray.recycle()
            }
        }
        inflateView()
    }

    private fun inflateView() {
        View.inflate(context, getLayoutResId(), this)
        war_increase.setOnClickListener {
            onIncreaseButtonClicked()
            buttonClickListener.onIncreaseButtonClicked(currentQuantity)
        }
        war_decrease.setOnClickListener {
            onDecreaseButtonClicked()
            buttonClickListener.onDecreaseButtonClicked(currentQuantity)
            if (currentQuantity < minQuantity) {
                currentQuantity = minQuantity
            }
        }
        resetButtons()
    }

    private fun onDecreaseButtonClicked() {
        currentQuantity--
        resetButtons()
    }

    private fun onIncreaseButtonClicked() {
        if (currentQuantity < maxQuantity) {
            currentQuantity++
        }
        resetButtons()
    }

    private fun resetButtons() {
        if (currentQuantity > 0) {
            setButtonColorAndClickable(war_decrease, enableBtnTintColor, true)
        } else {
            setButtonColorAndClickable(war_decrease, disableBtnTintColor, true)
        }
        if (currentQuantity < maxQuantity) {
            setButtonColorAndClickable(war_increase, enableBtnTintColor, true)
        } else {
            setButtonColorAndClickable(war_increase, disableBtnTintColor, false)
        }
    }

    private fun setButtonColorAndClickable(button: ImageView, btnTintColor: Int?, isClickable: Boolean) {
        btnTintColor?.let {
            button.setColorFilter(
                    it,
                    PorterDuff.Mode.SRC_IN
            )
        }
        button.isClickable = isClickable

    }

    fun setText(text: CharSequence) {
        war_tv_value.text = text
    }


    fun setLoanValue(value: Long){
        loanValue = value
    }

    fun getLoanValue(): Long {
        return loanValue
    }


    fun setMinQuantity(minQuantity: Int) {
        this.minQuantity = minQuantity
        resetButtons()
    }

    fun setMaxQuantity(maxQuantity: Int) {
        this.maxQuantity = maxQuantity
        resetButtons()
    }

    protected fun getLayoutResId(): Int {
        return R.layout.widget_add_remove
    }


}