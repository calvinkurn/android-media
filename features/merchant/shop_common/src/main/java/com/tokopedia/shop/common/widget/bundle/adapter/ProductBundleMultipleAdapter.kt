package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.model.*
import com.tokopedia.shop.common.widget.bundle.viewholder.MultipleProductBundleListener
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleMultiplePackageViewHolder
import com.tokopedia.shop.common.widget.model.ShopHomeWidgetLayout

class ProductBundleMultipleAdapter: RecyclerView.Adapter<ShopHomeProductBundleMultiplePackageViewHolder>(),
    MultipleProductBundleListener {

    private var bundleProducts: List<BundleProductUiModel> = listOf()
    private var multipleBundleDetail: BundleDetailUiModel = BundleDetailUiModel()
    private var multipleBundleParent: BundleUiModel = BundleUiModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeProductBundleMultiplePackageViewHolder {
        return ShopHomeProductBundleMultiplePackageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        ShopHomeProductBundleMultiplePackageViewHolder.LAYOUT,
                        parent,
                        false
                ),
                this
        )
    }

    override fun onBindViewHolder(holder: ShopHomeProductBundleMultiplePackageViewHolder, position: Int) {
        val bundleProduct = bundleProducts.getOrNull(position) ?: BundleProductUiModel()
        holder.bind(
                bundleProduct,
                multipleBundleDetail,
                multipleBundleParent,
                position,
                "",
                ""
        )
    }

    override fun getItemCount(): Int {
        return bundleProducts.size
    }

    fun updateDataSet(
        newList: List<BundleProductUiModel>,
        bundleDetail: BundleDetailUiModel,
        bundleParent: BundleUiModel
    ) {
        bundleProducts = newList
        multipleBundleDetail = bundleDetail
        multipleBundleParent = bundleParent
        notifyDataSetChanged()
    }

    override fun onMultipleBundleProductClicked(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int,
    ) {
        //
    }

    override fun addMultipleBundleToCart(
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleListSize: Int,
        productDetails: List<ShopHomeBundleProductUiModel>,
        bundleName: String,
        widgetLayout: ShopHomeWidgetLayout,
    ) {
        //
    }

    override fun impressionProductBundleMultiple(
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
    ) {
       //
    }

    override fun impressionProductItemBundleMultiple(
        selectedProduct: ShopHomeBundleProductUiModel,
        selectedMultipleBundle: ShopHomeProductBundleDetailUiModel,
        bundleName: String,
        bundlePosition: Int,
        widgetTitle: String,
        widgetName: String,
        productItemPosition: Int,
    ) {
        //
    }

}