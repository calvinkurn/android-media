package com.tokopedia.product.addedit.tooltip.presentation

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.animateCollapse
import com.tokopedia.product.addedit.common.util.animateExpand
import com.tokopedia.product.addedit.common.util.animateRotateCcw
import com.tokopedia.product.addedit.common.util.animateRotateCw
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import android.os.Handler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.LoaderUnify

class TooltipCardViewSelectable : BaseCustomView {

    var text: String = ""
    var description: String = ""
        set(value) {
            field = value
            refreshViews()
        }
    var price: String = ""
        set(value) {
            field = value
            findViewById<UnifyButton>(R.id.btn_price)?.apply {
                text = value
            }
        }

    private var tvTipsText: Typography? = null
    private var tvTipsDescription: Typography? = null
    private var iconExpand: IconUnify? = null
    private var layoutTooltipContent: ViewGroup? = null
    private var tvApply: Typography? = null
    private var loaderApply: LoaderUnify? = null
    private var iconCheck: IconUnify? = null
    private var tvDescriptionLink: Typography? = null
    private var onButtonNextClicked: () -> Unit = {}
    private var onSuggestedPriceSelected: (price: String) -> Unit = {}
    private var rotated = false

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    fun setButtonToBlack() {
        findViewById<UnifyButton>(R.id.btn_price)?.apply {
            gravity = Gravity.CENTER_VERTICAL or Gravity.START
            setTextColor(ContextCompat.getColor(this.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
        }
    }

    fun collapse() {
        iconExpand?.animateRotateCw()
        layoutTooltipContent?.animateCollapse()
        rotated = true
    }

    fun expand() {
        iconExpand?.animateRotateCcw()
        layoutTooltipContent?.animateExpand()
        rotated = false
    }

    fun setSuggestedPriceSelected() {
        tvApply?.hide()
        loaderApply?.hide()
        iconCheck?.show()
        collapse()
    }

    fun setOnButtonNextClicked(onClicked: () -> Unit) {
        onButtonNextClicked = onClicked
    }

    fun setOnSuggestedPriceSelected(onSelected: (price: String) -> Unit) {
        onSuggestedPriceSelected = onSelected
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_tooltip_card_selectable, this)
        tvTipsText = view.findViewById(R.id.tv_tips_text)
        tvTipsDescription = view.findViewById(R.id.tv_description)
        iconExpand = view.findViewById(R.id.icon_expand)
        layoutTooltipContent = view.findViewById(R.id.layout_tooltip_content)
        tvApply = view.findViewById(R.id.tv_apply)
        loaderApply = view.findViewById(R.id.loader_apply)
        iconCheck = view.findViewById(R.id.icon_check)
        tvDescriptionLink = view.findViewById(R.id.tv_description_link)

        setupHideExpandButton()
        setupTvApply()
        setupDescriptionLink()

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupDescriptionLink() {
        tvDescriptionLink?.setOnClickListener {
            onButtonNextClicked()
        }
    }

    private fun setupTvApply() {
        val duration = resources.getInteger(com.tokopedia.unifyprinciples.R.integer.Unify_T3).toLong()
        tvApply?.setOnClickListener {
            tvApply?.hide()
            iconCheck?.hide()
            loaderApply?.show()
            Handler().postDelayed({
                setSuggestedPriceSelected()
                onSuggestedPriceSelected(price.filter { it.isDigit() })
            }, duration)
        }
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TooltipCardViewSelectable, 0, 0)

            try {
                text = styledAttributes.getString(R.styleable.TooltipCardViewSelectable_textTitle).orEmpty()
                description = styledAttributes.getString(R.styleable.TooltipCardViewSelectable_textDescription).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvTipsText?.text = text
        tvTipsDescription?.text = description
    }

    private fun setupHideExpandButton() {
        iconExpand?.setOnClickListener {
            if (rotated) expand()
            else collapse()
        }
    }
}