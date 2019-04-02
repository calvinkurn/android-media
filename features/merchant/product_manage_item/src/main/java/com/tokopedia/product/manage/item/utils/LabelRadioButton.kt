package com.tokopedia.product.manage.item.utils

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView

import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.item.R

/**
 * Created by zulfikarrahman on 12/29/16.
 */

class LabelRadioButton : BaseCustomView {
    private lateinit var titleTextView: TextView
    private lateinit var radiobuttonTextView: TextView
    private lateinit var radiobuttonStatus: RadioButton
    private lateinit var summaryTextView: TextView

    private var titleText: String? = null
    private var radiobuttonEnable: Boolean = false
    private var titleTextSize: Float = 0.toFloat()
    private var titleTextColor: Int = 0
    private var contentTextSize: Float = 0.toFloat()
    private var contentTextColor: Int = 0
    private var subTitleText: String? = null

    var title: String
        get() = titleTextView.text.toString()
        set(textTitle) {
            titleTextView.text = textTitle
            invalidate()
            requestLayout()
        }

    var isChecked: Boolean
        get() = radiobuttonStatus.isChecked
        set(isChecked) {
            radiobuttonStatus.isChecked = isChecked
            invalidate()
            requestLayout()
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet) {
        init()
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.LabelRadioButton)
        try {
            titleText = styledAttributes.getString(R.styleable.LabelRadioButton_lr_title)
            radiobuttonEnable = styledAttributes.getBoolean(R.styleable.LabelRadioButton_lr_radiobutton_enable, true)
            titleTextSize = styledAttributes.getDimension(R.styleable.LabelRadioButton_lr_title_text_size, 0f)
            titleTextColor = styledAttributes.getColor(R.styleable.LabelRadioButton_lr_title_color, 0)
            subTitleText = styledAttributes.getString(R.styleable.LabelRadioButton_lr_subtitle)
            contentTextSize = styledAttributes.getDimension(R.styleable.LabelRadioButton_lr_content_text_size, 0f)
            contentTextColor = styledAttributes.getColor(R.styleable.LabelRadioButton_lr_content_color, 0)
        } finally {
            styledAttributes.recycle()
        }
        if (titleTextSize != 0f) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        }
        if (titleTextColor != 0) {
            titleTextView.setTextColor(titleTextColor)
        }

        if (contentTextSize != 0f) {
            summaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize)
        }
        if (contentTextColor != 0) {
            summaryTextView.setTextColor(contentTextColor)
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleTextView.text = titleText
        if(subTitleText!=null)
            subTitleText(subTitleText!!)
        radiobuttonStatus.isEnabled = radiobuttonEnable
        this.rootView.isClickable = true
        this.rootView.setOnClickListener { radiobuttonStatus.isChecked = !radiobuttonStatus.isChecked }
        invalidate()
        requestLayout()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.widget_label_radiobutton, this)
        titleTextView = view.findViewById(R.id.title_text_view)
        summaryTextView = view.findViewById(R.id.summary_text_view)
        radiobuttonTextView = view.findViewById(R.id.radiobutton_text_view)
        radiobuttonStatus = view.findViewById(R.id.radiobutton_status)
    }

    fun subTitleText(summaryText: String) {
        if (TextUtils.isEmpty(summaryText)) {
            summaryTextView.visibility = View.GONE
        } else {
            summaryTextView.text = summaryText
            summaryTextView.visibility = View.VISIBLE
        }
    }
}
