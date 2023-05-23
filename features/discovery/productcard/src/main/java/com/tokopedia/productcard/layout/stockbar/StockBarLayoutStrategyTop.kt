package com.tokopedia.productcard.layout.stockbar

import android.view.View
import android.view.ViewGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.R
import com.tokopedia.productcard.utils.renderStockBar
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifyprinciples.Typography

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

        backgroundTopStockBar?.showWithCondition(productCardModel.isStockBarShown())

        renderStockBar(progressBarStockTop, textViewStockLabelTop, productCardModel)
    }
}
