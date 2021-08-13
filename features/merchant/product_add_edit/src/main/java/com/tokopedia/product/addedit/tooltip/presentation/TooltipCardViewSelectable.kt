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
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.DividerUnify
import com.tokopedia.unifycomponents.LoaderUnify

class TooltipCardViewSelectable : BaseCustomView {

    var text: String = ""
        set(value) {
            field = value
            refreshViews()
        }
    var description: String = ""
        set(value) {
            field = value
            refreshViews()
        }
    var priceDescription: String = ""
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
    var titleIcon: ImageView? = null

    private var tvTipsText: Typography? = null
    private var tvTipsDescription: Typography? = null
    private var tvPriceDescription: Typography? = null
    private var dividerPriceDescription: DividerUnify? = null
    private var iconExpand: IconUnify? = null
    private var layoutTooltipContent: ViewGroup? = null
    private var layoutTooltipShimmer: ViewGroup? = null
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
        if (!rotated) {
            iconExpand?.animateRotateCw()
            layoutTooltipContent?.animateCollapse()
            rotated = true
        }
    }

    fun expand() {
        if (rotated) {
            iconExpand?.animateRotateCcw()
            layoutTooltipContent?.animateExpand()
            rotated = false
        }
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

    fun displaySuggestedPriceSelected() {
        val checkIcon = getIconUnifyDrawable(context,
                IconUnify.CHECK_CIRCLE,
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500))
        text = context.getString(R.string.title_price_recommendation_applied)
        titleIcon?.setImageDrawable(checkIcon)
        tvApply?.hide()
        collapse()
    }

    fun displaySuggestedPriceDeselected() {
        val bulbIcon = MethodChecker.getDrawable(context, R.drawable.product_add_edit_ic_tips_bulb)
        text = context.getString(R.string.title_price_recommendation)
        titleIcon?.setImageDrawable(bulbIcon)
        tvApply?.show()
        expand()
    }

    fun setPriceDescriptionVisibility(isVisible: Boolean) {
        tvPriceDescription?.isVisible = isVisible
        dividerPriceDescription?.isVisible = isVisible
    }

    fun setShimmerVisibility(isVisible: Boolean) {
        layoutTooltipContent?.isVisible = !isVisible
        iconExpand?.isVisible = !isVisible
        layoutTooltipShimmer?.isVisible = isVisible
    }

    fun hideIconCheck() {
        iconCheck?.hide()
        iconCheck = null
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.add_edit_product_tooltip_card_selectable, this)
        tvTipsText = view.findViewById(R.id.tv_tips_text)
        tvTipsDescription = view.findViewById(R.id.tv_description)
        tvPriceDescription = view.findViewById(R.id.tv_price_description)
        dividerPriceDescription = view.findViewById(R.id.divider_price_description)
        iconExpand = view.findViewById(R.id.icon_expand)
        layoutTooltipContent = view.findViewById(R.id.layout_tooltip_content)
        layoutTooltipShimmer = view.findViewById(R.id.layout_tooltip_shimmer)
        tvApply = view.findViewById(R.id.tv_apply)
        loaderApply = view.findViewById(R.id.loader_apply)
        iconCheck = view.findViewById(R.id.icon_check)
        tvDescriptionLink = view.findViewById(R.id.tv_description_link)
        titleIcon = view.findViewById(R.id.iv_tips_bulb)

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
                priceDescription = styledAttributes.getString(R.styleable.TooltipCardViewSelectable_textTitleDescription).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvTipsText?.text = text
        tvTipsDescription?.text = description
        tvPriceDescription?.text = MethodChecker.fromHtml(priceDescription)
    }

    private fun setupHideExpandButton() {
        iconExpand?.setOnClickListener {
            if (rotated) expand()
            else collapse()
        }
    }
}