package com.tokopedia.product_bundle.common.customview

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.product_bundle.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class DiscountPriceView : BaseCustomView {

    var price: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var slashPrice: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var discountAmount: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var layoutDiscountPrice: ConstraintLayout? = null
    var tvItemPrice: Typography? = null
    var tvItemSlashPrice: Typography? = null
    var labelItemDiscount: Label? = null

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
        val view = View.inflate(context, R.layout.customview_discount_price, this)
        layoutDiscountPrice = view.findViewById(R.id.layout_discount_price)
        tvItemPrice = view.findViewById(R.id.tv_price)
        tvItemSlashPrice = view.findViewById(R.id.tv_slash_price)
        labelItemDiscount = view.findViewById(R.id.label_discount)
        tvItemSlashPrice?.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }

        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.DiscountPriceView, 0, 0)

            try {
                price = styledAttributes.getString(R.styleable.DiscountPriceView_discountprice_price).orEmpty()
                discountAmount = styledAttributes.getString(R.styleable.DiscountPriceView_discountprice_discount_amount).orEmpty()
                slashPrice = styledAttributes.getString(R.styleable.DiscountPriceView_discountprice_slashprice).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        tvItemPrice?.text = price
        tvItemSlashPrice?.text = slashPrice
        labelItemDiscount?.text = discountAmount
    }
}