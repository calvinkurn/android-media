package com.tokopedia.shop.common.widget.bundle.adapter

import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter.Companion.PRODUCT_BUNDLE_SINGLE
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.ProductBundleMultipleViewHolder
import com.tokopedia.shop.common.widget.bundle.viewholder.ProductBundleSingleViewHolder

class ProductBundleWidgetAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bundleListItem: List<BundleUiModel> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val displayMetrics = parent.resources.displayMetrics
        return if (viewType == ProductBundleSingleViewHolder.LAYOUT) {
            ProductBundleSingleViewHolder(
                View.inflate(parent.context, viewType, null),
                createContainerWidgetParams(displayMetrics)
            )
        } else {
            ProductBundleMultipleViewHolder(
                View.inflate(parent.context, viewType, null),
                createContainerWidgetParams(displayMetrics)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bundleItem = bundleListItem.getOrNull(position) ?: BundleUiModel()
        when (holder) {
            is ProductBundleSingleViewHolder -> holder.bind(bundleItem)
            is ProductBundleMultipleViewHolder -> holder.bind(bundleItem, bundleListItem.size)
        }
    }

    override fun getItemCount(): Int {
        return bundleListItem.size
    }

    override fun getItemViewType(position: Int): Int {
        val bundleItem = bundleListItem.getOrNull(position) ?: BundleUiModel()
        return if (bundleItem.bundleType == PRODUCT_BUNDLE_SINGLE) {
            ProductBundleSingleViewHolder.LAYOUT
        } else {
            ProductBundleMultipleViewHolder.LAYOUT
        }
    }

    private fun createContainerWidgetParams(displayMetrics: DisplayMetrics) =
        if (bundleListItem.size == ShopHomeProductBundleWidgetAdapter.SINGLE_SIZE_WIDGET) {
            ConstraintLayout.LayoutParams.MATCH_PARENT
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                ShopHomeProductBundleWidgetAdapter.BUNDLE_WIDGET_DEFAULT_WIDTH,
                displayMetrics
            ).toInt()
        }

    fun updateDataSet(newList: List<BundleUiModel>) {
        bundleListItem = newList
        notifyDataSetChanged()
    }

}