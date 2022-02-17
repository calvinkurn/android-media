package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeProductBundleItemUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleMultipleViewHolder
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleSingleViewHolder
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleClickListener
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout

class ShopHomeProductBundleWidgetAdapter(
        private val multipleProductBundleListener: MultipleProductBundleListener,
        private val singleProductBundleClickListener: SingleProductBundleClickListener,
        private val bundleListSize: Int,
        private val widgetId: String,
        private val widgetMasterId: String,
        private val widgetType: String,
        private val widgetName: String,
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val PRODUCT_BUNDLE_SINGLE = "single_bundling"
        const val SINGLE_SIZE_WIDGET = 1
    }

    private var bundleListItem: List<ShopHomeProductBundleItemUiModel> = listOf()
    private var parentPosition: Int = 0
    private var widgetLayout = ShopHomeWidgetLayout(
            widgetId,
            widgetMasterId,
            widgetType,
            widgetName
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ShopHomeProductBundleSingleViewHolder.LAYOUT) {
            ShopHomeProductBundleSingleViewHolder(
                    View.inflate(parent.context, viewType, null),
                    singleProductBundleClickListener,
                    bundleListSize,
                    widgetLayout
            )
        } else {
            ShopHomeProductBundleMultipleViewHolder(
                    View.inflate(parent.context, viewType, null),
                    multipleProductBundleListener,
                    bundleListSize,
                    widgetLayout
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShopHomeProductBundleSingleViewHolder -> holder.bind(bundleListItem[position])
            is ShopHomeProductBundleMultipleViewHolder -> holder.bind(bundleListItem[position])
        }
    }

    override fun getItemCount(): Int {
        return bundleListItem.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (bundleListItem[position].bundleType == PRODUCT_BUNDLE_SINGLE) {
            ShopHomeProductBundleSingleViewHolder.LAYOUT
        } else {
            ShopHomeProductBundleMultipleViewHolder.LAYOUT
        }
    }

    fun updateDataSet(newList: List<ShopHomeProductBundleItemUiModel>) {
        bundleListItem = newList
        notifyDataSetChanged()
    }

    fun setParentPosition(adapterPosition: Int) {
        this.parentPosition = adapterPosition
    }
}