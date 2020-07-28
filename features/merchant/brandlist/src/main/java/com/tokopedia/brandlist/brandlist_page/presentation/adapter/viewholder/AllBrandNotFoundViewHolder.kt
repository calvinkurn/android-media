package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllbrandNotFoundUiModel
import com.tokopedia.brandlist.common.ImageAssets
import kotlinx.android.synthetic.main.brandlist_all_brand_not_found.view.*
import kotlinx.android.synthetic.main.brandlist_all_brand_not_found.view.brand_not_found_layout
import kotlinx.android.synthetic.main.brandlist_all_brand_not_found.view.img_brand_not_found

class AllBrandNotFoundViewHolder(
        itemView: View,
        private val listener: Listener
): AbstractViewHolder<AllbrandNotFoundUiModel>(itemView) {

    override fun bind(element: AllbrandNotFoundUiModel?) {
        itemView.brand_not_found_layout.visibility = View.VISIBLE
        ImageHandler.loadImage(itemView.context, itemView.img_brand_not_found,
                ImageAssets.BRAND_NOT_FOUND, null)
        itemView.btn_search_brand.setOnClickListener {
            listener.onClickSearchButton()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_not_found
    }

    interface Listener {
        fun onClickSearchButton()
    }
}