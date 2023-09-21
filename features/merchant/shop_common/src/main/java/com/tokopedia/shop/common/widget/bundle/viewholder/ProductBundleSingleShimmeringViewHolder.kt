package com.tokopedia.shop.common.widget.bundle.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.databinding.ItemProductBundleSingleWidgetShimmeringBinding
import com.tokopedia.utils.view.binding.viewBinding

class ProductBundleSingleShimmeringViewHolder(
    itemView: View,
    containerWidgetParams: Int
): RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_product_bundle_single_widget_shimmering
    }

    private var viewBinding: ItemProductBundleSingleWidgetShimmeringBinding? by viewBinding()

    init {
        viewBinding?.layoutShimmerContainer?.layoutParams?.width = containerWidgetParams
    }
}
