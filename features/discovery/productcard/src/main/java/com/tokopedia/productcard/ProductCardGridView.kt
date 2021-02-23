package com.tokopedia.productcard

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel.Companion.WORDING_SEGERA_HABIS
import com.tokopedia.productcard.utils.*
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
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

        renderLabelCampaign(labelCampaignBackground, textViewLabelCampaign, productCardModel)

        renderLabelBestSeller(labelBestSeller, productCardModel)

        renderOutOfStockView(productCardModel)

        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())

        textTopAds?.showWithCondition(productCardModel.isTopAds)

        renderProductCardContent(productCardModel)

        renderStockPercentage(productCardModel)
        renderStockLabel(productCardModel)

        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)

        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)
        buttonAddToCart?.buttonType = productCardModel.addToCartButtonType

        buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)

        constraintLayoutProductCard?.post {
            imageThreeDots?.expandTouchArea(
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
                getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_16)
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

    fun setNotifyMeOnClickListener(notifyMeClickListener: (View) -> Unit) {
        buttonNotify?.setOnClickListener(notifyMeClickListener)
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
        labelCampaignBackground?.glideClear(context)
    }

    private fun View.renderStockPercentage(productCardModel: ProductCardModel) {
        progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
//                progressBarStock.setProgressIcon(ContextCompat.getDrawable(context, R.drawable.ic_fire_filled))
                progressBarStock.setProgressIcon(
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_fire_filled),
                        offsetY = ProductCardModel.FIRE_OFFSET,
                        width = context.resources.getDimension(R.dimen.dp_12).toInt(),
                        height = context.resources.getDimension(R.dimen.dp_16).toInt())
            }
            progressBarStock.progressBarColorType = ProgressBarUnify.COLOR_RED
            progressBarStock.setValue(productCardModel.stockBarPercentage, true)
        }
    }

    private fun View.renderStockLabel(productCardModel: ProductCardModel) {
        textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
            textViewStockLabel.text = productCardModel.stockBarLabel

            if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
                textViewStockLabel.setTextColor(MethodChecker.getColor(context,
                        com.tokopedia.unifyprinciples.R.color.Unify_R600))
            } else if (productCardModel.stockBarLabelColor.isNotEmpty()) {
                textViewStockLabel.setTextColor(safeParseColor(productCardModel.stockBarLabelColor))
            } else {
                textViewStockLabel.setTextColor(MethodChecker.getColor(context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
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

    override fun getThreeDotsButton(): View? = imageThreeDots

    override fun getNotifyMeButton(): UnifyButton? = buttonNotify

}