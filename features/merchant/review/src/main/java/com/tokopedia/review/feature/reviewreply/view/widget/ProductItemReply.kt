package com.tokopedia.review.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.review.R
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_reply_product_item.view.*

class ProductItemReply: BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.widget_reply_product_item, this)
    }

    fun setItem(data: ProductReplyUiModel) {
        ivItemProduct?.loadImage(data.productImageUrl.orEmpty())
        tgTitleProduct?.text = data.productName
        setupVariant(data.variantName.orEmpty())
    }

    private fun setupVariant(variantName: String) {
        if (variantName.isEmpty()) {
            tgVariantName?.hide()
            tgVariantLabel?.hide()
        } else {
            tgVariantLabel?.show()
            tgVariantName?.show()
            tgVariantName?.text = variantName
        }
    }
}