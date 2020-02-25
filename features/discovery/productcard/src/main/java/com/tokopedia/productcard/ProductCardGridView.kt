package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bumptech.glide.Glide
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.initLabelGroup
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*

class ProductCardGridView: BaseCustomView {

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.product_card_grid_layout, this)
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImage(productCardModel.productImageUrl)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasOptions)

        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        imageThreeDots.setOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        buttonAddToCart?.setOnClickListener(addToCartClickListener)
    }

    fun setCardHeight(height: Int) {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = height
        cardViewProductCard?.layoutParams = layoutParams
    }

    fun recycle() {
        Glide.with(context).clear(imageProduct)
        Glide.with(context).clear(imageFreeOngkirPromo)
    }



    /**
    * Special cases for specific pages
    * */
    fun shopPage_carouselHideComponent(
            isInvisibleRatingAndReview: Boolean,
            isInvisibleFreeOngkirBadge: Boolean,
            isInvisibleDiscountAndSlashPrice: Boolean
    ) {
        if (isInvisibleRatingAndReview) renderRatingAndReviewInvisible()

        if (isInvisibleFreeOngkirBadge) renderFreeOngkirInvisible()

        if (isInvisibleDiscountAndSlashPrice) renderDiscountInvisible()
    }
}