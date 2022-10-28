package com.tokopedia.gifting.presentation.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class NumberView : BaseCustomView {

    var text: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var tvNumber: Typography? = null

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
        val view = View.inflate(context, R.layout.customview_numberview, this)
        tvNumber = view.findViewById(R.id.tv_number)

        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.NumberView, 0, 0)

            try {
                text = styledAttributes.getString(R.styleable.NumberView_numberview_text).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvNumber?.text = text
    }
}