package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationNotFoundUiModel
import com.tokopedia.brandlist.common.ImageAssets
import kotlinx.android.synthetic.main.brandlist_search_recom_not_found_view_holder.view.*
import kotlinx.android.synthetic.main.brandlist_search_recom_not_found_view_holder.view.brand_not_found_layout
import kotlinx.android.synthetic.main.brandlist_search_recom_not_found_view_holder.view.img_brand_not_found


class BrandlistSearchRecommendationNotFoundViewHolder(
        itemView: View,
        private val listener: Listener
): AbstractViewHolder<BrandlistSearchRecommendationNotFoundUiModel>(itemView) {

    override fun bind(element: BrandlistSearchRecommendationNotFoundUiModel?) {
        itemView.brand_not_found_layout.visibility = View.VISIBLE
        ImageHandler.loadImage(itemView.context, itemView.img_brand_not_found,
                ImageAssets.BRAND_NOT_FOUND, null)
        itemView.btn_search_brands.setOnClickListener {
            listener.onClickSearchButton()
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_search_recom_not_found_view_holder
    }

    interface Listener {
        fun onClickSearchButton()
    }
}
