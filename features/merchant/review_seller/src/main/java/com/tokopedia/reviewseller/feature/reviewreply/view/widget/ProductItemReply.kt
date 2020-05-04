package com.tokopedia.reviewseller.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ProductReplyUiModel
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
        View.inflate(context, R.layout.widget_reply_product_item, null)
    }

    fun setItem(data: ProductReplyUiModel) {
        ivItemProduct?.setImageUrl(data.productImageUrl.orEmpty())
        tgTitleProduct?.text = data.productName
        tgVariantName?.text = data.variantName
    }
}