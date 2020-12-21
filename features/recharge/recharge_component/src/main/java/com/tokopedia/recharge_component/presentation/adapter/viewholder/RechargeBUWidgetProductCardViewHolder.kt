package com.tokopedia.recharge_component.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.model.RechargeBUWidgetProductCardModel
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.home_recharge_bu_widget_product_card.view.*

class RechargeBUWidgetProductCardViewHolder(
        itemView: View,
        private val channels: ChannelModel
): AbstractViewHolder<RechargeBUWidgetProductCardModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_product_card

        const val IMAGE_TYPE_FULL = "full"
        const val IMAGE_TYPE_FRAME = "frame"
    }

    override fun bind(element: RechargeBUWidgetProductCardModel) {
        with (itemView) {
            addOnImpressionListener(element) {
                element.listener.onProductCardImpressed(channels, ChannelGrid(), adapterPosition)
            }
            setOnClickListener {
                element.listener.onProductCardClicked(channels, ChannelGrid(), adapterPosition, element.applink)
            }
            applyCarousel()

            // Product Image
            if (element.imageType == IMAGE_TYPE_FULL) {
                imageProduct.loadImage(element.imageUrl)
                imageProduct.show()
                imageProductIconContainer.hide()
                imageProductBackground.hide()
            } else {
                imageProductIcon.loadImage(element.imageUrl)
                imageProductIconContainer.show()
                imageProduct.invisible()
                try {
                    (imageProductBackground.background as? GradientDrawable)?.setColor(Color.parseColor(element.backgroundColor))
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                }
                imageProductBackground.show()
            }

            // Category
            with(textViewGimmick) {
                val productName = MethodChecker.fromHtml(element.categoryName)
                if (productName.isNotEmpty()) {
                    text = productName
                    try {
                        setTextColor(Color.parseColor(element.categoryNameColor))
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                    }
                    show()
                } else {
                    hide()
                }
            }

            // Product Name
            with(textViewProductName) {
                val productName = MethodChecker.fromHtml(element.productName)
                if (productName.isNotEmpty()) {
                    text = productName
                    show()
                } else {
                    hide()
                }
            }

            // Discount
            with(labelDiscount) {
                val discountPercentage = MethodChecker.fromHtml(element.discountPercentage)
                if (discountPercentage.isNotEmpty()) {
                    text = discountPercentage
                    show()
                } else {
                    hide()
                }
            }

            // Slashed Price
            with(textViewSlashedPrice) {
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
            with(textViewPrice) {
                val formattedPrice = MethodChecker.fromHtml(element.price)
                if (formattedPrice.isNotEmpty()) {
                    text = formattedPrice
                    show()
                } else {
                    hide()
                }
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