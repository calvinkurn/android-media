package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RepurchaseProductGridAdapter
import com.tokopedia.tokopedianow.recentpurchase.presentation.adapter.RepurchaseProductGridAdapterTypeFactory
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseProductGridUiModel
import com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder.RepurchaseProductViewHolder.*

class RepurchaseProductGridViewHolder(
    itemView: View,
    productCardListener: RepurchaseProductCardListener
): AbstractViewHolder<RepurchaseProductGridUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_product_grid

        private const val GRID_SPAN_COUNT = 2
    }

    private var rvProductList: RecyclerView? = null

    private val adapter by lazy {
        RepurchaseProductGridAdapter(RepurchaseProductGridAdapterTypeFactory(productCardListener))
    }

    init {
        rvProductList = itemView.findViewById(R.id.rv_product_list)
    }

    override fun bind(data: RepurchaseProductGridUiModel) {
        rvProductList?.apply {
            layoutManager = StaggeredGridLayoutManager(
                GRID_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL
            )
            adapter = this@RepurchaseProductGridViewHolder.adapter
        }

        adapter.submitList(data.productList)
    }
}
