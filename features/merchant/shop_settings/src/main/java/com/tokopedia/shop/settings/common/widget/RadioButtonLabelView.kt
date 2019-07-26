package com.tokopedia.shop.settings.common.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.TextView

import com.tokopedia.shop.settings.R

class RadioButtonLabelView : FrameLayout {

    private var titleText: String? = null
    private var titleTextSize: Float = 0.toFloat()
    private var titleTextStyleValue: Int = 0
    private var titleTextView: TextView? = null
    @ColorInt
    private var titleColorValue: Int = 0
    private var radioButton: RadioButton? = null

    private var onRadioButtonLabelViewListener: OnRadioButtonLabelViewListener? = null

    var isChecked: Boolean
        get() = radioButton!!.isChecked
        set(isChecked) {
            radioButton!!.isChecked = isChecked
        }

    interface OnRadioButtonLabelViewListener {
        fun onChecked(isChecked: Boolean)
    }

    fun setOnRadioButtonLabelViewListener(onRadioButtonLabelViewListener: OnRadioButtonLabelViewListener) {
        this.onRadioButtonLabelViewListener = onRadioButtonLabelViewListener
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        applyAttrs(attrs)
        val view = LayoutInflater.from(context).inflate(com.tokopedia.design.R.layout.widget_label_view_radio_button,
                this, true)
        titleTextView = view.findViewById(com.tokopedia.design.R.id.text_view_title)
        titleTextView!!.text = titleText
        titleTextView!!.setTypeface(null, titleTextStyleValue)
        titleTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        titleTextView!!.setTextColor(titleColorValue)
        radioButton = view.findViewById(com.tokopedia.design.R.id.radio_button)
        radioButton!!.setOnCheckedChangeListener { _, isChecked ->
            if (onRadioButtonLabelViewListener != null) {
                onRadioButtonLabelViewListener!!.onChecked(isChecked)
            }
        }
        view.setOnClickListener {
            if (!radioButton!!.isChecked) {
                radioButton!!.isChecked = true
            }
        }
    }

    private fun applyAttrs(attrs: AttributeSet?) {
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.RadioButtonLabelView)
        try {
            titleText = styledAttributes.getString(R.styleable.RadioButtonLabelView_rblv_title)
            titleColorValue = styledAttributes.getColor(R.styleable.RadioButtonLabelView_rblv_title_color, ContextCompat.getColor(context, com.tokopedia.design.R.color.font_black_primary_70))
            titleTextStyleValue = styledAttributes.getInt(R.styleable.RadioButtonLabelView_rblv_title_text_style, Typeface.NORMAL)
            titleTextSize = styledAttributes.getDimension(R.styleable.RadioButtonLabelView_rblv_title_text_size, resources.getDimension(com.tokopedia.design.R.dimen.sp_16))
        } finally {
            styledAttributes.recycle()
        }
    }

    override fun setEnabled(enabled: Boolean) {
        isClickable = enabled
        if (enabled) {
            titleTextView!!.setTextColor(titleColorValue)
        } else {
            titleTextView!!.setTextColor(ContextCompat.getColor(context,  com.tokopedia.design.R.color.font_black_disabled_38))
        }
    }


}
