package com.tokopedia.product_bundle.multiple.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleMasterViewHolder

class ProductBundleMasterAdapter(private val clickListener: ProductBundleMasterItemClickListener)
    : RecyclerView.Adapter<ProductBundleMasterViewHolder>() {

    interface ProductBundleMasterItemClickListener {
        fun onProductBundleMasterItemClicked(productBundleInfo: BundleInfo)
    }

    private var productBundleInfo: List<BundleInfo> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBundleMasterViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_bundle_master_item, parent, false)
        return ProductBundleMasterViewHolder(rootView, clickListener)
    }

    override fun getItemCount(): Int {
        return productBundleInfo.size
    }

    override fun onBindViewHolder(holderBundle: ProductBundleMasterViewHolder, position: Int) {
        val bundleInfo = productBundleInfo[position]
        holderBundle.bindData(bundleInfo)
    }

    fun setProductBundleInformation(productBundleInfo: List<BundleInfo>) {
        this.productBundleInfo = productBundleInfo
        notifyDataSetChanged()
    }
}