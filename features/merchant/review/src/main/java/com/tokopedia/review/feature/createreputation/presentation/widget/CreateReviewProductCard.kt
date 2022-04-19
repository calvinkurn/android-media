package com.tokopedia.review.feature.createreputation.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.review.R
import com.tokopedia.review.feature.createreputation.model.ProductData
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography


class CreateReviewProductCard : BaseCustomView {

    companion object {
        private const val PRODUCT_NAME_MAX_LINES_WITH_VARIANT = 1
    }

    private var productImage: AppCompatImageView? = null
    private var productName: Typography? = null
    private var productVariant: Typography? = null
    private var constraintLayout: ConstraintLayout? = null

    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    fun setProduct(product: ProductData) {
        with(product) {
            productImage?.loadImage(productImageURL)
            showProductName(productName)
            showProductVariant(productVariant.variantName)
        }
    }

    private fun init() {
        View.inflate(context, R.layout.widget_create_review_product_card, this)
        bindViews()
    }

    private fun bindViews() {
        productImage = findViewById(R.id.review_form_product_image)
        productName = findViewById(R.id.review_form_product_name)
        productVariant = findViewById(R.id.review_form_product_variant)
        constraintLayout = findViewById(R.id.review_form_product_card_layout)
    }

    private fun showProductName(name: String) {
        productName?.apply {
            text = name
            show()
        }
    }

    private fun showProductVariant(variant: String) {
        if(variant.isBlank()) return
        productVariant?.apply {
            text = context.getString(R.string.review_pending_variant, variant)
            show()
        }
        productName?.apply {
            maxLines = PRODUCT_NAME_MAX_LINES_WITH_VARIANT
            constraintLayout?.let {
                val constraintSet = ConstraintSet()
                constraintSet.clone(constraintLayout)
                constraintSet.clear(R.id.review_form_product_name, ConstraintSet.BOTTOM)
                constraintSet.applyTo(constraintLayout)
            }
        }
    }
}