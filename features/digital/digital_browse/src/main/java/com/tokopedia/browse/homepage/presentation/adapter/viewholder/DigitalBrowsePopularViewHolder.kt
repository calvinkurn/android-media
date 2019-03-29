package com.tokopedia.browse.homepage.presentation.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.browse.R
import com.tokopedia.browse.homepage.presentation.model.DigitalBrowsePopularBrandsViewModel

/**
 * @author by furqan on 05/09/18.
 */

class DigitalBrowsePopularViewHolder(itemView: View, private val popularBrandListener: PopularBrandListener) :
        AbstractViewHolder<DigitalBrowsePopularBrandsViewModel>(itemView) {

    private val ivPopularBrand: AppCompatImageView = itemView.findViewById(R.id.iv_popular_brand)

    override fun bind(element: DigitalBrowsePopularBrandsViewModel) {
        ImageHandler.loadImageWithoutPlaceholder(ivPopularBrand, element.logoUrl, R.drawable.status_no_result)

        itemView.setOnClickListener { popularBrandListener.onPopularItemClicked(element, adapterPosition) }
    }

    interface PopularBrandListener {
        fun onPopularItemClicked(viewModel: DigitalBrowsePopularBrandsViewModel, position: Int)
    }

    companion object {
        @LayoutRes
        var LAYOUT = R.layout.item_digital_browse_image
    }
}
