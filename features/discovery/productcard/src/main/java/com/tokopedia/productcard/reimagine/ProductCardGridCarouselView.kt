package com.tokopedia.productcard.reimagine

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.R
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ProductCardGridCarouselView: ConstraintLayout {
    private val renderer = ProductCardRenderer(this, GridCarousel)
    private val stockInfo = ProductCardStockInfo(this)

    private val cardContainer by lazyView<CardUnify2?>(R.id.productCardCardUnifyContainer)
    private val cardConstraintLayout by lazyView<ConstraintLayout?>(R.id.productCardConstraintLayout)
    private val imageView by lazyView<ImageUnify?>(R.id.productCardImage)

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
            layoutParams = layoutParams?.apply { height = MATCH_PARENT }
            elevation = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }
    }

    fun setProductModel(productCardModel: ProductCardModel) {
        renderer.setProductModel(productCardModel)

        renderAddToCart(productCardModel)
        stockInfo.render(productCardModel)
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
}
