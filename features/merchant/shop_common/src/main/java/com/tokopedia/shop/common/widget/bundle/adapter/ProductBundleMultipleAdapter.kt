package com.tokopedia.shop.common.widget.bundle.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.common.widget.bundle.enum.BundleTypes
import com.tokopedia.shop.common.widget.bundle.listener.ProductBundleListener
import com.tokopedia.shop.common.widget.bundle.model.BundleDetailUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleProductUiModel
import com.tokopedia.shop.common.widget.bundle.model.BundleUiModel
import com.tokopedia.shop.common.widget.bundle.viewholder.ShopHomeProductBundleMultiplePackageViewHolder

class ProductBundleMultipleAdapter(
    private val listener: ProductBundleListener?
) : RecyclerView.Adapter<ShopHomeProductBundleMultiplePackageViewHolder>() {

    private var bundleProducts: List<BundleProductUiModel> = listOf()
    private var bundleDetail: BundleDetailUiModel = BundleDetailUiModel()
    private var bundle: BundleUiModel = BundleUiModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopHomeProductBundleMultiplePackageViewHolder {
        return ShopHomeProductBundleMultiplePackageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        ShopHomeProductBundleMultiplePackageViewHolder.LAYOUT,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ShopHomeProductBundleMultiplePackageViewHolder, position: Int) {
        val bundleProduct = bundleProducts.getOrNull(position) ?: BundleProductUiModel()
        holder.bind(bundleProduct, ::onViewImpression, ::onClickImpression)
    }

    private fun onClickImpression(position: Int) {
        bundleProducts.getOrNull(position)?.let { product ->
            listener?.onBundleProductClicked(
                BundleTypes.MULTIPLE_BUNDLE,
                bundle,
                bundleDetail,
                product,
                position
            )
        }
    }

    private fun onViewImpression(position: Int) {
        bundleProducts.getOrNull(position)?.let { product ->
            listener?.impressionProductItemBundleMultiple(
                product,
                bundleDetail,
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return bundleProducts.size
    }

    fun updateDataSet(
        bundleProducts: List<BundleProductUiModel>,
        bundleDetail: BundleDetailUiModel,
        bundle: BundleUiModel
    ) {
        this.bundleProducts = bundleProducts
        this.bundleDetail = bundleDetail
        this.bundle = bundle
        notifyDataSetChanged()
    }
}