package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.CompatPaddingUtils
import com.tokopedia.productcard.reimagine.ProductCardRenderer
import com.tokopedia.productcard.reimagine.ProductCardStockInfo
import com.tokopedia.productcard.reimagine.ProductCardType.GridCarousel
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.unifycomponents.CardUnify2
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

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val productCardOutlineCard by lazyView<View?>(R.id.productCardOutline)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)
    private val productCardGuidelineStartContent by lazyView<Guideline?>(R.id.productCardGuidelineStartContent)
    private val productCardGuidelineEndContent by lazyView<Guideline?>(R.id.productCardGuidelineEndContent)

    val additionalMarginStart: Int
        get() = cardContainer?.marginStart ?: 0

    private var useCompatPadding = false

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        View.inflate(context, R.layout.product_card_reimagine_grid_carousel_layout, productCardView)

        initAttributes(attrs)

        cardContainer?.run {
            updateLayoutParams { height = MATCH_PARENT }
            elevation = 0f
            radius = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }
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

        renderGuidelineContent(productCardModel)
        setContainerProductHeightCard(productCardModel)
        renderOutlineProductCard(productCardModel)

        CompatPaddingUtils(productCardView, useCompatPadding, productCardModel).updatePadding()
    }

    private fun renderGuidelineContent(productCardModel: ProductCardModelReimagine) {
        if (productCardModel.isInBackground) {
            setGuidelineItemInBackground()
        } else {
            setGuidelineItemNotInBackground()
        }
    }

    private fun setGuidelineItemNotInBackground() {
        productCardGuidelineStartContent?.setGuidelineBegin(0)
        productCardGuidelineEndContent?.setGuidelineEnd(0)
    }

    private fun setGuidelineItemInBackground() {
        val contextResource = context?.resources
        val dimensGuideline =
            contextResource?.getDimensionPixelSize(R.dimen.product_card_reimagine_content_guideline_pading_in_background)
                ?: 0
        productCardGuidelineStartContent?.setGuidelineBegin(dimensGuideline)
        productCardGuidelineEndContent?.setGuidelineEnd(dimensGuideline)
    }

    private fun renderOutlineProductCard(productCardModel: ProductCardModelReimagine) {
        productCardOutlineCard?.showWithCondition(productCardModel.isInBackground)
    }

    private fun setContainerProductHeightCard(productCardModel: ProductCardModelReimagine) {
        val contextResource = context?.resources
        val dimensMarginBottom =
            contextResource?.getDimensionPixelSize(R.dimen.product_card_reimagine_carousel_padding_bottom) ?: 0
        val isInBackground = productCardModel.isInBackground
        val marginBottom = if (isInBackground) 0 else dimensMarginBottom

        cardConstraintLayout?.setPadding(0,0,0, marginBottom)
    }

    override fun recycle() { }

    override fun setImageProductViewHintListener(
        impressHolder: ImpressHolder,
        viewHintListener: ViewHintListener,
    ) {
        imageView?.addOnImpressionListener(impressHolder, viewHintListener)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        cardContainer?.setOnClickListener(l)
    }

    override fun setAddToCartOnClickListener(l: View.OnClickListener?) {
        productCardView
            .findViewById<View?>(R.id.productCardAddToCart)
            ?.setOnClickListener(l)
    }
}
