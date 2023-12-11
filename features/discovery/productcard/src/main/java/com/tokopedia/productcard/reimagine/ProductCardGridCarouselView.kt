package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ProductCardGridCarouselView: ConstraintLayout {
    private val renderer = ProductCardRenderer(this, GridCarousel)
    private val stockInfo = ProductCardStockInfo(this)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val productCardOutlineCard by lazyView<View?>(R.id.productCardOutlineCard)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageUnify?>(R.id.productCardImage)
    private val productCardGuidelineStartContent by lazyView<Guideline?>(R.id.productCardGuidelineStartContent)
    private val productCardGuidelineEndContent by lazyView<Guideline?>(R.id.productCardGuidelineEndContent)

    val additionalMarginStart: Int
        get() = cardContainer?.marginStart ?: 0

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

        cardContainer?.run {
            layoutParams = layoutParams?.apply { height = MATCH_PARENT }
            elevation = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }

        cardConstraintLayout?.run {
            layoutParams = layoutParams?.apply { height = MATCH_PARENT }
        }
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        renderer.setProductModel(productCardModel)

        renderAddToCart(productCardModel)
        stockInfo.render(productCardModel)
        renderMarginContent(productCardModel)
        setContainerProductHeightCard(productCardModel)
        renderOutlineProductCard(productCardModel)
    }


    private fun renderAddToCart(productCardModel: ProductCardModel) {
        val cardConstraintLayout = cardConstraintLayout ?: return

        showView(R.id.productCardAddToCart, productCardModel.hasAddToCart) {
            AddToCartButton(cardConstraintLayout)
        }
    }

    fun addOnImpressionListener(holder: ImpressHolder, onView: () -> Unit) {
        imageView?.addOnImpressionListener(holder, onView)
    }

    fun setAddToCartOnClickListener(onClickListener: OnClickListener) {
        findViewById<View?>(R.id.productCardAddToCart)?.setOnClickListener(onClickListener)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        cardContainer?.setOnClickListener(l)
    }

    private fun renderMarginContent(productCardModel: ProductCardModel){
        if (productCardModel.isInBackground) {
            setMarginItemInBackground()
        } else {
            setMarginItemNotInBackground()
        }
    }

    private fun setMarginItemNotInBackground() {
        productCardGuidelineStartContent?.setGuidelineBegin(0)
        productCardGuidelineEndContent?.setGuidelineEnd(0)
    }

    private fun setMarginItemInBackground() {
        val contextResource = context.resources
        val dimensMarginStart =
            contextResource.getDimensionPixelSize(R.dimen.product_card_reimagine_content_padding_in_background)
        productCardGuidelineStartContent?.setGuidelineBegin(dimensMarginStart)
        productCardGuidelineEndContent?.setGuidelineEnd(dimensMarginStart)
    }

    private fun renderOutlineProductCard(productCardModel: ProductCardModel) {
        productCardOutlineCard?.showWithCondition(productCardModel.isInBackground)
    }
    
    private fun setContainerProductHeightCard(productCardModel: ProductCardModel) {
        val contextResource = context.resources
        val dimensMarginBottom =
            contextResource.getDimensionPixelSize(R.dimen.product_card_reimagine_carousel_padding_bottom)
        val isInBackground = productCardModel.isInBackground
        val marginBottom = if(isInBackground) 0 else dimensMarginBottom

        cardConstraintLayout?.run {
            setPadding(0,0,0, marginBottom)
            layoutParams = layoutParams?.apply { height = if(isInBackground) MATCH_PARENT else WRAP_CONTENT }
        }
    }
}
