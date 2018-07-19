package com.tokopedia.product.edit.price

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView

import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.product.edit.R

/**
 * Created by zulfikarrahman on 12/29/16.
 */

class LabelSwitch : BaseCustomView {
    private lateinit var titleTextView: TextView
    private lateinit var switchTextView: TextView
    private lateinit var switchStatus: SwitchCompat
    private lateinit var summaryTextView: TextView
    
    private var listener: CompoundButton.OnCheckedChangeListener? = null
    private var titleText: String? = null
    private var switchEnable: Boolean = false
    private var titleTextSize: Float = 0.toFloat()
    private var titleTextColor: Int = 0
    private var contentTextSize: Float = 0.toFloat()
    private var contentTextColor: Int = 0
    private var contentText: String? = null

    var title: String
        get() = titleTextView.text.toString()
        set(textTitle) {
            titleTextView.text = textTitle
            invalidate()
            requestLayout()
        }

    var isChecked: Boolean
        get() = switchStatus.isChecked
        set(isChecked) {
            switchStatus.isChecked = isChecked
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
        val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.LabelSwitch)
        try {
            titleText = styledAttributes.getString(R.styleable.LabelSwitch_ls_title)
            switchEnable = styledAttributes.getBoolean(R.styleable.LabelSwitch_ls_switch_enable, true)
            titleTextSize = styledAttributes.getDimension(R.styleable.LabelSwitch_ls_title_text_size, 0f)
            titleTextColor = styledAttributes.getColor(R.styleable.LabelSwitch_ls_title_color, 0)
            contentText = styledAttributes.getString(R.styleable.LabelSwitch_ls_content)
            contentTextSize = styledAttributes.getDimension(R.styleable.LabelSwitch_ls_content_text_size, 0f)
            contentTextColor = styledAttributes.getColor(R.styleable.LabelSwitch_ls_content_color, 0)
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
        setContent(contentText!!)
        switchStatus.isEnabled = switchEnable
        switchStatus.setOnCheckedChangeListener { buttonView, isChecked ->
            if (listener != null) {
                listener?.onCheckedChanged(buttonView, isChecked)
            }
        }
        this.rootView.isClickable = true
        this.rootView.setOnClickListener { switchStatus.isChecked = !switchStatus.isChecked }
        invalidate()
        requestLayout()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.widget_label_switch, this)
        titleTextView = view.findViewById(R.id.title_text_view)
        summaryTextView = view.findViewById(R.id.summary_text_view)
        switchTextView = view.findViewById(R.id.switch_text_view)
        switchStatus = view.findViewById(R.id.switch_status)
    }

    fun setContent(summaryText: String) {
        if (TextUtils.isEmpty(summaryText)) {
            summaryTextView.visibility = View.GONE
        } else {
            summaryTextView.text = summaryText
            summaryTextView.visibility = View.VISIBLE
        }
    }

    fun setSwitchStatusText(textTitle: String) {
        switchTextView.text = textTitle
        invalidate()
        requestLayout()
    }

    fun setCheckedNoListener(isChecked: Boolean) {
        val tempListener = this.listener
        this.listener = null
        switchStatus.isChecked = isChecked
        this.listener = tempListener
        invalidate()
        requestLayout()
    }

    fun setSwitchEnabled(enabled: Boolean) {
        if (switchStatus.isEnabled != enabled) {
            switchStatus.isEnabled = enabled
        }
    }

    fun setListenerValue(listener: CompoundButton.OnCheckedChangeListener) {
        this.listener = listener
    }
}
