package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.common.widget.bundle.adapter.ShopHomeProductBundleWidgetAdapter
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleClickListener
import com.tokopedia.shop.common.widget.bundle.viewholder.SingleProductBundleClickListener
import com.tokopedia.shop.databinding.ItemShopHomeProductBundleParentWidgetBinding
import com.tokopedia.shop.home.view.model.ShopHomeProductBundleListUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * author by Rafli Syam on 05/01/2022
 */
class ShopHomeProductBundleParentWidgetViewHolder (
        itemView: View,
        private val multipleProductBundleClickListener: MultipleProductBundleClickListener,
        private val singleProductBundleClickListener: SingleProductBundleClickListener
) : AbstractViewHolder<ShopHomeProductBundleListUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_bundle_parent_widget

        private const val BUNDLE_MULTIPLE_ITEM_SIZE = 2
        private const val BUNDLE_SINGLE_ITEM_SIZE = 1
    }

    private val viewBinding: ItemShopHomeProductBundleParentWidgetBinding? by viewBinding()
    private var tvWidgetTitle : TextView? = null
    private var rvBundleList : RecyclerView? = null
    private var rvBundleAdapter : ShopHomeProductBundleWidgetAdapter? = null

    init {
        initView()
    }

    override fun bind(element: ShopHomeProductBundleListUiModel) {
        tvWidgetTitle?.text = element.header.title
        val bundleLayoutManager = if (element.productBundleList.size >= BUNDLE_MULTIPLE_ITEM_SIZE) {
            LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
        } else {
            GridLayoutManager(itemView.context, BUNDLE_SINGLE_ITEM_SIZE, GridLayoutManager.VERTICAL, false)
        }
        initRecyclerView(bundleLayoutManager)
        rvBundleAdapter?.updateDataSet(element.productBundleList)
        rvBundleAdapter?.setParentPosition(adapterPosition)
    }

    private fun initView() {
        tvWidgetTitle = viewBinding?.tvBundleWidgetTitle
        rvBundleList = viewBinding?.rvProductBundleList
    }

    private fun initRecyclerView(bundleLayoutManager: RecyclerView.LayoutManager) {
        rvBundleAdapter = ShopHomeProductBundleWidgetAdapter(
                multipleProductBundleClickListener,
                singleProductBundleClickListener
        )
        rvBundleList?.apply {
            setHasFixedSize(true)
            layoutManager = bundleLayoutManager
            adapter = rvBundleAdapter
        }
    }
}