package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster
import com.tokopedia.product_service_widget.R
import com.tokopedia.unifyprinciples.Typography

class ProductBundleMasterOneItemViewHolder(
    itemView: View
) : ProductBundleMasterBaseViewHolder(itemView) {

    private var tvProductBundleView: Typography? = itemView.findViewById(R.id.tv_product_bundle_master)

    override fun bindData(productBundleMaster: ProductBundleMaster, state: ProductBundleChipState) {
        tvProductBundleView?.text = productBundleMaster.bundleName
    }
}