package com.tokopedia.productcard.compact.productcard.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard.compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard.compact.productcard.presentation.listener.ProductCardCompactViewListener
import com.tokopedia.productcard.compact.productcard.presentation.uimodel.ProductCardCompactUiModel
import com.tokopedia.productcard.compact.test.R as productcardcompacttestR

internal class ProductCardCompactProductCardViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val productCard: ProductCardCompactView? by lazy {
        itemView.findViewById(productcardcompacttestR.id.product_card)
    }

    fun bind(model: ProductCardCompactUiModel) {
        productCard?.apply {
            bind(model, ProductCardCompactViewListener())
        }
    }
}
