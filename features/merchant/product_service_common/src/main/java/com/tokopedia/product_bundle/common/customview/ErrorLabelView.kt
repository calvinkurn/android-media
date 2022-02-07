package com.tokopedia.product_bundle.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.product_service_common.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class ErrorLabelView : BaseCustomView {

    var text: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var tvLabel: Typography? = null

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
        val view = View.inflate(context, R.layout.customview_error_label, this)
        tvLabel = view.findViewById(R.id.tv_label)

        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ErrorLabelView, 0, 0)

            try {
                text = styledAttributes.getString(R.styleable.ErrorLabelView_errorlabel_text).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvLabel?.text = text
    }
}