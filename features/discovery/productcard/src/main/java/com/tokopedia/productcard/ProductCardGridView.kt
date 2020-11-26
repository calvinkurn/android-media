package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.product_card_content_layout.view.*
import kotlinx.android.synthetic.main.product_card_grid_layout.view.*

class ProductCardGridView: BaseCustomView, IProductCardView {

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

    override fun setProductModel(productCardModel: ProductCardModel) {
        imageProduct?.loadImage(productCardModel.productImageUrl)

        renderOutOfStockView(productCardModel)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        renderStockPercentage(productCardModel)
        renderStockLabel(productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)

        constraintLayoutProductCard?.post {
            imageThreeDots?.expandTouchArea(
                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16),
                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)
            )
        }
    }

    fun setImageProductViewHintListener(impressHolder: ImpressHolder, viewHintListener: ViewHintListener) {
        imageProduct?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    fun setThreeDotsOnClickListener(threeDotsClickListener: (View) -> Unit) {
        imageThreeDots?.setOnClickListener(threeDotsClickListener)
    }

    fun setAddToCartOnClickListener(addToCartClickListener: (View) -> Unit) {
        buttonAddToCart?.setOnClickListener(addToCartClickListener)
    }

    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f

    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

    override fun recycle() {
        imageProduct?.glideClear(context)
        imageFreeOngkirPromo?.glideClear(context)
    }

    private fun View.renderStockPercentage(productCardModel: ProductCardModel) {
        progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            progressBarStock.progress = productCardModel.stockBarPercentage
        }
    }

    private fun View.renderStockLabel(productCardModel: ProductCardModel) {
        textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            textViewStockLabel.text = productCardModel.stockBarLabel
        }
    }

    private fun renderOutOfStockView(productCardModel: ProductCardModel) {
        if (productCardModel.isOutOfStock) {
            textViewStockLabel?.hide()
            progressBarStock?.hide()
            outOfStockOverlay?.visible()
        } else {
            outOfStockOverlay?.gone()
        }
    }
}