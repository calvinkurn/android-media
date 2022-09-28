package com.tokopedia.shop.flashsale.common.customcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.seller_shop_flash_sale.R
import com.tokopedia.shop.flashsale.common.util.doOnTextChanged
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography

class TextFieldLabelView : BaseCustomView {

    var placeholderText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var text: String = ""
        set(value) {
            field = value
            refreshTextInput()
        }

    var appendText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var prependText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var enabledEditing: Boolean = true
        set(value) {
            field = value
            refreshViews()
        }

    var textField: TextFieldUnify2? = null
    var layoutLabel: LinearLayout? = null
    var tfPlaceholder: Typography? = null
    var tfText: Typography? = null
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
        val view = View.inflate(context, R.layout.ssfs_customview_text_field_lebel_view, this)
        setupViews(view)
        defineCustomAttributes(attrs)
        refreshViews()
        setupTextListener()
    }

    private fun setupViews(view: View) {
        textField = view.findViewById(R.id.textField)
        layoutLabel = view.findViewById(R.id.layoutLabel)
        tfPlaceholder = view.findViewById(R.id.tfPlaceholder)
        tfText = view.findViewById(R.id.tfText)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TextFieldLabelView, 0, 0)
            try {
                appendText = styledAttributes.getString(R.styleable.TextFieldLabelView_tfl_append_text).orEmpty()
                prependText = styledAttributes.getString(R.styleable.TextFieldLabelView_tfl_prepend_text).orEmpty()
                placeholderText = styledAttributes.getString(R.styleable.TextFieldLabelView_tfl_placeholder_text).orEmpty()
                enabledEditing = styledAttributes.getBoolean(R.styleable.TextFieldLabelView_tfl_enable_editing, true)
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun setupTextListener() {
        textField?.editText?.doOnTextChanged {
            val text = if (it.isEmpty()) "-" else it
            val textPlaceholder = "$prependText $text$appendText"
            tfText?.text = textPlaceholder
        }
    }

    private fun refreshViews() {
        textField?.setLabel(placeholderText)
        textField?.prependText(prependText)
        textField?.appendText(appendText)

        textField?.isVisible = enabledEditing
        layoutLabel?.isVisible = !enabledEditing

        tfPlaceholder?.text = placeholderText
    }

    private fun refreshTextInput() {
        textField?.editText?.setText(text)
    }

    fun setOnClickListener(onClickListener: () -> Unit) {
        this.onClickListener = onClickListener
    }
}