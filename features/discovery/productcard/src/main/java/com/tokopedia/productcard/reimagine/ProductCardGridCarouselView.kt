package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.setTextAndContentDescription
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.strikethrough
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.loadImageRounded
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class ProductCardGridCarouselView: ConstraintLayout {

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val imageView by lazyView<ImageUnify?>(R.id.productCardImage)
    private val adsText by lazyView<Typography?>(R.id.productCardAds)
    private val nameText by lazyView<Typography?>(R.id.productCardName)
    private val priceText by lazyView<Typography?>(R.id.productCardPrice)
    private val slashedPriceText by lazyView<Typography?>(R.id.productCardSlashedPrice)
    private val discountText by lazyView<Typography?>(R.id.productCardDiscount)
    private val benefitLabel by lazyView<Typography?>(R.id.productCardLabelBenefit)
    private val credibilitySection by lazyView<LinearLayout?>(R.id.productCardCredibility)
    private val shopSection by lazyView<LinearLayout?>(R.id.productCardShopSection)
    private val freeShippingImage by lazyView<ImageView?>(R.id.productCardFreeShipping)

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet? = null) {
        View.inflate(context, R.layout.product_card_reimagine_grid_carousel_layout, this)

        layoutParams = LayoutParams(
            context.resources.getDimensionPixelSize(
                R.dimen.product_card_reimagine_carousel_width
            ),
            MATCH_PARENT,
        )

        cardContainer?.run {
            layoutParams = cardContainer?.layoutParams?.apply {
                height = MATCH_PARENT
            }

            elevation = 0f
        }
    }

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
    }

    private fun renderImage(productCardModel: ProductCardModel) {
        imageView?.loadImageRounded(
            productCardModel.imageUrl,
            context.resources.getDimensionPixelSize(
                R.dimen.product_card_reimagine_carousel_image_radius
            ).toFloat(),
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
            it.maxLines = if (productCardModel.hasMultilineName) 2 else 1
        }
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
        slashedPriceText?.shouldShowWithAction(productCardModel.slashedPrice.isNotEmpty(),) {
            it.setTextAndContentDescription(
                productCardModel.slashedPrice,
                R.string.content_desc_textViewSlashedPrice
            )
            it.strikethrough()
            it.requestLayout()
        }
    }

    private fun renderDiscountPercentage(productCardModel: ProductCardModel) {
        discountText?.shouldShowWithAction(
            productCardModel.discountPercentage != 0,
        ) {
            it.setTextAndContentDescription(
                "${productCardModel.discountPercentage}%",
                R.string.content_desc_labelDiscount
            )
        }
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
        val hasLabelCredibility = labelCredibility?.hasTitle() == true
        val hasCredibilitySection = hasRating || hasLabelCredibility
        credibilitySection?.shouldShowWithAction(hasCredibilitySection) {
            findViewById<IconUnify?>(R.id.productCardRatingIcon)?.showWithCondition(hasRating)
            findViewById<Typography?>(R.id.productCardRating)?.shouldShowWithAction(hasRating) {
                it.text = productCardModel.rating
            }

            findViewById<Typography?>(R.id.productCardLabelCredibility)?.shouldShowWithAction(
                hasLabelCredibility
            ) {
                it.text = labelCredibility?.title ?: ""
            }

            val ratingDotsVisibility = hasRating && hasLabelCredibility
            findViewById<Typography?>(R.id.productCardRatingDots)?.showWithCondition(
                ratingDotsVisibility
            )
        }
    }

    private fun renderShopSection(productCardModel: ProductCardModel) {
        val shopBadge = productCardModel.shopBadge
        val hasShopSection = shopBadge.hasTitle()
        shopSection?.shouldShowWithAction(hasShopSection) {
            val hasIcon = shopBadge.hasImage()
            findViewById<ImageView?>(R.id.productCardShopBadge)?.shouldShowWithAction(hasIcon) {
                it.loadIcon(shopBadge.imageUrl)
            }

            findViewById<Typography?>(R.id.productCardShopNameLocation)?.text = shopBadge.title
        }
    }

    private fun renderFreeShipping(productCardModel: ProductCardModel) {
        val freeShippingImageUrl = productCardModel.freeShipping.imageUrl
        freeShippingImage?.shouldShowWithAction(freeShippingImageUrl.isNotEmpty()) {
            it.loadIcon(freeShippingImageUrl)
        }
    }

    fun addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
        imageView?.addOnImpressionListener(holder, onView)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cardContainer?.setOnClickListener(l)
    }
}
