package com.tokopedia.home_component.viewholders.shorten.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.GlobalComponent2squareProductWidgetBinding
import com.tokopedia.home_component.viewholders.shorten.ContainerMultiTwoSquareListener
import com.tokopedia.home_component.viewholders.shorten.internal.TWO_SQUARE_LIMIT
import com.tokopedia.home_component.viewholders.shorten.viewholder.item.PartialItemWidgetAdapter
import com.tokopedia.home_component.visitable.shorten.MultiTwoSquareWidgetUiModel.Type as ItemTwoSquareType
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ProductWidgetViewHolder(
    view: View,
    pool: RecyclerView.RecycledViewPool?,
    private val listener: ContainerMultiTwoSquareListener
) : AbstractViewHolder<ProductWidgetUiModel>(view) {

    private val binding: GlobalComponent2squareProductWidgetBinding? by viewBinding()
    private var mAdapter: PartialItemWidgetAdapter? = null

    init {
        if (pool != null) {
            binding?.lstProductCard?.setRecycledViewPool(pool)
        }

        setupRecyclerView()
    }

    override fun bind(element: ProductWidgetUiModel?) {
        if (element == null) return

        binding?.txtHeader?.text = element.header.name
        mAdapter?.submitList(element.data)
    }

    private fun setupRecyclerView() {
        mAdapter = PartialItemWidgetAdapter(ItemTwoSquareType.Product, listener)
        binding?.lstProductCard?.layoutManager = GridLayoutManager(itemView.context, TWO_SQUARE_LIMIT)
        binding?.lstProductCard?.adapter = mAdapter
        binding?.lstProductCard?.setHasFixedSize(true)
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.global_component_2square_product_widget
    }
}
