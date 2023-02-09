package com.tokopedia.tokopedianow.test.common.productcard.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard_compact.productcard.presentation.customview.ProductCardCompactView
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.test.R

internal class TokoNowProductCardViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val productCard: ProductCardCompactView? by lazy {
        itemView.findViewById(R.id.product_card)
    }

    fun bind(model: TokoNowProductCardViewUiModel) {
        productCard?.apply {
            setData(model)
        }
    }
}
