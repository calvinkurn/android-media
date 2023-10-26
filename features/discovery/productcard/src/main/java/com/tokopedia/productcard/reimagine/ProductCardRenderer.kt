package com.tokopedia.productcard.reimagine

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.ALL
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.TOP
import com.tokopedia.productcard.utils.imageRounded
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

internal class ProductCardRenderer(
    private val view: View,
    private val type: ProductCardType,
) {

    private val context = view.context

    private val imageView by view.lazyView<ImageUnify?>(R.id.productCardImage)
    private val adsText by view.lazyView<Typography?>(R.id.productCardAds)
    private val nameText by view.lazyView<Typography?>(R.id.productCardName)
    private val priceText by view.lazyView<Typography?>(R.id.productCardPrice)
    private val slashedPriceText by view.lazyView<Typography?>(R.id.productCardSlashedPrice)
    private val discountText by view.lazyView<Typography?>(R.id.productCardDiscount)
    private val slashedPriceInlineText by view.lazyView<Typography?>(R.id.productCardSlashedPriceInline)
    private val discountInlineText by view.lazyView<Typography?>(R.id.productCardDiscountInline)
    private val benefitLabel by view.lazyView<Typography?>(R.id.productCardLabelBenefit)
    private val credibilitySection by view.lazyView<LinearLayout?>(R.id.productCardCredibility)
    private val shopSection by view.lazyView<LinearLayout?>(R.id.productCardShopSection)
    private val freeShippingImage by view.lazyView<ImageView?>(R.id.productCardFreeShipping)
    private val ribbon = ProductCardRibbon(view)

    fun setProductModel(productCardModel: ProductCardModel) {
        renderImage(productCardModel)
        renderAds(productCardModel)
        renderName(productCardModel)
        renderPrice(productCardModel)
        renderSlashedPrice(productCardModel)
        renderDiscountPercentage(productCardModel)
        renderLabelBenefit(productCardModel)
        renderCredibilitySection(productCardModel)
        renderShopSection(productCardModel)
        renderFreeShipping(productCardModel)
        ribbon.render(productCardModel.ribbon())
    }

    private fun renderImage(productCardModel: ProductCardModel) {
        val cornerType= if (productCardModel.stockInfo() != null) TOP else ALL
        imageView?.imageRounded(
            productCardModel.imageUrl,
            context.resources.getDimensionPixelSize(
                R.dimen.product_card_reimagine_carousel_image_radius
            ).toFloat(),
            cornerType
        )
    }

    private fun renderAds(productCardModel: ProductCardModel) {
        adsText?.showWithCondition(productCardModel.isAds)
    }

    private fun renderName(productCardModel: ProductCardModel) {
        nameText?.shouldShowWithAction(productCardModel.name.isNotEmpty()) {
            it.setTextAndContentDescription(
                MethodChecker.fromHtml(productCardModel.name).toString(),
                R.string.content_desc_textViewProductName
            )
            it.maxLines = maxLinesName(productCardModel)
        }
    }

    private fun maxLinesName(productCardModel: ProductCardModel): Int {
        val hasMultilineName = when (type) {
            GridCarousel -> productCardModel.hasMultilineName
            Grid -> productCardModel.labelAssignedValue() == null
        }

        return if (hasMultilineName) 2 else 1
    }

    private fun renderPrice(productCardModel: ProductCardModel) {
        priceText?.shouldShowWithAction(productCardModel.price.isNotEmpty()) {
            it.setTextAndContentDescription(
                productCardModel.price,
                R.string.content_desc_textViewPrice
            )
        }
    }

    private fun renderSlashedPrice(productCardModel: ProductCardModel) {
        val hasSlashedPrice = productCardModel.slashedPrice.isNotEmpty()
        val showAsInline = showDiscountAsInline(productCardModel)
        val showBelowPrice = !showAsInline

        val setSlashedPrice = { slashedPriceText: Typography ->
            slashedPriceText.setTextAndContentDescription(
                productCardModel.slashedPrice,
                R.string.content_desc_textViewSlashedPrice
            )
            slashedPriceText.strikethrough()
            slashedPriceText.requestLayout()
        }

        slashedPriceInlineText?.shouldShowWithAction(
            shouldShow = hasSlashedPrice && showAsInline,
            action = setSlashedPrice
        )

        slashedPriceText?.shouldShowWithAction(
            shouldShow = hasSlashedPrice && showBelowPrice,
            action = setSlashedPrice
        )
    }

    private fun showDiscountAsInline(productCardModel: ProductCardModel): Boolean =
        type == GridCarousel && productCardModel.ribbon() != null

    private fun renderDiscountPercentage(productCardModel: ProductCardModel) {
        val hasDiscountPercentage = productCardModel.discountPercentage != 0
        val showAsInline = showDiscountAsInline(productCardModel)
        val showBelowPrice = !showAsInline

        val setDiscount = { discountText: Typography ->
            discountText.setTextAndContentDescription(
                "${productCardModel.discountPercentage}%",
                R.string.content_desc_labelDiscount
            )
        }

        discountInlineText?.shouldShowWithAction(
            shouldShow = showAsInline && hasDiscountPercentage,
            action = setDiscount
        )

        discountText?.shouldShowWithAction(
            shouldShow = showBelowPrice && hasDiscountPercentage,
            action = setDiscount,
        )
    }

    private fun renderLabelBenefit(productCardModel: ProductCardModel) {
        val labelBenefit = productCardModel.labelBenefit()
        benefitLabel?.shouldShowWithAction(labelBenefit != null) {
            it.initLabelGroupLabel(labelBenefit)
        }
    }

    private fun renderCredibilitySection(productCardModel: ProductCardModel) {
        val hasRating = productCardModel.rating.isNotEmpty()
        val labelCredibility = productCardModel.labelCredibility()
        val hasLabelCredibility = labelCredibility != null
        val hasCredibilitySection = hasRating || hasLabelCredibility
        credibilitySection?.shouldShowWithAction(hasCredibilitySection) {
            view.findViewById<IconUnify?>(R.id.productCardRatingIcon)?.showWithCondition(hasRating)
            view.findViewById<Typography?>(R.id.productCardRating)?.shouldShowWithAction(hasRating) {
                it.text = productCardModel.rating
            }

            view.findViewById<Typography?>(R.id.productCardLabelCredibility)?.shouldShowWithAction(
                hasLabelCredibility
            ) {
                it.text = labelCredibility?.title ?: ""
            }

            val ratingDotsVisibility = hasRating && hasLabelCredibility
            view.findViewById<Typography?>(R.id.productCardRatingDots)?.showWithCondition(
                ratingDotsVisibility
            )
        }
    }

    private fun renderShopSection(productCardModel: ProductCardModel) {
        val shopBadge = productCardModel.shopBadge
        val hasShopSection = shopBadge.hasTitle()
        shopSection?.shouldShowWithAction(hasShopSection) {
            val hasIcon = shopBadge.hasImage()
            view.findViewById<ImageView?>(R.id.productCardShopBadge)?.shouldShowWithAction(hasIcon) {
                it.loadIcon(shopBadge.imageUrl)
            }

            view.findViewById<Typography?>(R.id.productCardShopNameLocation)?.text = shopBadge.title
        }
    }

    private fun renderFreeShipping(productCardModel: ProductCardModel) {
        val freeShippingImageUrl = productCardModel.freeShipping.imageUrl
        freeShippingImage?.shouldShowWithAction(freeShippingImageUrl.isNotEmpty()) {
            it.loadIcon(freeShippingImageUrl)
        }
    }
}
