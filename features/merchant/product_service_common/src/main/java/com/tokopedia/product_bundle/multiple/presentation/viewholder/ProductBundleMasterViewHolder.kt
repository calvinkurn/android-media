package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.product_bundle.multiple.presentation.adapter.ProductBundleMasterAdapter.ProductBundleMasterItemClickListener
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_service_common.R
import com.tokopedia.unifycomponents.ChipsUnify

class ProductBundleMasterViewHolder(
    itemView: View,
    clickListener: ProductBundleMasterItemClickListener
) : ProductBundleMasterBaseViewHolder(itemView) {

    private var context: Context? = null
    private var productBundleChipView: ChipsUnify? = null

    init {
        this.context = itemView.context
        this.productBundleChipView = itemView.findViewById(R.id.cu_product_bundle_master)
        this.productBundleChipView?.setOnClickListener { view ->
            val productBundleMasterObj = view.getTag(R.id.product_bundle_master_tag)
            productBundleMasterObj?.let { obj ->
                val productBundleMaster = obj as ProductBundleMaster
                clickListener.onProductBundleMasterItemClicked(adapterPosition, productBundleMaster)
            }
        }
    }

    override fun bindData(productBundleMaster: ProductBundleMaster, state: ProductBundleChipState) {
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