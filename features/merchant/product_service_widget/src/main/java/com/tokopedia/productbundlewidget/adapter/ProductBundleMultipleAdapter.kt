package com.tokopedia.productbundlewidget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.productbundlewidget.adapter.viewholder.ProductBundleMultiplePackageGroupViewHolder
import com.tokopedia.productbundlewidget.adapter.viewholder.ProductBundleMultiplePackageViewHolder
import com.tokopedia.productbundlewidget.listener.ProductBundleAdapterListener
import com.tokopedia.productbundlewidget.model.BundleDetailUiModel
import com.tokopedia.productbundlewidget.model.BundleProductUiModel
import com.tokopedia.productbundlewidget.model.BundleTypes
import com.tokopedia.productbundlewidget.model.BundleUiModel

class ProductBundleMultipleAdapter(
    private val listener: ProductBundleAdapterListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var bundleProducts: List<BundleProductUiModel> = listOf()
    private var bundleProductGrouped: List<BundleProductUiModel> = listOf()
    private var bundleDetail: BundleDetailUiModel = BundleDetailUiModel()
    private var bundle: BundleUiModel = BundleUiModel()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            viewType,
            parent,
            false
        )
        return if (viewType == ProductBundleMultiplePackageViewHolder.LAYOUT) {
            ProductBundleMultiplePackageViewHolder(itemView)
        } else {
            ProductBundleMultiplePackageGroupViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val bundleProduct = bundleProducts.getOrNull(position) ?: BundleProductUiModel()
        when (holder) {
            is ProductBundleMultiplePackageViewHolder ->
                holder.bind(bundleProduct, ::onViewImpression, ::onClickImpression)
            is ProductBundleMultiplePackageGroupViewHolder ->
                holder.bind(bundleProductGrouped, ::onMoreProductClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position >= bundleProducts.size && bundleProductGrouped.isNotEmpty()) {
            ProductBundleMultiplePackageGroupViewHolder.LAYOUT
        } else {
            ProductBundleMultiplePackageViewHolder.LAYOUT
        }
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

    private fun onMoreProductClick() {
        listener?.onMultipleBundleMoreProductClicked(
            bundleDetail,
            bundleProductGrouped,
            bundleProducts
        )
    }

    override fun getItemCount(): Int {
        val count = bundleProducts.size
        return if (bundleProductGrouped.isNotEmpty()) { count.inc() } else { count }
    }

    fun updateDataSet(
        bundleProducts: List<BundleProductUiModel>,
        bundleProductGrouped: List<BundleProductUiModel>,
        bundleDetail: BundleDetailUiModel,
        bundle: BundleUiModel
    ) {
        this.bundleProducts = bundleProducts
        this.bundleProductGrouped = bundleProductGrouped
        this.bundleDetail = bundleDetail
        this.bundle = bundle
        notifyDataSetChanged()
    }
}
