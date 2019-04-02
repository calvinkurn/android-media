package com.tokopedia.product.manage.item.utils

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.manage.item.R

/**
 * Created by zulfikarrahman on 12/29/16.
 */

class LabelCheckbox : BaseCustomView {
    private lateinit var titleTextView: TextView
    private lateinit var checkboxTextView: TextView
    private lateinit var checkboxStatus: CheckBox
    private lateinit var summaryTextView: TextView

    private var titleText: String? = null
    private var checkboxEnable: Boolean = false
    private var titleTextSize: Float = 0.toFloat()
    private var titleTextColor: Int = 0
    private var subTitleTextSize: Float = 0.toFloat()
    private var subTitleTextColor: Int = 0
    private var subTitleText: String? = null

    var title: String
        get() = titleTextView.text.toString()
        set(textTitle) {
            titleTextView.text = textTitle
            invalidate()
            requestLayout()
        }

    var isChecked: Boolean
        get() = checkboxStatus.isChecked
        set(isChecked) {
            checkboxStatus.isChecked = isChecked
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
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.LabelCheckbox)
        try {
            titleText = styledAttributes.getString(R.styleable.LabelCheckbox_lc_title)
            checkboxEnable = styledAttributes.getBoolean(R.styleable.LabelCheckbox_lc_checkbox_enable, true)
            titleTextSize = styledAttributes.getDimension(R.styleable.LabelCheckbox_lc_title_text_size, 0f)
            titleTextColor = styledAttributes.getColor(R.styleable.LabelCheckbox_lc_title_color, 0)
            subTitleText = styledAttributes.getString(R.styleable.LabelCheckbox_lc_subtitle)
            subTitleTextSize = styledAttributes.getDimension(R.styleable.LabelCheckbox_lc_content_text_size, 0f)
            subTitleTextColor = styledAttributes.getColor(R.styleable.LabelCheckbox_lc_content_color, 0)
        } finally {
            styledAttributes.recycle()
        }
        if (titleTextSize != 0f) {
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize)
        }
        if (titleTextColor != 0) {
            titleTextView.setTextColor(titleTextColor)
        }

        if (subTitleTextSize != 0f) {
            summaryTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTitleTextSize)
        }
        if (subTitleTextColor != 0) {
            summaryTextView.setTextColor(subTitleTextColor)
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        titleTextView.text = titleText
        if(subTitleText!=null)
            setSubTitle(subTitleText!!)
        checkboxStatus.isEnabled = checkboxEnable
        this.rootView.isClickable = true
        this.rootView.setOnClickListener { checkboxStatus.isChecked = !checkboxStatus.isChecked }
        invalidate()
        requestLayout()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.widget_label_checkbox, this)
        titleTextView = view.findViewById(R.id.title_text_view)
        summaryTextView = view.findViewById(R.id.summary_text_view)
        checkboxTextView = view.findViewById(R.id.checkbox_text_view)
        checkboxStatus = view.findViewById(R.id.checkbox_status)
    }

    private fun setSubTitle(summaryText: String) {
        if (TextUtils.isEmpty(summaryText)) {
            summaryTextView.visibility = View.GONE
        } else {
            summaryTextView.text = summaryText
            summaryTextView.visibility = View.VISIBLE
        }
    }
}
