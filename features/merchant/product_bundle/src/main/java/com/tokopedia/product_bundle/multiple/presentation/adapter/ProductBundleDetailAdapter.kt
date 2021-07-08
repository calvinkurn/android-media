package com.tokopedia.product_bundle.multiple.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleDetail
import com.tokopedia.product_bundle.multiple.presentation.viewholder.ProductBundleDetailViewHolder

class ProductBundleDetailAdapter(private val clickListener: ProductBundleDetailItemClickListener)
    : RecyclerView.Adapter<ProductBundleDetailViewHolder>() {

    interface ProductBundleDetailItemClickListener {
        fun onProductVariantSpinnerClicked(productBundleItem: BundleItem)
    }

    private var productBundleDetails: List<ProductBundleDetail> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductBundleDetailViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.product_bundle_detail_item, parent, false)
        return ProductBundleDetailViewHolder(rootView, clickListener)
    }

    override fun getItemCount(): Int {
        return productBundleDetails.size
    }

    override fun onBindViewHolder(holder: ProductBundleDetailViewHolder, position: Int) {
        val bundleDetail = productBundleDetails[position]
        holder.bindData(bundleDetail)
    }

    fun setProductBundleDetails(productBundleDetails: List<ProductBundleDetail>) {
        this.productBundleDetails = productBundleDetails
        notifyDataSetChanged()
    }
}