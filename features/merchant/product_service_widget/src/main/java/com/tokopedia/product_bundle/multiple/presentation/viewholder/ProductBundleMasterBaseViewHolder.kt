package com.tokopedia.product_bundle.multiple.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product_bundle.multiple.presentation.model.ProductBundleMaster

abstract class ProductBundleMasterBaseViewHolder (
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    enum class ProductBundleChipState {
        SELECTED,
        NORMAL,
    }

    abstract fun bindData(productBundleMaster: ProductBundleMaster, state: ProductBundleChipState)
}