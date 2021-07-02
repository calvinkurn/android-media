package com.tokopedia.review.feature.reading.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.reading.presentation.adapter.ReadReviewItemListener
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ReadReviewProductInfo : BaseCustomView {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private var productImage: ImageUnify? = null
    private var productName: Typography? = null
    private var threeDotsMenu: IconUnify? = null

    private fun init() {
        View.inflate(context, R.layout.widget_read_review_product_info, this)
        bindViews()
    }

    private fun bindViews() {
        productImage = findViewById(R.id.read_review_product_image)
        productName = findViewById(R.id.read_review_product_name)
        threeDotsMenu = findViewById(R.id.read_review_product_info_three_dots)
    }

    fun setProductInfo(imageUrl: String, productName: String) {
        productImage?.loadImage(imageUrl)
        this.productName?.text = productName
    }

    fun setListener(isReportable: Boolean, reviewId: String, shopId: String, readReviewItemListener: ReadReviewItemListener) {
        threeDotsMenu?.apply {
            if (isReportable) {
                show()
                setOnClickListener {
                    readReviewItemListener.onThreeDotsClicked(reviewId, shopId)
                }
            } else {
                hide()
            }
        }
    }

}