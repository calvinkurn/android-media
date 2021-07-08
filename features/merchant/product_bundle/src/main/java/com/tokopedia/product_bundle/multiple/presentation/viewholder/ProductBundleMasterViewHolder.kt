package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.R
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.unifycomponents.ChipsUnify

class ProductBundleMasterViewHolder(itemView: View, clickListener: ProductBundleMasterItemClickListener)
    : RecyclerView.ViewHolder(itemView) {

    private var productBundleChipView: ChipsUnify? = null

    enum class ProductBundleChipState {
        SELECTED,
        NORMAL,
    }

    init {
        productBundleChipView = itemView.findViewById(R.id.cu_product_bundle_master)
        productBundleChipView?.setOnClickListener {
            val productBundleMasterObj = it.getTag(R.id.product_bundle_master_tag)
            productBundleMasterObj?.let { obj ->
                val productBundleMaster = obj as ProductBundleMaster
                clickListener.onProductBundleMasterItemClicked(adapterPosition, productBundleMaster)
            }
        }
    }

    fun bindData(productBundleMaster: ProductBundleMaster, state: ProductBundleChipState) {
        productBundleChipView?.setTag(R.id.product_bundle_master_tag, productBundleMaster)
        productBundleChipView?.chip_text?.text = productBundleMaster.bundleName
        when (state) {
            ProductBundleChipState.NORMAL ->
                productBundleChipView?.chipType = ChipsUnify.TYPE_NORMAL
            ProductBundleChipState.SELECTED ->
                productBundleChipView?.chipType = ChipsUnify.TYPE_SELECTED
        }
    }
}