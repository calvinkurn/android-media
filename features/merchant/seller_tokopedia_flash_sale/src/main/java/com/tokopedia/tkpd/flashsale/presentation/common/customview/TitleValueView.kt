package com.tokopedia.tkpd.flashsale.presentation.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_tokopedia_flash_sale.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class TitleValueView : BaseCustomView {

    var title: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var value: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var tfValue: Typography? = null
    var tfTitle: Typography? = null
    var iconValue: IconUnify? = null

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
        val view = View.inflate(context, R.layout.ssfs_customview_title_value, this)
        setupViews(view)
        defineCustomAttributes(attrs)
    }

    private fun setupViews(view: View) {
        tfTitle = view.findViewById(R.id.tfTitle)
        tfValue = view.findViewById(R.id.tfValue)
        iconValue = view.findViewById(R.id.iconValue)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TextValueView)
            try {
                title = styledAttributes.getString(R.styleable.TextValueView_textvalue_title).orEmpty()
                value = styledAttributes.getString(R.styleable.TextValueView_textvalue_value).orEmpty()
                iconValue?.isVisible = styledAttributes.getBoolean(R.styleable.TextValueView_textvalue_icon_visibility, false)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tfTitle?.text = title
        tfValue?.text = value
    }
}