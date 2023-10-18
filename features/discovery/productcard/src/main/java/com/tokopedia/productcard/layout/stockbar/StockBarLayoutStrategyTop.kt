package com.tokopedia.productcard.layout.stockbar

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.FIRE_HEIGHT
import com.tokopedia.productcard.utils.FIRE_WIDTH
import com.tokopedia.productcard.utils.WORDING_LAGI_DIMINATI
import com.tokopedia.productcard.utils.WORDING_TERSEDIA
import com.tokopedia.productcard.utils.WORDING_SEGERA_HABIS
import com.tokopedia.productcard.utils.getStockLabelColor
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyR

internal class StockBarLayoutStrategyTop: StockBarLayoutStrategy {

    override fun renderStockBar(
        productCardModel: ProductCardModel,
        productCardViewContainer: ViewGroup
    ) {
        val backgroundTopStockBar: View? =
            productCardViewContainer.findViewById(R.id.bgTopStockBarView)
        val progressBarStockTop: ProgressBarUnify? =
            productCardViewContainer.findViewById(R.id.progressBarTopStockBar)
        val textViewStockLabelTop: Typography? =
            productCardViewContainer.findViewById(R.id.textViewTopStockBar)

        val progressBarStock: ProgressBarUnify? =
            productCardViewContainer.findViewById(R.id.progressBarStock)
        val textViewStockLabel: Typography? =
            productCardViewContainer.findViewById(R.id.textViewStockLabel)

        progressBarStock?.hide()
        textViewStockLabel?.hide()
        backgroundTopStockBar?.show()
        textViewStockLabelTop?.show()
        progressBarStockTop?.show()

        renderStockPercentage(progressBarStockTop, productCardModel)
        renderStockLabel(textViewStockLabelTop, productCardModel)
    }

    private fun renderStockPercentage(
        progressBarStock: ProgressBarUnify?,
        productCardModel: ProductCardModel,
    ) {
        if(progressBarStock == null) return
        progressBarStock.progressBarHeight = ProgressBarUnify.SIZE_MEDIUM
        if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            progressBarStock.setProgressIcon(
                icon = ContextCompat.getDrawable(
                    progressBarStock.context,
                    com.tokopedia.productcard.R.drawable.product_card_ic_stockbar_progress_top
                ),
                width = progressBarStock.context.resources.getDimension(FIRE_WIDTH).toInt(),
                height = progressBarStock.context.resources.getDimension(FIRE_HEIGHT).toInt()
            )
            progressBarStock.translationY = -(progressBarStock.context.resources.getDimension(com.tokopedia.productcard.utils.FIRE_HEIGHT).toDp() - ProgressBarUnify.SIZE_MEDIUM)
        } else {
            progressBarStock.setProgressIcon(null, 0f, ProgressBarUnify.SIZE_MEDIUM, ProgressBarUnify.SIZE_MEDIUM)
            progressBarStock.translationY = 0f
        }
        val color = ContextCompat.getColor(progressBarStock.context, getProgressBarColor(productCardModel))
        progressBarStock.progressBarColor = intArrayOf(color, color)
        progressBarStock.setValue(productCardModel.stockBarPercentage, false)
    }

    private fun getProgressBarColor(productCardModel: ProductCardModel): Int {
        return when(productCardModel.stockBarLabel) {
            WORDING_SEGERA_HABIS -> unifyR.color.Unify_RN500
            WORDING_LAGI_DIMINATI -> unifyR.color.Unify_YN500
            WORDING_TERSEDIA -> unifyR.color.Unify_YN300
            else -> unifyR.color.Unify_RN500
        }
    }

    private fun renderStockLabel(
        textViewStockLabel: Typography?,
        productCardModel: ProductCardModel,
    ) {
        if(textViewStockLabel == null) return
        if(productCardModel.stockBarLabel.isEmpty()) {
            textViewStockLabel.text = WORDING_TERSEDIA
        } else textViewStockLabel.text = productCardModel.stockBarLabel

        val color = getStockLabelColor(productCardModel, textViewStockLabel)
        textViewStockLabel.setTextColor(color)
    }
}
