package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.setPadding
import androidx.core.view.updateLayoutParams
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.CompatPaddingUtils
import com.tokopedia.productcard.reimagine.ProductCardRenderer
import com.tokopedia.productcard.reimagine.ProductCardStockInfo
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.getPixel
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.productcard.reimagine.ProductCardModel as ProductCardModelReimagine
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

internal class ReimagineListCarouselViewStrategy(
    private val productCardView: ViewGroup,
): ProductCardStrategy {
    private val context: Context?
        get() = productCardView.context

    private fun <T: View?> lazyView(@IdRes id: Int) = productCardView.lazyView<T>(id)

    private val renderer = ProductCardRenderer(productCardView, ProductCardType.ListCarousel)
    private val stockInfo = ProductCardStockInfo(productCardView)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)

    override fun additionalMarginStart() = cardContainer?.marginStart ?: 0

    private var useCompatPadding = false

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        View.inflate(context, R.layout.product_card_reimagine_list_carousel_layout, productCardView)

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

        renderCardPadding(productCardModel)

        CompatPaddingUtils(productCardView, useCompatPadding, productCardModel).updatePadding()
    }

    private fun renderCardPadding(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        val guidelinePadding =
            if (productCardModel.isInBackground)
                context.getPixel(R.dimen.product_card_reimagine_content_guideline_padding_in_background)
            else
                0

        cardConstraintLayout?.setPadding(guidelinePadding)
    }

    override fun recycle() { }

    override fun setImageProductViewHintListener(
        impressHolder: ImpressHolder,
        viewHintListener: ViewHintListener
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
