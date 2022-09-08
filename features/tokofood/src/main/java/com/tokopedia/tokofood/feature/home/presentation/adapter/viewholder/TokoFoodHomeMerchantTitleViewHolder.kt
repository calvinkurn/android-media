package com.tokopedia.tokofood.feature.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.home.presentation.uimodel.TokoFoodHomeMerchantTitleUiModel

class TokoFoodHomeMerchantTitleViewHolder (
    itemView: View
): AbstractViewHolder<TokoFoodHomeMerchantTitleUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_merchant_list_title
    }

    override fun bind(element: TokoFoodHomeMerchantTitleUiModel) {}
}