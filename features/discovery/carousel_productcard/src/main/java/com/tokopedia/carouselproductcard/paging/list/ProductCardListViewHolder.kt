package com.tokopedia.carouselproductcard.paging.list

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.R
import com.tokopedia.carouselproductcard.databinding.CarouselPagingItemLayoutBinding
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.view.binding.viewBinding

internal class ProductCardListViewHolder(
    itemView: View,
    private val paddingStart: Int,
): AbstractViewHolder<ProductCardListDataView>(itemView) {

    private var binding: CarouselPagingItemLayoutBinding? by viewBinding()

    init {
        val layoutParams = binding?.carouselPagingProductCardListView?.layoutParams

        if (layoutParams != null) {
            layoutParams.width = itemWidth()
            binding?.carouselPagingProductCardListView?.layoutParams = layoutParams
        }
    }

    private fun itemWidth(): Int =
        try {
            val screenWidth = DeviceScreenInfo.getScreenWidth(itemView.context)

            ((screenWidth * SCREEN_SIZE_PERCENTAGE) - paddingStart).toInt()
        } catch (_: Throwable) {
            DEFAULT_ITEM_WIDTH_DP.toPx()
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
        binding?.carouselPagingProductCardListView?.setOnClickListener {
            element.listener.onItemClick(element.group, element.productIndex)
        }
    }

    override fun onViewRecycled() {
        binding?.carouselPagingProductCardListView?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.carousel_paging_item_layout

        private const val DEFAULT_ITEM_WIDTH_DP = 300
        private const val SCREEN_SIZE_PERCENTAGE = 0.8
    }
}
