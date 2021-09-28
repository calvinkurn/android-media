package com.tokopedia.review.feature.reviewreply.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.databinding.WidgetReplyProductItemBinding
import com.tokopedia.review.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.unifycomponents.BaseCustomView

class ProductItemReply : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private val binding = WidgetReplyProductItemBinding.bind(this)

    private fun init() {
        View.inflate(context, R.layout.widget_reply_product_item, this)
    }

    fun setItem(data: ProductReplyUiModel) {
        binding.ivItemProduct.loadImage(data.productImageUrl.orEmpty())
        binding.tgTitleProduct.text = data.productName
        setupVariant(data.variantName.orEmpty())
    }

    private fun setupVariant(variantName: String) {
        binding.apply {
            if (variantName.isEmpty()) {
                tgVariantName.hide()
                tgVariantLabel.hide()
            } else {
                tgVariantLabel.show()
                tgVariantName.show()
                tgVariantName.text = variantName
            }
        }
    }
}