package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter.Companion.PRODUCT_BUNDLE_SINGLE
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.ProductBundleMultipleViewHolder
import com.tokopedia.shop.common.widget.bundle.viewholder.ProductBundleSingleViewHolder

class ProductBundleWidgetAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var bundleListItem: List<ShopHomeProductBundleItemUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ProductBundleSingleViewHolder.LAYOUT) {
            ProductBundleSingleViewHolder(
                    View.inflate(parent.context, viewType, null)
            )
        } else {
            ProductBundleMultipleViewHolder(
                    View.inflate(parent.context, viewType, null)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bundleItem = bundleListItem.getOrNull(position) ?: ShopHomeProductBundleItemUiModel()
        when (holder) {
            is ProductBundleSingleViewHolder -> holder.bind(bundleItem)
            is ProductBundleMultipleViewHolder -> holder.bind(bundleItem)
        }
    }

    override fun getItemCount(): Int {
        return bundleListItem.size
    }

    override fun getItemViewType(position: Int): Int {
        val bundleItem = bundleListItem.getOrNull(position) ?: ShopHomeProductBundleItemUiModel()
        return if (bundleItem.bundleType == PRODUCT_BUNDLE_SINGLE) {
            ProductBundleSingleViewHolder.LAYOUT
        } else {
            ProductBundleMultipleViewHolder.LAYOUT
        }
    }

    fun updateDataSet(newList: List<ShopHomeProductBundleItemUiModel>) {
        bundleListItem = newList
        notifyDataSetChanged()
    }

}