package com.tokopedia.productbundlewidget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productbundlewidget.adapter.viewholder.ProductBundleMultiplePackageViewHolder
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleTypes
import com.tokopedia.productbundlewidget.model.BundleUiModel

class ProductBundleMultipleAdapter(
    private val listener: ProductBundleAdapterListener?
) : RecyclerView.Adapter<ProductBundleMultiplePackageViewHolder>() {

    private var bundleProducts: List<BundleProductUiModel> = listOf()
    private var bundleDetail: BundleDetailUiModel = BundleDetailUiModel()
    private var bundle: BundleUiModel = BundleUiModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBundleMultiplePackageViewHolder {
        return ProductBundleMultiplePackageViewHolder(
                LayoutInflater.from(parent.context).inflate(
                        ProductBundleMultiplePackageViewHolder.LAYOUT,
                        parent,
                        false
                )
        )
    }

    override fun onBindViewHolder(holder: ProductBundleMultiplePackageViewHolder, position: Int) {
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
