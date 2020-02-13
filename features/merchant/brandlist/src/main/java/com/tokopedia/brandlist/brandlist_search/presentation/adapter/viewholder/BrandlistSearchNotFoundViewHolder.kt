package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchNotFoundViewModel
import com.tokopedia.brandlist.common.ImageAssets
import kotlinx.android.synthetic.main.view_brand_not_found.view.*

class BrandlistSearchNotFoundViewHolder(view: View): AbstractViewHolder<BrandlistSearchNotFoundViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.view_brand_not_found
    }

    override fun bind(element: BrandlistSearchNotFoundViewModel?) {
        itemView.brand_not_found_layout.visibility = View.VISIBLE
        ImageHandler.loadImage(itemView.context, itemView.img_brand_not_found,
                ImageAssets.BRAND_NOT_FOUND, null)
        itemView.btn_check_balance.setOnClickListener {
            // Set focus on search bar
        }
    }
}
