package com.tokopedia.product_bundle.multiple.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleDetailViewHolder

class ProductBundleDetailAdapter(private val clickListener: ProductBundleDetailVariantClickListener)
    : RecyclerView.Adapter<ProductBundleDetailViewHolder>() {

    interface ProductBundleDetailVariantClickListener {
        fun onProductBundleMasterItemClicked(productBundleItem: BundleItem)
    }

    private var productBundleItems: List<BundleItem> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBundleDetailViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_bundle_detail_item, parent, false)
        return ProductBundleDetailViewHolder(rootView, clickListener)
    }

    override fun getItemCount(): Int {
        return productBundleItems.size
    }

    override fun onBindViewHolder(holder: ProductBundleDetailViewHolder, position: Int) {
        val bundleItem = productBundleItems[position]
        holder.bindData(bundleItem)
    }

    fun setProductBundleItems(productBundleItems: List<BundleItem>) {
        this.productBundleItems = productBundleItems
    }
}