package com.tokopedia.productcard

import android.view.View
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.productcard.utils.FIRE_HEIGHT
import com.tokopedia.productcard.utils.FIRE_WIDTH
import com.tokopedia.productcard.utils.WORDING_SEGERA_HABIS
import com.tokopedia.productcard.utils.safeParseColor
import com.tokopedia.productcard.utils.shouldShowWithAction
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.product_card_footer_layout.view.*

internal fun View.renderProductCardFooter(
    productCardModel: ProductCardModel,
    isProductCardList: Boolean,
) {
    renderStockBar(progressBarStock, textViewStockLabel, productCardModel)

    buttonNotify?.showWithCondition(productCardModel.hasNotifyMeButton)

    if (isProductCardList) {
        val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
        buttonDeleteProduct?.showWithCondition(productCardModel.hasDeleteProductButton)
        buttonRemoveFromWishlist?.showWithCondition(productCardModel.hasRemoveFromWishlistButton)
        buttonSimilarProduct?.hide()
    } else {
        buttonDeleteProduct?.hide()
        buttonRemoveFromWishlist?.hide()
        renderSimilarProductButton(productCardModel)
    }
}

internal fun renderStockBar(progressBarStock: ProgressBarUnify?, textViewStock: Typography?, productCardModel: ProductCardModel) {
    renderStockPercentage(progressBarStock, productCardModel)
    renderStockLabel(textViewStock, productCardModel)
}

private fun renderStockPercentage(progressBarStock: ProgressBarUnify?, productCardModel: ProductCardModel) {
    progressBarStock?.shouldShowWithAction(productCardModel.isStockBarShown()) {
        it.setProgressIcon(icon = null)
        if (productCardModel.stockBarLabel.equals(WORDING_SEGERA_HABIS, ignoreCase = true)) {
            it.setProgressIcon(
                icon = ContextCompat.getDrawable(it.context, R.drawable.product_card_ic_fire_filled),
                width = it.context.resources.getDimension(FIRE_WIDTH).toInt(),
                height = it.context.resources.getDimension(FIRE_HEIGHT).toInt())
        }
        it.progressBarColorType = ProgressBarUnify.COLOR_RED
        it.setValue(productCardModel.stockBarPercentage, false)
    }
}

private fun renderStockLabel(textViewStockLabel: Typography?, productCardModel: ProductCardModel) {
    textViewStockLabel?.shouldShowWithAction(productCardModel.isStockBarShown()) {
        it.text = productCardModel.stockBarLabel

        val color = getStockLabelColor(productCardModel, it)
        it.setTextColor(color)
    }
}

private fun getStockLabelColor(productCardModel: ProductCardModel, it: Typography) =
    when {
        productCardModel.stockBarLabelColor.isNotEmpty() ->
            safeParseColor(
                productCardModel.stockBarLabelColor,
                ContextCompat.getColor(it.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
            )
        else ->
            MethodChecker.getColor(it.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
    }

private fun View.renderSimilarProductButton(productCardModel: ProductCardModel) {
    val buttonSimilarProduct = findViewById<UnifyButton?>(R.id.buttonSeeSimilarProduct)
    buttonSimilarProduct?.showWithCondition(productCardModel.hasSimilarProductButton)
}
