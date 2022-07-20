package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllbrandNotFoundUiModel
import com.tokopedia.brandlist.common.ImageAssets
import com.tokopedia.brandlist.databinding.BrandlistAllBrandNotFoundBinding
import com.tokopedia.utils.view.binding.viewBinding

class AllBrandNotFoundViewHolder(
        itemView: View,
        private val listener: Listener
): AbstractViewHolder<AllbrandNotFoundUiModel>(itemView) {

    private var binding: BrandlistAllBrandNotFoundBinding? by viewBinding()

    override fun bind(element: AllbrandNotFoundUiModel?) {
        binding?.brandNotFoundLayout?.visibility = View.VISIBLE
        ImageHandler.loadImage(itemView.context, binding?.imgBrandNotFound,
                ImageAssets.BRAND_NOT_FOUND, null)
        binding?.btnSearchBrand?.setOnClickListener {
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