package com.tokopedia.productcard.reimagine

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.kotlin.extensions.view.toDp
import com.tokopedia.productcard.R
import com.tokopedia.productcard.reimagine.ProductCardModel.StockInfo
import com.tokopedia.productcard.utils.FIRE_HEIGHT
import com.tokopedia.productcard.utils.FIRE_WIDTH
import com.tokopedia.productcard.utils.WORDING_LAGI_DIMINATI
import com.tokopedia.productcard.utils.WORDING_SEGERA_HABIS
import com.tokopedia.productcard.utils.WORDING_TERSEDIA
import com.tokopedia.productcard.utils.safeParseColor
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.productcard.R as productcardR

internal class ProductCardStockInfo(view: View) {

    private val context = view.context
    private val background by view.lazyView<View?>(R.id.productCardStockInfoBackground)
    private val label by view.lazyView<Typography?>(R.id.productCardStockInfoLabel)
    private val progressBar by view.lazyView<ProgressBarUnify?>(R.id.productCardStockInfoBar)

    fun render(productModel: ProductCardModel) {
        val stockInfo = productModel.stockInfo()
        val hasStockInfo = stockInfo != null

        background?.showWithCondition(hasStockInfo)

        progressBar?.shouldShowWithAction(hasStockInfo) {
            renderProgressBar(it, stockInfo)
        }

        label?.shouldShowWithAction(hasStockInfo) {
            renderLabel(it, stockInfo)
        }
    }

    private fun renderProgressBar(progressBarStock: ProgressBarUnify?, stockInfo: StockInfo?) {
        if (progressBarStock == null) return

        if (stockInfo?.label.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            progressBarStock.setProgressIcon(
                icon = ContextCompat.getDrawable(
                    context,
                    R.drawable.product_card_ic_stockbar_progress_top
                ),
                width = context.resources.getDimension(FIRE_WIDTH).toInt(),
                height = context.resources.getDimension(FIRE_HEIGHT).toInt()
            )
            progressBarStock.translationY =
                -(context.resources.getDimension(FIRE_HEIGHT).toDp() - ProgressBarUnify.SIZE_MEDIUM)
        } else {
            progressBarStock.setProgressIcon(
                icon = null,
                offsetY = 0f,
                width = ProgressBarUnify.SIZE_MEDIUM,
                height = ProgressBarUnify.SIZE_MEDIUM,
            )
            progressBarStock.translationY = 0f
        }

        val color = ContextCompat.getColor(context, progressBarColor(stockInfo))
        progressBarStock.progressBarColor = intArrayOf(color, color)
        progressBarStock.progressBarTrackColor = ContextCompat.getColor(context, productcardR.color.product_card_bg_stock_info_stockbar_track_color)

        progressBarStock.setValue(stockInfo?.percentage.orZero(), false)
    }

    private fun progressBarColor(stockInfo: StockInfo?): Int {
        return when (stockInfo?.label) {
            WORDING_SEGERA_HABIS -> unifyprinciplesR.color.Unify_RN500
            WORDING_LAGI_DIMINATI -> unifyprinciplesR.color.Unify_YN500
            WORDING_TERSEDIA -> unifyprinciplesR.color.Unify_YN300
            else -> unifyprinciplesR.color.Unify_RN500
        }
    }

    private fun renderLabel(textViewStockLabel: Typography?, stockInfo: StockInfo?) {
        if (textViewStockLabel == null) return

        textViewStockLabel.text = labelText(stockInfo)
        textViewStockLabel.setTextColor(labelColor())
    }

    private fun labelText(stockInfo: StockInfo?): String =
        if (stockInfo?.label?.isNotBlank() == true) stockInfo.label
        else WORDING_TERSEDIA

    private fun labelColor(stockInfo: StockInfo?) = when {
        stockInfo?.labelColor?.isNotEmpty() == true ->
            safeParseColor(
                stockInfo.labelColor,
                ContextCompat.getColor(context, productcardR.color.dms_stock_info_label_color)
            )
        else ->
            ContextCompat.getColor(context, productcardR.color.dms_stock_info_label_color)
    }
    private fun labelColor() = ContextCompat.getColor(context, unifyprinciplesR.color.Unify_NN600)

    companion object{
        private const val PROGRESS_BAR_RADIUS_CLIP = 16f
    }
}
