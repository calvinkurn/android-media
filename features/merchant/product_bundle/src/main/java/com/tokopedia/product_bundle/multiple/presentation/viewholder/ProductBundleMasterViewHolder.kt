package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.unifycomponents.ChipsUnify

class ProductBundleMasterViewHolder(itemView: View, clickListener: ProductBundleMasterItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var productBundleMasterChipView: ChipsUnify? = null

    init {
        productBundleMasterChipView = itemView.findViewById(R.id.cu_product_bundle_master)
        productBundleMasterChipView?.setOnClickListener {
            val productBundleInfoObj = it.getTag(R.id.product_bundle_info_tag)
            productBundleInfoObj?.let { obj ->
                val productBundleInfo = obj as BundleInfo
                clickListener.onProductBundleMasterItemClicked(productBundleInfo)
            }
        }
    }

    fun bindData(bundleInfo: BundleInfo) {
        productBundleMasterChipView?.chip_text?.text = bundleInfo.name
    }
}