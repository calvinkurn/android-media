package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationNotFoundUiModel
import com.tokopedia.brandlist.common.ImageAssets
import com.tokopedia.brandlist.databinding.BrandlistSearchRecomNotFoundViewHolderBinding
import com.tokopedia.utils.view.binding.viewBinding


class BrandlistSearchRecommendationNotFoundViewHolder(
        itemView: View,
        private val listener: Listener
): AbstractViewHolder<BrandlistSearchRecommendationNotFoundUiModel>(itemView) {
    private var binding: BrandlistSearchRecomNotFoundViewHolderBinding? by viewBinding()

    override fun bind(element: BrandlistSearchRecommendationNotFoundUiModel?) {
        binding?.brandNotFoundLayout?.visibility = View.VISIBLE
        ImageHandler.loadImage(itemView.context, binding?.imgBrandNotFound,
                ImageAssets.BRAND_NOT_FOUND, null)
        binding?.btnSearchBrands?.setOnClickListener {
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
