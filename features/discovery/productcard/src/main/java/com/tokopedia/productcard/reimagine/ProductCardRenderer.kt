package com.tokopedia.productcard.reimagine

import android.graphics.PorterDuff
import android.text.Spannable
import android.text.SpannableString
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.assignedvalue.renderProductNameWithAssignedValue
import com.tokopedia.productcard.reimagine.benefit.LabelBenefitView
import com.tokopedia.productcard.reimagine.overlay.LabelOverlay
import com.tokopedia.productcard.reimagine.ribbon.RibbonView
import com.tokopedia.productcard.utils.RoundedCornersTransformation
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.ALL
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.TOP
import com.tokopedia.productcard.utils.imageRounded
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifyprinciples.Typography

internal class ProductCardRenderer(
    private val view: View,
    private val type: ProductCardType,
) {

    private val context = view.context

    private val cardContainer by view.lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val cardConstraintLayout by view.lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by view.lazyView<ImageView?>(R.id.productCardImage)
    private val labelOverlay = LabelOverlay(view)
    private val adsText by view.lazyView<Typography?>(R.id.productCardAds)
    private val labelPreventiveOverlay by view.lazyView<Typography?>(R.id.productCardLabelPreventiveOverlay)
    private val labelPreventiveBlock by view.lazyView<Typography?>(R.id.productCardLabelPreventiveBlock)
    private val nameText by lazyThreadSafetyNone { initNameText() }
    private val priceText by view.lazyView<Typography?>(R.id.productCardPrice)
    private val nettPriceIcon by view.lazyView<ImageView?>(R.id.productCardNettPriceIcon)
    private val nettPriceText by view.lazyView<Typography?>(R.id.productCardNettPrice)
    private val slashedPriceText by view.lazyView<Typography?>(R.id.productCardSlashedPrice)
    private val discountText by view.lazyView<Typography?>(R.id.productCardDiscount)
    private val slashedPriceInlineText by view.lazyView<Typography?>(R.id.productCardSlashedPriceInline)
    private val discountInlineText by view.lazyView<Typography?>(R.id.productCardDiscountInline)
    private val benefitLabel by view.lazyView<LabelBenefitView?>(R.id.productCardLabelBenefit)
    private val offerLabel by view.lazyView<Typography?>(R.id.productCardLabelOffer)
    private val credibilitySection by view.lazyView<LinearLayout?>(R.id.productCardCredibility)
    private val shopSection by view.lazyView<LinearLayout?>(R.id.productCardShopSection)
    private val ribbon by view.lazyView<RibbonView?>(R.id.productCardRibbon)
    private val safeGroup by view.lazyView<Group?>(R.id.productCardSafeGroup)

    private fun initNameText(): Typography? =
        view.findViewById<Typography?>(R.id.productCardName).also {
            it.setSpannableFactory(SPANNABLE_FACTORY)
        }

    fun setProductModel(productCardModel: ProductCardModel) {
        renderImage(productCardModel)
        renderOverlay(productCardModel)
        renderAds(productCardModel)
        renderLabelPreventiveOverlay(productCardModel)
        renderLabelPreventiveBlock(productCardModel)
        renderName(productCardModel)
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
        renderAddToCart(productCardModel)
    }

    private fun renderImage(productCardModel: ProductCardModel) {
        val cornerType = if (productCardModel.stockInfo() != null) TOP else ALL

        imageView?.apply {
            if (productCardModel.isSafeProduct) 
                loadImage(ContextCompat.getDrawable(context, overlayProductImageSafe(cornerType)))
            else
                loadImage(productCardModel, cornerType)

            setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.dms_product_card_reimagine_image_overlay,
                ),
                PorterDuff.Mode.SRC_OVER
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
    ) {
        imageRounded(
            productCardModel.imageUrl,
            context.resources.getDimensionPixelSize(
                R.dimen.product_card_reimagine_image_radius
            ).toFloat(),
            cornerType
        )
    }

    private fun renderOverlay(productCardModel: ProductCardModel) {
        labelOverlay.render(productCardModel)
    }

    private fun renderAds(productCardModel: ProductCardModel) {
        val isSafeProduct = productCardModel.isSafeProduct
        adsText?.showWithCondition(productCardModel.isAds && !isSafeProduct)
    }

    private fun renderLabelPreventiveOverlay(productCardModel: ProductCardModel) {
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
            it.maxLines = maxLinesName()

            if (isSafeProduct) {
                it.setText(SpannableString(""), TextView.BufferType.SPANNABLE)
                it.contentDescription = ""
            } else {
                val name = MethodChecker.fromHtml(productCardModel.name).toString()
                it.renderProductNameWithAssignedValue(productCardModel, name)
                it.contentDescription =
                    context.getString(R.string.content_desc_textViewProductName) + " " + name
            }
        }
    }

    private fun maxLinesName(): Int {
        val hasMultilineName = type != ProductCardType.GridCarousel

        return if (hasMultilineName) 2 else 1
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
        (type == ProductCardType.GridCarousel || type == ProductCardType.ListCarousel)
            && productCardModel.ribbon() != null

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
            it.render(labelBenefit)
        }
    }

    private fun renderLabelProductOffer(productCardModel: ProductCardModel) {
        val offerLabel = offerLabel ?: return
        val labelProductOffer = productCardModel.labelProductOffer()

        if (labelProductOffer == null) {
            offerLabel.hide()
        } else {
            val hasLabelBenefit = productCardModel.labelBenefit() != null
            val isCarousel = type == ProductCardType.GridCarousel || type == ProductCardType.ListCarousel
            val showLabelProductOffer = !hasLabelBenefit || !isCarousel

            offerLabel.shouldShowWithAction(showLabelProductOffer) {
                ProductCardLabel(it.background, it).render(labelProductOffer)
            }
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

    private fun renderRibbon(productCardModel: ProductCardModel) {
        val labelRibbon = productCardModel.ribbon()

        ribbon?.render(labelRibbon)

        val marginStart =
            if (labelRibbon != null) ribbon?.additionalMarginStart() ?: 0
            else 0

        cardContainer?.layoutParams = cardContainer?.layoutParams?.apply {
            val marginLayoutParams = this as? ViewGroup.MarginLayoutParams
            marginLayoutParams?.marginStart = marginStart
        }
    }

    private fun renderSafeContent(productCardModel: ProductCardModel) {
        safeGroup?.showWithCondition(productCardModel.isSafeProduct)
    }

    private fun renderAddToCart(productCardModel: ProductCardModel) {
        val cardConstraintLayout = cardConstraintLayout ?: return

        view.showView(R.id.productCardAddToCart, productCardModel.hasAddToCart) {
            AddToCartButton(cardConstraintLayout, type.bottomViewId())
        }
    }

    companion object {
        private val SPANNABLE_FACTORY = object: Spannable.Factory() {
            override fun newSpannable(source: CharSequence?): Spannable {
                return if (source is Spannable) source
                else super.newSpannable(source)
            }
        }
    }
}
