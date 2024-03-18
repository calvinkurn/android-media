package com.tokopedia.productcard.reimagine.overlay

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Space
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.RoundedCornersTransformation
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.BOTTOM_LEFT
import com.tokopedia.productcard.utils.shouldShowWithAction

internal class LabelOverlay(view: View) {

    private val context: Context? = view.context
    private val labelOverlay1 by view.lazyView<ImageView?>(R.id.productCardOverlay1)
    private val labelOverlay1Space by view.lazyView<Space?>(R.id.productCardOverlay1Space)
    private val labelOverlay2 by view.lazyView<ImageView>(R.id.productCardOverlay2)
    private val labelOverlay2Space by view.lazyView<Space?>(R.id.productCardOverlay2Space)
    private val labelOverlay3 by view.lazyView<ImageView>(R.id.productCardOverlay3)

    data class Components(
        val imageView: ImageView?,
        val space: Space?,
        val labelGroup: LabelGroup?,
    )

    fun render(productCardModel: ProductCardModel) {
        val componentsList = listOf(
            Components(labelOverlay1, labelOverlay1Space, productCardModel.labelOverlay1()),
            Components(labelOverlay2, labelOverlay2Space, productCardModel.labelOverlay2()),
            Components(labelOverlay3, null, productCardModel.labelOverlay3()),
        )

        renderNextLabel(
            componentsList = componentsList.iterator(),
            isFirstLabel = true,
            hasStockInfo = productCardModel.stockInfo() != null,
        )
    }

    private tailrec fun renderNextLabel(
        componentsList: Iterator<Components>,
        isFirstLabel: Boolean,
        hasStockInfo: Boolean,
    ) {
        if (!componentsList.hasNext()) return

        val components = componentsList.next()
        val isShow = components.labelGroup != null

        components.space?.showWithCondition(isShow)
        components.imageView?.shouldShowWithAction(isShow) {
            val labelGroup = components.labelGroup ?: return@shouldShowWithAction
            components.imageView.loadImage(labelGroup.imageUrl) {
                if (isFirstLabel && !hasStockInfo)
                    transform(RoundedCornersTransformation(bottomLeftRadius(), BOTTOM_LEFT))
            }
        }

        renderNextLabel(componentsList, !isShow, hasStockInfo)
    }

    private fun bottomLeftRadius() =
        context?.resources?.getDimensionPixelSize(R.dimen.product_card_reimagine_image_radius) ?: 0
}
