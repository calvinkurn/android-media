package com.tokopedia.productcard.reimagine

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.R
import com.tokopedia.productcard.experiments.ProductCardColor
import com.tokopedia.productcard.reimagine.LabelGroupStyle.TEXT_COLOR
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup.Style
import com.tokopedia.productcard.reimagine.benefit.LabelBenefitView
import com.tokopedia.productcard.reimagine.overlay.LabelOverlay
import com.tokopedia.productcard.reimagine.ribbon.RibbonView
import com.tokopedia.productcard.utils.RoundedCornersTransformation
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.ALL
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.TOP
import com.tokopedia.productcard.utils.imageRounded
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ProductCardRenderer(
    private val view: View,
    private val type: ProductCardType,
) {

    private val context = view.context

    private val outlineView by view.lazyView<View?>(R.id.productCardOutline)
    private val cardContainer by view.lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val imageView by view.lazyView<ImageView?>(R.id.productCardImage)
    private val labelOverlay = LabelOverlay(view)
    private val adsText by view.lazyView<Typography?>(R.id.productCardAds)
    private val labelPreventiveOverlay by view.lazyView<Typography?>(R.id.productCardLabelPreventiveOverlay)
    private val labelPreventiveBlock by view.lazyView<Typography?>(R.id.productCardLabelPreventiveBlock)
    private val nameText by view.lazyView<Typography?>(R.id.productCardName)
    private val labelAssignedValue by view.lazyView<ImageView?>(R.id.productCardLabelAssignedValue)
    private val priceText by view.lazyView<Typography?>(R.id.productCardPrice)
    private val nettPriceIcon by view.lazyView<ImageView?>(R.id.productCardNettPriceIcon)
    private val nettPriceText by view.lazyView<Typography?>(R.id.productCardNettPrice)
    private val slashedPriceText by view.lazyView<Typography?>(R.id.productCardSlashedPrice)
    private val discountText by view.lazyView<Typography?>(R.id.productCardDiscount)
    private val benefitLabel by view.lazyView<LabelBenefitView?>(R.id.productCardLabelBenefit)
    private val offerLabel by view.lazyView<Typography?>(R.id.productCardLabelOffer)
    private val credibilitySection by view.lazyView<LinearLayout?>(R.id.productCardCredibility)
    private val shopSection by view.lazyView<LinearLayout?>(R.id.productCardShopSection)
    private val shopNameBadgeText by view.lazyView<Typography?>(R.id.productCardShopNameLocation)
    private val buttonAddToCart by view.lazyView<UnifyButton?>(R.id.productCardAddToCart)
    private val labelBenefitView by view.lazyView<LabelBenefitView?>(R.id.productCardLabelBenefit)
    private val ribbon by view.lazyView<RibbonView?>(R.id.productCardRibbon)
    private val safeGroup by view.lazyView<Group?>(R.id.productCardSafeGroup)
    private val credibilityText by view.lazyView<Typography?>(R.id.productCardLabelCredibility)
    private val ratingText by view.lazyView<Typography?>(R.id.productCardRating)
    private val productCardRatingDots by view.lazyView<Typography?>(R.id.productCardRatingDots)

    fun setProductModel(productCardModel: ProductCardModel) {
        renderOutline(productCardModel)
        renderCardContainer(productCardModel)
        renderImage(productCardModel)
        renderOverlay(productCardModel)
        renderAds(productCardModel)
        renderLabelPreventiveOverlay(productCardModel)
        renderLabelPreventiveBlock(productCardModel)
        renderName(productCardModel)
        renderLabelAssignedValue(productCardModel)
        renderPrice(productCardModel)
        renderNettPrice(productCardModel)
        renderSlashedPrice(productCardModel)
        renderDiscountPercentage(productCardModel)
        renderLabelBenefit(productCardModel)
        renderLabelProductOffer(productCardModel)
        renderCredibilitySection(productCardModel)
        renderShopSection(productCardModel)
        renderRibbon(productCardModel)
        renderSafeContent(productCardModel)
        handleColorMode(productCardModel)
    }

    private fun renderOutline(productCardModel: ProductCardModel) {
        outlineView?.showWithCondition(productCardModel.isInBackground)
    }

    private fun renderCardContainer(productCardModel: ProductCardModel) {
        cardContainer?.layoutParams = cardContainer?.layoutParams?.apply {
            val marginLayoutParams = this as? ViewGroup.MarginLayoutParams
            marginLayoutParams?.marginStart = type.cardContainerMarginStart(productCardModel)
        }
    }

    private fun renderImage(productCardModel: ProductCardModel) {
        val cornerType = if (productCardModel.stockInfo() != null) TOP else ALL
        val overlayColor = ContextCompat.getColor(context, R.color.dms_product_card_reimagine_image_overlay)
        imageView?.apply {
            if (productCardModel.isSafeProduct)
                loadImage(ContextCompat.getDrawable(context, overlayProductImageSafe(cornerType)))
            else
                loadImage (
                    productCardModel,
                    cornerType,
                    overlayColor
                )
        }
    }

    private fun overlayProductImageSafe(cornerType: RoundedCornersTransformation.CornerType): Int =
        if (cornerType == TOP)
            R.drawable.product_card_safe_background_top_rounded
        else
            R.drawable.product_card_safe_background_full_rounded

    private fun ImageView.loadImage(
        productCardModel: ProductCardModel,
        cornerType: RoundedCornersTransformation.CornerType,
        @ColorInt
        overlayColor: Int
    ) {
        imageRounded(
            productCardModel.imageUrl,
            context.resources.getDimensionPixelSize(
                R.dimen.product_card_reimagine_image_radius
            ).toFloat(),
            cornerType,
            overlayColor
        )
    }

    private fun renderOverlay(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct
        if(isSafeProduct) {
            labelOverlay.hide()
            return
        }
        labelOverlay.render(productCardModel)
    }

    private fun renderAds(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct
        adsText?.showWithCondition(productCardModel.isAds && !isSafeProduct)
    }

    private fun renderLabelPreventiveOverlay(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct
        if(isSafeProduct) {
            labelPreventiveOverlay?.hide()
            return
        }

        val preventiveOverlayLabel = productCardModel.labelPreventiveOverlay()

        labelPreventiveOverlay?.let {
            if (preventiveOverlayLabel == null) {
                it.hide()
            } else {
                it.show()
                ProductCardLabel(it.background, it).render(preventiveOverlayLabel)
            }
        }
    }

    private fun renderLabelPreventiveBlock(productCardModel: ProductCardModel) {
        val preventiveBlockLabel = productCardModel.labelPreventiveBlock()

        labelPreventiveBlock?.let {
            if (preventiveBlockLabel == null) {
                it.hide()
            } else {
                it.show()
                ProductCardLabel(it.background, it).render(preventiveBlockLabel)
            }
        }
    }

    private fun renderName(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct

        nameText?.background = nameTextBackground(isSafeProduct)
        nameText?.shouldShowWithAction(productCardModel.name.isNotEmpty()) {
            if (isSafeProduct) {
                it.text = ""
                it.contentDescription = ""
            } else {
                val originalName = MethodChecker.fromHtml(productCardModel.name).toString()
                it.contentDescription =
                    context.getString(R.string.content_desc_textViewProductName) + " " + originalName

                val imageURL = productCardModel.labelAssignedValue()?.imageUrl ?: ""
                val renderName = (if (imageURL.isNotBlank()) " ".repeat(15) else "") + originalName
                it.text = renderName
            }
        }
    }

    private fun renderLabelAssignedValue(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct
        if(isSafeProduct) {
            labelAssignedValue?.hide()
            return
        }
        val productName = productCardModel.name
        val imageURL = productCardModel.labelAssignedValue()?.imageUrl ?: ""
        val hasLabelAssignedValue = productName.isNotBlank() && imageURL.isNotBlank()

        labelAssignedValue.shouldShowWithAction(hasLabelAssignedValue) {
            it.loadImage(imageURL)
        }
    }

    private fun nameTextBackground(isSafeProduct: Boolean) =
        if (isSafeProduct)
            ContextCompat.getDrawable(context, R.drawable.product_card_safe_background_title)
        else null

    private fun renderPrice(productCardModel: ProductCardModel) {
        priceText?.shouldShowWithAction(productCardModel.showPrice()) {
            it.setTextAndContentDescription(
                productCardModel.price,
                R.string.content_desc_textViewPrice
            )
        }
    }

    private fun renderNettPrice(productCardModel: ProductCardModel) {
        val nettPriceLabel = productCardModel.labelNettPrice()

        if (nettPriceLabel == null) {
            nettPriceIcon?.hide()
            nettPriceText?.hide()
        } else {
            nettPriceIcon?.show()
            nettPriceIcon?.loadImage(nettPriceLabel.imageUrl)

            nettPriceText?.show()
            ProductCardLabel(nettPriceText?.background, nettPriceText).render(nettPriceLabel)
        }
    }

    private fun renderSlashedPrice(productCardModel: ProductCardModel) {
        val hasSlashedPrice = productCardModel.slashedPrice.isNotEmpty()

        slashedPriceText?.shouldShowWithAction(hasSlashedPrice) {
            it.setTextAndContentDescription(
                productCardModel.slashedPrice,
                R.string.content_desc_textViewSlashedPrice
            )
            it.strikethrough()
            it.requestLayout()
        }
    }

    private fun renderDiscountPercentage(productCardModel: ProductCardModel) {
        val hasDiscountPercentage = productCardModel.discountPercentage != 0

        discountText?.shouldShowWithAction(hasDiscountPercentage) {
            it.setTextAndContentDescription(
                "${productCardModel.discountPercentage}%",
                R.string.content_desc_labelDiscount
            )
        }
    }

    private fun renderLabelBenefit(productCardModel: ProductCardModel) {
        val labelBenefit = productCardModel.labelBenefit()
        benefitLabel?.shouldShowWithAction(labelBenefit != null) {
            it.render(labelBenefit)
        }
    }

    private fun renderLabelProductOffer(productCardModel: ProductCardModel) {
        val offerLabel = offerLabel ?: return

        val labelProductOffer = labelProductOffer(productCardModel)

        if (labelProductOffer == null) {
            offerLabel.hide()
        } else {
            offerLabel.show()
            ProductCardLabel(offerLabel.background, offerLabel).render(labelProductOffer)
        }
    }

    private fun labelProductOffer(productCardModel: ProductCardModel): LabelGroup? {
        val labelProductOffer = productCardModel.labelProductOffer() ?: return null

        return if (labelProductOffer.textColor().isNullOrBlank()) {
            val defaultTextColor = Style(
                key = TEXT_COLOR,
                value = context.getColorAsHexString(unifyprinciplesR.color.Unify_YN500)
            )

            labelProductOffer.copy(
                styles = labelProductOffer.styles + listOf(defaultTextColor)
            )
        } else labelProductOffer
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

    private fun renderRibbon(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct
        if(isSafeProduct) {
            ribbon?.hide()
            return
        }
        ribbon?.render(productCardModel.ribbon())

        val ribbonMargin = type.ribbonMargin(productCardModel)

        ribbon?.setMargin(
            left = ribbonMargin.start,
            top = ribbonMargin.top,
            right = 0,
            bottom = 0,
        )
    }

    private fun renderSafeContent(productCardModel: ProductCardModel) {
        safeGroup?.showWithCondition(productCardModel.isSafeProduct)
    }

    private fun handleColorMode(productCardModel: ProductCardModel) {
        if (productCardModel.colorMode == null) return
        overrideColor(colorMode = productCardModel.colorMode)
    }

    private fun overrideColor(colorMode: ProductCardColor) {
        colorMode.cardBackgroundColor?.let { cardBackgroundColor ->
            cardContainer?.setCardUnifyBackgroundColor(ContextCompat.getColor(context, cardBackgroundColor))
        }

        colorMode.productNameTextColor?.let { productNameTextColor ->
            nameText?.setTextColor(ContextCompat.getColor(context, productNameTextColor))
        }

        colorMode.priceTextColor?.let { priceTextColor ->
            priceText?.setTextColor(ContextCompat.getColor(context, priceTextColor))
        }

        colorMode.slashPriceTextColor?.let { slashPriceTextColor ->
            slashedPriceText?.setTextColor(ContextCompat.getColor(context, slashPriceTextColor))
        }

        colorMode.soldCountTextColor?.let { soldCountTextColor ->
            credibilityText?.setTextColor(ContextCompat.getColor(context, soldCountTextColor))
        }

        colorMode.discountTextColor?.let { discountTextColor ->
            discountText?.setTextColor(ContextCompat.getColor(context, discountTextColor))
        }

        colorMode.ratingTextColor?.let { ratingTextColor ->
            ratingText?.setTextColor(ContextCompat.getColor(context, ratingTextColor))
        }

        colorMode.shopBadgeTextColor?.let { shopBadgeTextColor ->
            shopNameBadgeText?.setTextColor(ContextCompat.getColor(context, shopBadgeTextColor))
        }

        colorMode.buttonColorMode?.let { buttonColorMode ->
            buttonAddToCart?.applyColorMode(buttonColorMode)
        }

        colorMode.labelBenefitViewColor?.let { labelBenefitViewColor ->
            val hasCustomCutoutFillColor = labelBenefitViewColor.cutoutFillColor.isNotEmpty()
            if (hasCustomCutoutFillColor) {
                labelBenefitView?.setCutoutFillColor(labelBenefitViewColor.cutoutFillColor)
            } else {
                labelBenefitView?.setCutoutFillColor(unifyprinciplesR.color.Unify_NN0)
            }
        }

        colorMode.ratingDotColor?.let { ratingDotColor ->
            productCardRatingDots?.setTextColor(ContextCompat.getColor(context, ratingDotColor))
        }

        outlineView?.isVisible = colorMode.showOutlineView
    }
}
