package com.tokopedia.product_bundle.common.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.product_bundle.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.CardUnify.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.CardUnify.Companion.TYPE_BORDER_ACTIVE
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

    var cardSpinner: CardUnify? = null
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
        cardSpinner = view.findViewById(R.id.card_spinner)
        layoutSpinner = view.findViewById(R.id.layout_spinner)
        tvSpinner = view.findViewById(R.id.tv_spinner)

        setupLayoutOnClick(layoutSpinner, cardSpinner)

        defineCustomAttributes(attrs)
        refreshViews()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupLayoutOnClick(layoutSpinner: LinearLayout?, cardSpinner: CardUnify?) {
        layoutSpinner?.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                cardSpinner?.cardType = (TYPE_BORDER_ACTIVE)
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                cardSpinner?.cardType = (TYPE_BORDER)
                onClickListener.invoke()
            }
            return@setOnTouchListener true
        }
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