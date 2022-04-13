package com.tokopedia.tokofood.home.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.home.presentation.uimodel.TokoFoodHomeNoPinPoinUiModel

class TokoFoodHomeNoPinPoinViewHolder (
    itemView: View
): AbstractViewHolder<TokoFoodHomeNoPinPoinUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokofood_no_pin_poin
    }

    override fun bind(element: TokoFoodHomeNoPinPoinUiModel) {

    }
}