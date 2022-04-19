package com.tokopedia.product.addedit.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography

class AddItemButton : BaseCustomView {

    var addItemButtonText: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    private var typographyAdd: Typography? = null
    private var iconAdd: IconUnify? = null
    private var isClickEnabled: Boolean = true
    private var onClickListener: (() -> Unit)? = null
    private var onDisabledClickListener: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun setEnabled(enabled: Boolean) {
        typographyAdd?.isEnabled = enabled
        iconAdd?.isEnabled = enabled
        isClickEnabled = enabled
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_add_item_button_layout, this)
        typographyAdd = view.findViewById(R.id.typography_add)
        iconAdd = view.findViewById(R.id.icon_add)
        setOnClickListener { view ->
            if (isClickEnabled) {
                onClickListener?.invoke()
            } else {
                onDisabledClickListener?.invoke()
            }
        }

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.AddItemButton, 0, 0)

            try {
                addItemButtonText = styledAttributes.getString(R.styleable.AddItemButton_addItemButtonText).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        typographyAdd?.text = addItemButtonText
    }

    fun setOnDisabledClickListener(listener: () -> Unit) {
        onDisabledClickListener = listener
    }

    fun setOnClickListener(listener: () -> Unit) {
        onClickListener = listener
    }
}