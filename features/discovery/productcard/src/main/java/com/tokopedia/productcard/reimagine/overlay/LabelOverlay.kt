package com.tokopedia.productcard.reimagine.overlay

import android.content.Context
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_1
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_2
import com.tokopedia.productcard.reimagine.LABEL_OVERLAY_3
import com.tokopedia.productcard.reimagine.ProductCardModel
import com.tokopedia.productcard.reimagine.ProductCardModel.LabelGroup
import com.tokopedia.productcard.reimagine.lazyView
import com.tokopedia.productcard.utils.RoundedCornersTransformation.CornerType.BOTTOM_LEFT
import com.tokopedia.productcard.utils.imageRounded
import com.tokopedia.productcard.utils.loadImage
import com.tokopedia.unifycomponents.toPx

internal class LabelOverlay(view: View) {

    private val context: Context? = view.context
    private val labelOverlay1 by view.lazyView<ImageView>(R.id.productCardOverlay1)
    private val labelOverlay2 by view.lazyView<ImageView>(R.id.productCardOverlay2)
    private val labelOverlay3 by view.lazyView<ImageView>(R.id.productCardOverlay3)
    private val widthMap = mapOf(
        LABEL_OVERLAY_1 to 48.toPx(),
        LABEL_OVERLAY_2 to 48.toPx(),
        LABEL_OVERLAY_3 to 32.toPx(),
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
        val widthPx = widthMap[labelGroup.position] ?: 0

        renderImage(imageView, labelGroup.imageUrl, widthPx, hasStockInfo, marginStartPx)

        val nextMarginStart = marginStartPx + widthPx - MARGIN_OVERLAP_DP.toPx()
        renderNextLabel(imageViewList, labelGroupList, hasStockInfo, nextMarginStart)
    }

    private fun renderImage(
        imageView: ImageView,
        imageUrl: String,
        widthPx: Int,
        hasStockInfo: Boolean,
        marginStartPx: Int,
    ) {
        imageView.show()

        val lp = imageView.layoutParams as? MarginLayoutParams ?: marginLayoutParams()
        lp.width = widthPx
        lp.marginStart = marginStartPx

        imageView.layoutParams = lp

        loadImage(imageView, marginStartPx, hasStockInfo, imageUrl)
    }

    private fun loadImage(
        imageView: ImageView,
        marginStartPx: Int,
        hasStockInfo: Boolean,
        imageUrl: String
    ) {
        if (marginStartPx == Int.ZERO && !hasStockInfo)
            imageView.imageRounded(
                url = imageUrl,
                radius = context?.resources?.getDimensionPixelSize(
                    R.dimen.product_card_reimagine_image_radius
                )?.toFloat() ?: 0f,
                cornerType = BOTTOM_LEFT,
            )
        else
            imageView.loadImage(imageUrl)
    }

    companion object {
        private const val MARGIN_OVERLAP_DP = 4
        private fun marginLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }
}
