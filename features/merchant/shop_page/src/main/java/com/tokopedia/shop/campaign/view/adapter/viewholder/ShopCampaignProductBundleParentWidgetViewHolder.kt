package com.tokopedia.shop.campaign.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.shop.R
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener
import com.tokopedia.shop.databinding.ItemShopCampaignProductBundleParentWidgetBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * author by Rafli Syam on 05/01/2022
 */
class ShopCampaignProductBundleParentWidgetViewHolder(
    itemView: View,
    private val multipleProductBundleListener: MultipleProductBundleListener,
    private val singleProductBundleListener: SingleProductBundleListener,
    private val widgetConfigListener: WidgetConfigListener,
    private val parentBundlingWidgetListener: Listener
) : AbstractViewHolder<ShopHomeProductBundleListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_campaign_product_bundle_parent_widget

        private const val BUNDLE_MULTIPLE_ITEM_SIZE = 2
        private const val BUNDLE_SINGLE_ITEM_SIZE = 1
    }

    interface Listener {
        fun onImpressionBundlingWidget(model: ShopHomeProductBundleListUiModel, position: Int)
    }

    private val viewBinding: ItemShopCampaignProductBundleParentWidgetBinding? by viewBinding()
    private var tvWidgetTitle: TextView? = null
    private var rvBundleList: RecyclerView? = null
    private var rvBundleAdapter: ShopHomeProductBundleWidgetAdapter? = null
    private var bundleListSize = 0

    init {
        initView()
    }

    override fun bind(element: ShopHomeProductBundleListUiModel) {
        tvWidgetTitle?.apply {
            text = element.header.title
            setTextColor(widgetConfigListener.getWidgetTextColor())
        }
        bundleListSize = element.productBundleList.size
        val bundleLayoutManager = if (bundleListSize >= BUNDLE_MULTIPLE_ITEM_SIZE) {
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(itemView.context, BUNDLE_SINGLE_ITEM_SIZE, GridLayoutManager.VERTICAL, false)
        }
        setWidgetImpressionListener(element)
        initRecyclerView(bundleLayoutManager, element)
        rvBundleAdapter?.setWidgetTitle(element.header.title)
        rvBundleAdapter?.setWidgetName(element.name)
        rvBundleAdapter?.updateDataSet(element.productBundleList)
        rvBundleAdapter?.setParentPosition(adapterPosition)
    }

    private fun setWidgetImpressionListener(element: ShopHomeProductBundleListUiModel) {
        itemView.addOnImpressionListener(element.impressHolder) {
            parentBundlingWidgetListener.onImpressionBundlingWidget(element, adapterPosition)
        }
    }

    private fun initView() {
        tvWidgetTitle = viewBinding?.tvBundleWidgetTitle
        rvBundleList = viewBinding?.rvProductBundleList
    }

    private fun initRecyclerView(bundleLayoutManager: RecyclerView.LayoutManager, bundleLayout: ShopHomeProductBundleListUiModel) {
        rvBundleAdapter = ShopHomeProductBundleWidgetAdapter(
            multipleProductBundleListener,
            singleProductBundleListener,
            bundleListSize,
            bundleLayout.widgetId,
            bundleLayout.widgetMasterId,
            bundleLayout.type,
            bundleLayout.name
        )
        rvBundleList?.apply {
            setHasFixedSize(true)
            layoutManager = bundleLayoutManager
            adapter = rvBundleAdapter
        }
    }
}
