package com.tokopedia.product.addedit.tooltip.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TooltipCardView : BaseCustomView {

    var text: String = ""
    var tvTipsText: Typography? = null

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
        val view = View.inflate(context, R.layout.add_edit_product_partial_tooltip_card, this)
        tvTipsText = view.findViewById(R.id.tv_tips_text)

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TooltipCardView, 0, 0)

            try {
                text = styledAttributes.getString(R.styleable.TooltipCardView_text).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvTipsText?.text = text
    }
}