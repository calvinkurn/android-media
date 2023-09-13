package com.tokopedia.shop.flashsale.common.customcomponent

import android.content.Context
import android.text.InputType.TYPE_NULL
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography

class SpinnerView : BaseCustomView {

    var titleText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

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

    var message: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var tfBorder: TextFieldUnify2? = null
    var layoutSpinner: LinearLayout? = null
    var tvSpinner: Typography? = null

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
        val view = View.inflate(context, R.layout.ssfs_customview_spinner, this)
        setupViews(view)
        setupLayoutOnClick(layoutSpinner)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupViews(view: View) {
        tfBorder = view.findViewById(R.id.tf_border)
        layoutSpinner = view.findViewById(R.id.layout_spinner)
        tvSpinner = view.findViewById(R.id.tv_spinner)
        tfBorder?.setInputType(TYPE_NULL)
    }

    private fun setupLayoutOnClick(layoutSpinner: LinearLayout?) {
        layoutSpinner?.setOnClickListener {
            this@SpinnerView.performClick()
        }
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.SpinnerView, 0, 0)

            try {
                titleText = styledAttributes.getString(R.styleable.SpinnerView_spinner_title_text).orEmpty()
                placeholderText = styledAttributes.getString(R.styleable.SpinnerView_spinner_placeholder_text).orEmpty()
                message = styledAttributes.getString(R.styleable.SpinnerView_spinner_message).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tfBorder?.labelText?.text = titleText
        tfBorder?.setMessage(message)
        if (text.isEmpty()) {
            tvSpinner?.text = placeholderText
            tvSpinner?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
        } else {
            tvSpinner?.text = text
            tvSpinner?.setTextColor(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
        }
    }
}