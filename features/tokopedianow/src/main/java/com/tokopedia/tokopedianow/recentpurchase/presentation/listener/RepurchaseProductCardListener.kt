package com.tokopedia.tokopedianow.recentpurchase.presentation.listener

import android.content.Context
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseProductViewHolder.RepurchaseProductCardListener

class RepurchaseProductCardListener(private val context: Context): RepurchaseProductCardListener {
    override fun onClickProduct(item: RepurchaseProductUiModel) {
    }

    override fun onAddToCartVariant(item: RepurchaseProductUiModel) {
    }

    override fun onAddToCartNonVariant(item: RepurchaseProductUiModel, quantity: Int) {
    }

    override fun onProductImpressed(item: RepurchaseProductUiModel) {
    }
}
