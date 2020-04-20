package com.tokopedia.product.addedit.detail.presentation.widget

import android.content.Context
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.util.InputPriceUtil
import kotlinx.android.synthetic.main.add_edit_product_bulk_price_edit_bottom_sheet_content.view.*

class ProductBulkPriceEditBottomSheetContent : LinearLayout {
    private var priceInputListener: PriceInputListener? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.add_edit_product_bulk_price_edit_bottom_sheet_content, this)
        createSaveButtonListener()
    }

    private fun createSaveButtonListener() {
        btn_save.setOnClickListener {
            with(tfu_product_price) {
                if (!isTextFieldError) {
                    val price = tfu_product_price.textFieldInput.text.toString().replace(".", "").toLong()
                    priceInputListener?.onPriceChangeRequested(price)
                }
            }
        }
    }

    fun setPrice(price: String) {
        tfu_product_price.textFieldInput.setText(InputPriceUtil.formatProductPriceInput(price))
    }

    fun setPriceTextWatcher(textWatcher: TextWatcher) {
        tfu_product_price.textFieldInput.addTextChangedListener(textWatcher)
    }

    fun setError(it: Boolean, productPriceMessage: String) = with(tfu_product_price) {
        setError(it)
        setMessage(productPriceMessage)
    }

    fun setPriceInputListener(priceInputListener: PriceInputListener) {
        this.priceInputListener = priceInputListener
    }

    interface PriceInputListener {
        fun onPriceChangeRequested(price: Long)
    }
}