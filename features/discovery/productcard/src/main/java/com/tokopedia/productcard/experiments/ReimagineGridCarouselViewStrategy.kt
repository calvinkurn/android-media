package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.CompatPaddingUtils
import com.tokopedia.productcard.reimagine.ProductCardRenderer
import com.tokopedia.productcard.reimagine.ProductCardStockInfo
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.ProductCardType.GridCarousel
import com.tokopedia.productcard.reimagine.cart.ProductCardCartExtension
import com.tokopedia.productcard.reimagine.cta.ProductCardGenericCtaExtension
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ReimagineGridCarouselViewStrategy(
    private val productCardView: ViewGroup,
): ProductCardStrategy {

    private val context: Context?
        get() = productCardView.context

    private fun <T: View?> lazyView(@IdRes id: Int) = productCardView.lazyView<T>(id)

    private val renderer = ProductCardRenderer(productCardView, GridCarousel)
    private val stockInfo = ProductCardStockInfo(productCardView)
    private val cartExtension = ProductCardCartExtension(productCardView, GridCarousel)
    private val genericCtaExtension = ProductCardGenericCtaExtension(productCardView, ProductCardType.Grid)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)
    private val nameText by lazyView<Typography?>(R.id.productCardName)
    private val guidelineStart by lazyView<Guideline?>(R.id.productCardGuidelineStartContent)
    private val guidelineEnd by lazyView<Guideline?>(R.id.productCardGuidelineEndContent)
    private val guidelineBottom by lazyView<Guideline?>(R.id.productCardGuidelineBottomContent)

    private val productCardPriceContainer by lazyView<RelativeLayout?>(R.id.productCardPriceContainer)
    private val productCardSlashedPrice by lazyView<Typography?>(R.id.productCardSlashedPrice)
    private val productCardDiscount by lazyView<Typography?>(R.id.productCardDiscount)
    private val productCardCredibility by lazyView<LinearLayout?>(R.id.productCardCredibility)
    private val productCardShopSection by lazyView<LinearLayout?>(R.id.productCardShopSection)

    val additionalMarginStart: Int
        get() = cardContainer?.marginStart ?: 0

    private var useCompatPadding = false

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        View.inflate(context, R.layout.product_card_reimagine_grid_carousel_layout, productCardView)

        initAttributes(attrs)

        cardContainer?.run {
            updateLayoutParams { height = MATCH_PARENT }
            elevation = 0f
            radius = context.getPixel(R.dimen.product_card_reimagine_image_radius).toFloat()
            cornerRadius = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }

        nameText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, 12.toPx().toFloat())
    }

    private fun initAttributes(attrs: AttributeSet?) {
        attrs ?: return

        val typedArray = context
            ?.obtainStyledAttributes(attrs, R.styleable.ProductCardView, 0, 0)
            ?: return

        return try {
            useCompatPadding = typedArray.getBoolean(R.styleable.ProductCardView_useCompatPadding, false)
        } catch(_: Throwable) {

        } finally {
            typedArray.recycle()
        }
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        setProductModel(ProductCardModelReimagine.from(productCardModel))
    }

    fun setProductModel(productCardModel: ProductCardModelReimagine) {
        renderer.setProductModel(productCardModel)

        stockInfo.render(productCardModel)

        genericCtaExtension.render(productCardModel)
        cartExtension.render(productCardModel)

        renderContentPadding(productCardModel)

        CompatPaddingUtils(productCardView, useCompatPadding, productCardModel).updatePadding()
    }

    private fun renderContentPadding(productCardModel: ProductCardModelReimagine) {
        renderContentPaddingHorizontal(productCardModel)
        renderContentPaddingBottom(productCardModel)
    }

    private fun renderContentPaddingHorizontal(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        val paddingHorizontal =
            if (productCardModel.isInBackground)
                context?.getPixel(R.dimen.product_card_reimagine_content_guideline_padding_in_background)
                    ?: 0
            else 0

        guidelineStart?.setGuidelineBegin(paddingHorizontal)
        guidelineEnd?.setGuidelineEnd(paddingHorizontal)
    }

    private fun renderContentPaddingBottom(productCardModel: ProductCardModelReimagine) {
        val paddingBottomConstraintLayout =
            if (productCardModel.isInBackground) 0
            else context?.getPixel(R.dimen.product_card_reimagine_padding_bottom) ?: 0

        cardConstraintLayout?.let {
            it.setPadding(
                it.paddingStart,
                it.paddingTop,
                it.paddingEnd,
                paddingBottomConstraintLayout
            )
        }

        val paddingBottomGuideline =
            if (productCardModel.isInBackground) context?.getPixel(R.dimen.product_card_reimagine_padding_bottom)
                ?: 0
            else 0

        guidelineBottom?.setGuidelineEnd(paddingBottomGuideline)
    }

    override fun recycle() {
        cartExtension.clear()
    }

    override fun setImageProductViewHintListener(
        impressHolder: ImpressHolder,
        viewHintListener: ViewHintListener,
    ) {
        imageView?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        cardContainer?.setOnClickListener(l)
    }
    override fun setOnClickListener(l: ProductCardClickListener) {
        cardContainer?.setOnClickListener {
            l.onAreaClicked(it)
            l.onClick(it)
        }
        setProductImageOnClickListener {
            l.onProductImageClicked(it)
            l.onClick(it)
        }
        setShopTypeLocationOnClickListener {
            l.onSellerInfoClicked(it)
            l.onClick(it)
        }
    }

    override fun setAddToCartOnClickListener(l: View.OnClickListener?) {
        cartExtension.addToCartClickListener = { l?.onClick(it) }
    }

    override fun setAddToCartNonVariantClickListener(addToCartNonVariantClickListener: ATCNonVariantListener) {
        cartExtension.addToCartNonVariantClickListener = addToCartNonVariantClickListener
    }

    override fun setGenericCtaButtonOnClickListener(l: View.OnClickListener?) {
        genericCtaExtension.ctaClickListener = { l?.onClick(it) }
    }

    override fun setGenericCtaSecondaryButtonOnClickListener(l: View.OnClickListener?) {
        genericCtaExtension.ctaSecondaryClickListener = { l?.onClick(it) }
    }

    override fun reRenderGenericCtaButton(productCardModel: ProductCardModelReimagine) {
        genericCtaExtension.render(productCardModel)
    }

    override fun setProductImageOnClickListener(l: (View) -> Unit) {
        imageView?.setOnClickListener(l)
    }

    override fun setShopTypeLocationOnClickListener(l: (View) -> Unit) {
        productCardShopSection?.setOnClickListener(l)
    }
}
