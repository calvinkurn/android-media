package com.tokopedia.recharge_component.presentation.adapter.viewholder

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recharge_component.R
import com.tokopedia.recharge_component.databinding.HomeRechargeBuWidgetProductCardBinding
import com.tokopedia.recharge_component.model.RechargeBUWidgetProductCardModel
import com.tokopedia.unifycomponents.ProgressBarUnify

class RechargeBUWidgetProductCardViewHolder(
    itemView: View,
    private val channels: ChannelModel
) : AbstractViewHolder<RechargeBUWidgetProductCardModel>(itemView) {

    private val binding = HomeRechargeBuWidgetProductCardBinding.bind(itemView)

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_recharge_bu_widget_product_card

        const val IMAGE_TYPE_FULL = "full"
        const val IMAGE_TYPE_FRAME = "frame"

        private const val MIN_PROGRESS_TO_SHOW_FIRE = 76
    }

    override fun bind(element: RechargeBUWidgetProductCardModel) {
        with(binding) {
            root.addOnImpressionListener(element) {
                element.listener.onProductCardImpressed(channels, ChannelGrid(), adapterPosition)
            }
            root.setOnClickListener {
                element.listener.onProductCardClicked(
                    channels,
                    ChannelGrid(),
                    adapterPosition,
                    element.applink
                )
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
                    (imageProductBackground.background as? GradientDrawable)?.setColor(
                        Color.parseColor(
                            element.backgroundColor
                        )
                    )
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

            // stock progress bar
            with(rechargeBuProgressStock) {
                if (element.showSoldPercentage) {
                    setProgressIcon(
                        icon = if (element.soldPercentage >= MIN_PROGRESS_TO_SHOW_FIRE) {
                            ContextCompat.getDrawable(
                                context,
                                com.tokopedia.resources.common.R.drawable.ic_fire_filled_product_card
                            )
                        } else {
                            null
                        },
                        width = context.resources.getDimension(R.dimen.digital_card_progress_fire_icon_width)
                            .toInt(),
                        height = context.resources.getDimension(R.dimen.digital_card_progress_fire_icon_height)
                            .toInt()
                    )
                    progressBarColorType = ProgressBarUnify.COLOR_RED
                    setValue(element.soldPercentage, false)
                    show()
                } else {
                    hide()
                }
            }

            with(rechargeBuProgressStockLabel) {
                if (element.showSoldPercentage) {
                    text = element.soldPercentageLabel

                    val color = try {
                        Color.parseColor(element.soldPercentageLabelColor)
                    } catch (throwable: Throwable) {
                        throwable.printStackTrace()
                        ContextCompat.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_RN500
                        )
                    }
                    setTextColor(color)

                    show()
                } else {
                    hide()
                }
            }
        }
    }

    fun applyCarousel() {
        setCardHeightMatchParent()
    }

    private fun setCardHeightMatchParent() {
        val layoutParams = binding.cardViewProductCard.layoutParams
        layoutParams?.height = MATCH_PARENT
        binding.cardViewProductCard.layoutParams = layoutParams
    }
}
