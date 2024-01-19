package com.tokopedia.productcard.experiments

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
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
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.productcard.reimagine.ProductCardModel.Companion as ProductCardModelReimagine
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
    private val imageView by lazyView<ImageView?>(R.id.productCardImage)

    override fun additionalMarginStart() = cardContainer?.marginStart ?: 0

    override fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int?) {
        View.inflate(context, R.layout.product_card_reimagine_list_carousel_layout, productCardView)

        CompatPaddingUtils(context, productCardView.layoutParams, attrs).updateMargin()

        cardContainer?.run {
            updateLayoutParams { height = MATCH_PARENT }

            elevation = 0f
            radius = 0f

            setCardUnifyBackgroundColor(
                ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN0)
            )
        }
    }

    override fun setProductModel(productCardModel: ProductCardModel) {
        setProductModel(ProductCardModelReimagine.from(productCardModel))
    }

    fun setProductModel(productCardModel: com.tokopedia.productcard.reimagine.ProductCardModel) {
        renderer.setProductModel(productCardModel)

        stockInfo.render(productCardModel)
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
