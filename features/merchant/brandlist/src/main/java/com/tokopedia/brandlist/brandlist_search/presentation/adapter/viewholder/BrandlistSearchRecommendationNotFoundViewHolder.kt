package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationNotFoundUiModel
import com.tokopedia.brandlist.common.ImageAssets
import com.tokopedia.brandlist.databinding.BrandlistSearchRecomNotFoundViewHolderBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding


class BrandlistSearchRecommendationNotFoundViewHolder(
        itemView: View,
        private val listener: Listener
): AbstractViewHolder<BrandlistSearchRecommendationNotFoundUiModel>(itemView) {
    private var binding: BrandlistSearchRecomNotFoundViewHolderBinding? by viewBinding()

    override fun bind(element: BrandlistSearchRecommendationNotFoundUiModel?) {
        binding?.brandNotFoundLayout?.visibility = View.VISIBLE
        binding?.imgBrandNotFound?.loadImage(ImageAssets.BRAND_NOT_FOUND)
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
