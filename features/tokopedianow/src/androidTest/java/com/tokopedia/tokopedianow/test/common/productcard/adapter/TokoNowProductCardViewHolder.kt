package com.tokopedia.tokopedianow.test.common.productcard.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.view.productcard.TokoNowProductCardView
import com.tokopedia.tokopedianow.test.R

internal class TokoNowProductCardViewHolder(
    itemView: View
): RecyclerView.ViewHolder(itemView) {

    private val productCard: TokoNowProductCardView? by lazy {
        itemView.findViewById(R.id.product_card)
    }

    fun bind(model: TokoNowProductCardViewUiModel) {
        productCard?.apply {
            setData(model)
        }
    }
}
