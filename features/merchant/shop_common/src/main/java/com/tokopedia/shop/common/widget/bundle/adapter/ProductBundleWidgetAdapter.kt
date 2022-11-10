package com.tokopedia.shop.common.widget.bundle.adapter

import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.ProductBundleMultipleViewHolder
import com.tokopedia.shop.common.widget.bundle.viewholder.ProductBundleSingleViewHolder

class ProductBundleWidgetAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bundleListItem: List<BundleUiModel> = listOf()
    private var listener: ProductBundleListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val displayMetrics = parent.resources.displayMetrics
        val containerWidgetParam = createContainerWidgetParams(displayMetrics)
        val itemView = createItemView(parent, viewType, containerWidgetParam)
        return if (viewType == ProductBundleSingleViewHolder.LAYOUT) {
            ProductBundleSingleViewHolder(
                itemView,
                containerWidgetParam
            ).apply {
                setListener(listener)
            }
        } else {
            ProductBundleMultipleViewHolder(
                itemView,
                containerWidgetParam
            ).apply {
                setListener(listener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bundleItem = bundleListItem.getOrNull(position) ?: BundleUiModel()
        when (holder) {
            is ProductBundleSingleViewHolder -> holder.bind(bundleItem)
            is ProductBundleMultipleViewHolder -> holder.bind(bundleItem)
        }
    }

    override fun getItemCount(): Int {
        return bundleListItem.size
    }

    override fun getItemViewType(position: Int): Int {
        val bundleItem = bundleListItem.getOrNull(position) ?: BundleUiModel()
        return if (bundleItem.bundleType == BundleTypes.SINGLE_BUNDLE) {
            ProductBundleSingleViewHolder.LAYOUT
        } else {
            ProductBundleMultipleViewHolder.LAYOUT
        }
    }

    private fun createItemView(parent: ViewGroup, viewType: Int, containerWidgetParam: Int): View {
        val root = if (containerWidgetParam == ConstraintLayout.LayoutParams.MATCH_PARENT) parent else null
        return LayoutInflater.from(parent.context).inflate(viewType, root, false)
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

    fun setListener(listener: ProductBundleListener) {
        this.listener = listener
    }

}
