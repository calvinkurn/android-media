package com.tokopedia.carouselproductcard.paging.list

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.databinding.CarouselPagingItemLayoutBinding
import com.tokopedia.carouselproductcard.paging.CarouselPagingProductCardView
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.utils.view.binding.viewBinding

internal class ProductCardListViewHolder(
    itemView: View,
    private val listener: CarouselPagingProductCardView.CarouselPagingListener,
): AbstractViewHolder<ProductCardListDataView>(itemView) {

    private var binding: CarouselPagingItemLayoutBinding? by viewBinding()

    override fun bind(element: ProductCardListDataView) {
        binding?.carouselPagingProductCardListView?.setProductModel(element.productCardModel)
        binding?.carouselPagingProductCardListView?.setImageProductViewHintListener(
            element,
            object: ViewHintListener {
                override fun onViewHint() {
                    listener.onItemImpress(element.group, element.productIndex)
                }
            }
        )
        binding?.carouselPagingProductCardListView?.setOnClickListener {
            listener.onItemClick(element.group, element.productIndex)
        }
    }

    override fun onViewRecycled() {
        binding?.carouselPagingProductCardListView?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_paging_item_layout
    }
}
