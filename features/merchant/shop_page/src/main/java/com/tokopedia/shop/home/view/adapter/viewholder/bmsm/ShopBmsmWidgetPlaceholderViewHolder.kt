package com.tokopedia.shop.home.view.adapter.viewholder.bmsm

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopBmsmWidgetGwpUiModel

class ShopBmsmWidgetPlaceholderViewHolder(
    itemView: View
): AbstractViewHolder<ShopBmsmWidgetGwpUiModel>(itemView)   {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_bmsm_widget_placeholder
    }

    override fun bind(element: ShopBmsmWidgetGwpUiModel) { }
}
