package com.tokopedia.productcard_compact.productcard.presentation.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productcard_compact.productcard.presentation.customview.TokoNowProductCardView
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.productcard_compact.test.R

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
