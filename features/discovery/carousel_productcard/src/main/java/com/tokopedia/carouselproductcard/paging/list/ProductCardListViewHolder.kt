package com.tokopedia.carouselproductcard.paging.list

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.databinding.CarouselPagingItemLayoutBinding
import com.tokopedia.carouselproductcard.helper.CarouselPagingUtil
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.utils.view.binding.viewBinding

internal class ProductCardListViewHolder(
    itemView: View,
    util: CarouselPagingUtil,
): AbstractViewHolder<ProductCardListDataView>(itemView) {

    private var binding: CarouselPagingItemLayoutBinding? by viewBinding()

    init {
        val layoutParams = binding?.carouselPagingProductCardListView?.layoutParams

        if (layoutParams != null) {
            layoutParams.width = util.getItemWidth()
            binding?.carouselPagingProductCardListView?.layoutParams = layoutParams
        }
    }

    override fun bind(element: ProductCardListDataView) {
        binding?.carouselPagingProductCardListView?.setProductModel(element.productCardModel)
        binding?.carouselPagingProductCardListView?.setImageProductViewHintListener(
            element,
            object: ViewHintListener {
                override fun onViewHint() {
                    element.listener.onItemImpress(element.group, element.productIndex)
                }
            }
        )
        binding?.carouselPagingProductCardListView?.setOnClickListener(object: ProductCardClickListener {
            override fun onClick(v: View) {
                element.listener.onItemClick(element.group, element.productIndex)
            }
        })
    }

    override fun onViewRecycled() {
        binding?.carouselPagingProductCardListView?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_paging_item_layout
    }
}
