package com.tokopedia.minicart.cartlist.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartProductBundleBinding
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleListener

class MiniCartProductBundleViewHolder (
    private val viewBinding: ItemMiniCartProductBundleBinding,
    private val multipleProductBundleListener: MultipleProductBundleListener? = null,
    private val singleProductBundleListener: SingleProductBundleListener? = null
) : AbstractViewHolder<MiniCartProductBundleRecomUiModel>(viewBinding.root) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mini_cart_product_bundle

        private const val BUNDLE_MULTIPLE_ITEM_SIZE = 2
        private const val BUNDLE_SINGLE_ITEM_SIZE = 1
    }

    private var rvBundleAdapter : ShopHomeProductBundleWidgetAdapter? = null
    private var bundleListSize = 0

    override fun bind(element: MiniCartProductBundleRecomUiModel) {
        viewBinding.tvBundleWidgetTitle.text = element.title
        bundleListSize = element.productBundleList.size
        val bundleLayoutManager = if (bundleListSize >= BUNDLE_MULTIPLE_ITEM_SIZE) {
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(itemView.context, BUNDLE_SINGLE_ITEM_SIZE, GridLayoutManager.VERTICAL, false)
        }
        initRecyclerView(bundleLayoutManager, element)
        rvBundleAdapter?.updateDataSet(element.productBundleList)
        rvBundleAdapter?.setParentPosition(adapterPosition)
    }

    private fun initRecyclerView(bundleLayoutManager: RecyclerView.LayoutManager, bundleLayout: MiniCartProductBundleRecomUiModel) {
        if (multipleProductBundleListener != null && singleProductBundleListener != null) {
            rvBundleAdapter = ShopHomeProductBundleWidgetAdapter(
                multipleProductBundleListener,
                singleProductBundleListener,
                bundleListSize,
                bundleLayout.widgetId,
                bundleLayout.widgetMasterId,
                bundleLayout.type,
                bundleLayout.name
            )
            viewBinding.rvProductBundleList.apply {
                setHasFixedSize(true)
                layoutManager = bundleLayoutManager
                adapter = rvBundleAdapter
            }
        }
    }
}