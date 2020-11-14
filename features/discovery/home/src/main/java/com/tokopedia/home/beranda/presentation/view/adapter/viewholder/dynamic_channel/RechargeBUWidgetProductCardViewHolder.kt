package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home.R
import com.tokopedia.home.beranda.domain.model.recharge_bu_widget.RechargeBUWidgetProductCardModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography

class RechargeBUWidgetProductCardViewHolder(
        itemView: View
): AbstractViewHolder<RechargeBUWidgetProductCardModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_product_card
    }

    override fun bind(element: RechargeBUWidgetProductCardModel) {
//        imageProduct?.loadImage(productCardModel.productImageUrl)
//
//        renderLabelCampaign(labelCampaignBackground, textViewLabelCampaign, productCardModel)
//
//        renderOutOfStockView(productCardModel)
//
//        labelProductStatus?.initLabelGroup(productCardModel.getLabelProductStatus())
//
//        textTopAds?.showWithCondition(productCardModel.isTopAds)
//
//        renderProductCardContent(productCardModel)
//
//        renderStockPercentage(productCardModel)
//        renderStockLabel(productCardModel)
//
//        imageThreeDots?.showWithCondition(productCardModel.hasThreeDots)
//
//        buttonAddToCart?.showWithCondition(productCardModel.hasAddToCartButton)
//
//        constraintLayoutProductCard?.post {
//            imageThreeDots?.expandTouchArea(
//                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
//                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16),
//                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_8),
//                getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_16)
//            )
//        }

        applyCarousel()

        val imageProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        val imageProductBackground: ImageView = itemView.findViewById(R.id.imageProductBackground)
        val imageProductIcon: ImageView = itemView.findViewById(R.id.imageProductIcon)
        val imageProductIconContainer: CardView = itemView.findViewById(R.id.imageProductIconContainer)
        val textViewGimmick: Typography = itemView.findViewById(R.id.textViewGimmick)
        val textViewProductName: Typography = itemView.findViewById(R.id.textViewProductName)
        val labelDiscount: Label = itemView.findViewById(R.id.labelDiscount)
        val textViewSlashedPrice: Typography = itemView.findViewById(R.id.textViewSlashedPrice)
        val textViewPrice: Typography = itemView.findViewById(R.id.textViewPrice)

        // Product Image
        if (element.imageType == "full") {
            imageProduct.loadImage(element.imageUrl)
            imageProduct.show()
            imageProductIconContainer.hide()
            imageProductBackground.hide()
        } else {
            imageProductIcon.loadImage(element.imageUrl)
            imageProductIconContainer.show()
            imageProduct.invisible()
//            imageProductBackground.setColorFilter(Color.parseColor(element.backgroundTintColor))
            imageProductBackground.setColorFilter(Color.parseColor(element.categoryNameColor))
            imageProductBackground.show()
        }

        // Category
        with (textViewGimmick) {
            val productName = MethodChecker.fromHtml(element.categoryName)
            if (productName.isNotEmpty()) {
                text = productName

                val textColor = try {
                    Color.parseColor(element.categoryNameColor)
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                    ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
                }
                setTextColor(textColor)

                show()
            } else {
                hide()
            }
        }

        // Product Name
        with (textViewProductName) {
            val productName = MethodChecker.fromHtml(element.productName)
            if (productName.isNotEmpty()) {
                text = productName
                show()
            } else {
                hide()
            }
        }

        // Discount
        with (labelDiscount) {
            val discountPercentage = MethodChecker.fromHtml(element.discountPercentage)
            if (discountPercentage.isNotEmpty()) {
                text = discountPercentage
                show()
            } else {
                hide()
            }
        }

        // Slashed Price
        with (textViewSlashedPrice) {
            val slashedPrice = MethodChecker.fromHtml(element.slashedPrice)
            if (slashedPrice.isNotEmpty()) {
                text = slashedPrice
                // If discount exists, value is slashed price
                if (element.discountPercentage.isNotEmpty()) {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                show()
            } else {
                hide()
            }
        }

        // Price
        with (textViewPrice) {
            val formattedPrice = MethodChecker.fromHtml(element.price)
            if (formattedPrice.isNotEmpty()) {
                text = formattedPrice
                show()
            } else {
                hide()
            }
        }
    }

//    override fun getCardMaxElevation() = cardViewProductCard?.maxCardElevation ?: 0f
//
//    override fun getCardRadius() = cardViewProductCard?.radius ?: 0f

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        val cardViewProductCard: CardView = itemView.findViewById(R.id.cardViewProductCard)
        val layoutParams = cardViewProductCard?.layoutParams
        layoutParams?.height = MATCH_PARENT
        cardViewProductCard?.layoutParams = layoutParams
    }

//    override fun recycle() {
//        imageProduct?.glideClear(context)
//        imageFreeOngkirPromo?.glideClear(context)
//        labelCampaignBackground?.glideClear(context)
//    }

//    private fun View.renderStockPercentage(productCardModel: ProductCardModel) {
//        progressBarStock?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
//            progressBarStock.progress = productCardModel.stockBarPercentage
//        }
//    }
//
//    private fun View.renderStockLabel(productCardModel: ProductCardModel) {
//        textViewStockLabel?.shouldShowWithAction(productCardModel.stockBarLabel.isNotEmpty()) {
//            textViewStockLabel.text = productCardModel.stockBarLabel
//        }
//    }

//    private fun renderOutOfStockView(productCardModel: ProductCardModel) {
//        if (productCardModel.isOutOfStock) {
//            textViewStockLabel?.hide()
//            progressBarStock?.hide()
//            outOfStockOverlay?.visible()
//        } else {
//            outOfStockOverlay?.gone()
//        }
//    }

//    override fun getThreeDotsButton(): View? = null
}