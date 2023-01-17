package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.ShopHomeDisplayWidgetUiModel

class ShopHomeMultipleImageColumnPlaceholderViewHolder(
    itemView: View
) : AbstractViewHolder<ShopHomeDisplayWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.widget_shop_home_multiple_image_column_placeholder
    }

    override fun bind(element: ShopHomeDisplayWidgetUiModel) {}
}
