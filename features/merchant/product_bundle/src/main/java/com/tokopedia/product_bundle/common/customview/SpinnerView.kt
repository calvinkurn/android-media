package com.tokopedia.product_bundle.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.product_bundle.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class SpinnerView : BaseCustomView {

    var placeholderText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var text: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var layoutSpinner: LinearLayout? = null
    var tvSpinner: Typography? = null
    private var onClickListener: () -> Unit = {}

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.customview_spinner, this)
        layoutSpinner = view.findViewById(R.id.layout_spinner)
        tvSpinner = view.findViewById(R.id.tv_spinner)

        layoutSpinner?.setOnClickListener { onClickListener.invoke() }

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SpinnerView, 0, 0)

            try {
                placeholderText = styledAttributes.getString(R.styleable.SpinnerView_spinner_placeholder_text).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        if (text.isEmpty()) {
            tvSpinner?.text = placeholderText
            tvSpinner?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_44))
        } else {
            tvSpinner?.text = text
            tvSpinner?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
    }

    fun setOnClickListener(onClickListener: () -> Unit) {
        this.onClickListener = onClickListener
    }
}