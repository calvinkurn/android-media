package com.tokopedia.shop_showcase.shop_showcase_add.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.ImageAssets
import com.tokopedia.shop_showcase.shop_showcase_add.presentation.model.AddShowcaseHeader
import kotlinx.android.synthetic.main.item_add_showcase_header.view.*

class AddShowcaseHeaderViewHolder(itemView: View) : AbstractViewHolder<AddShowcaseHeader>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_add_showcase_header
    }

    override fun bind(element: AddShowcaseHeader?) {
//        itemView.tv_add_showcase_title.text = itemView.resources.getString(R.string.add_showcase_title_header)
//        itemView.empty_state_product_showcase.setImageUrl(ImageAssets.PRODUCT_EMPTY)
    }
}