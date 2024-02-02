package com.tokopedia.productcard.reimagine.overlay

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.data.Resize
import com.tokopedia.media.loader.loadImage
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_1
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_2
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_3
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup
import com.tokopedia.productcard.reimagine.ProductCardType
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.RoundedCornersTransformation
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.BOTTOM_LEFT
import com.tokopedia.unifycomponents.toPx

internal class LabelOverlay(
    view: View,
    private val type: ProductCardType
) {

    private val context: Context? = view.context
    private val labelOverlay1 by view.lazyView<ImageView>(R.id.productCardOverlay1)
    private val labelOverlay2 by view.lazyView<ImageView>(R.id.productCardOverlay2)
    private val labelOverlay3 by view.lazyView<ImageView>(R.id.productCardOverlay3)
    private val regularSizeMap = mapOf(
        LABEL_OVERLAY_1 to (48.toPx() to 20.toPx()),
        LABEL_OVERLAY_2 to (48.toPx() to 20.toPx()),
        LABEL_OVERLAY_3 to (32.toPx() to 20.toPx()),
    )

    fun render(productCardModel: ProductCardModel) {
        val labelOverlayViewList = listOf(labelOverlay1, labelOverlay2, labelOverlay3)
        val labelGroupOverlayList = productCardModel.labelGroupOverlayList()

        labelOverlayViewList.forEach { it?.hide() }

        renderNextLabel(
            labelOverlayViewList.iterator(),
            labelGroupOverlayList.iterator(),
            productCardModel.stockInfo() != null,
        )
    }

    private tailrec fun renderNextLabel(
        imageViewList: Iterator<ImageView?>,
        labelGroupList: Iterator<LabelGroup>,
        hasStockInfo: Boolean,
        marginStartPx: Int = 0,
    ) {
        if (!imageViewList.hasNext() || !labelGroupList.hasNext()) return

        val imageView = imageViewList.next() ?: return
        val labelGroup = labelGroupList.next()
        val sizeMultiplier = sizeMultiplier()
        val widthPx = ((regularSizeMap[labelGroup.position]?.first ?: 0) * sizeMultiplier).toInt()
        val heightPx = ((regularSizeMap[labelGroup.position]?.second ?: 0) * sizeMultiplier).toInt()

        renderImage(imageView, labelGroup.imageUrl, widthPx, heightPx, hasStockInfo, marginStartPx)

        val marginOverlap = (MARGIN_OVERLAP_DP.toPx() * sizeMultiplier).toInt()
        val nextMarginStart = marginStartPx + widthPx - marginOverlap
        renderNextLabel(imageViewList, labelGroupList, hasStockInfo, nextMarginStart)
    }

    private fun sizeMultiplier(): Float =
        if (type == ProductCardType.GridCarousel || type == ProductCardType.ListCarousel)
            CAROUSEL_SIZE_MULTIPLIER
        else 1f

    private fun renderImage(
        imageView: ImageView,
        imageUrl: String,
        widthPx: Int,
        heightPx: Int,
        hasStockInfo: Boolean,
        marginStartPx: Int,
    ) {
        imageView.show()

        val lp = imageView.layoutParams as? MarginLayoutParams ?: marginLayoutParams()
        lp.width = widthPx
        lp.height = heightPx
        lp.marginStart = marginStartPx

        imageView.layoutParams = lp

        imageView.loadImage(imageUrl) {
            overrideSize(Resize(widthPx, heightPx))

            if (marginStartPx == Int.ZERO && !hasStockInfo)
                transform(RoundedCornersTransformation(bottomLeftRadius(), BOTTOM_LEFT))
        }
    }

    private fun bottomLeftRadius() =
        context?.resources?.getDimensionPixelSize(R.dimen.product_card_reimagine_image_radius) ?: 0

    companion object {
        private const val MARGIN_OVERLAP_DP = 4
        private const val CAROUSEL_SIZE_MULTIPLIER = 0.8f
        private fun marginLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }
}
