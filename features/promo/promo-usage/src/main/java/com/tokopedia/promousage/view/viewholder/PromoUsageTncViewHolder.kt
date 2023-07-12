package com.tokopedia.promousage.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.promousage.R
import com.tokopedia.promousage.databinding.LayoutPromoTncBinding
import com.tokopedia.promousage.view.model.PromoTncUiModel

class PromoUsageTncViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_promo_tnc
    }

    private val binding: LayoutPromoTncBinding = LayoutPromoTncBinding.bind(itemView)

    fun bind(promoTncUiModel: PromoTncUiModel) {

    }
}
