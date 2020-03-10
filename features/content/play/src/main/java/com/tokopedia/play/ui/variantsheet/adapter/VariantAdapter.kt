package com.tokopedia.play.ui.variantsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.variantsheet.adapter.delegate.VariantAdapterDelegate
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener


/**
 * Created by mzennis on 2020-03-10.
 */
class VariantAdapter(
        listener: ProductVariantListener
) : BaseDiffUtilAdapter<VariantCategory>(), ProductVariantListener by listener {

    init {
        delegatesManager
                .addDelegate(VariantAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: VariantCategory, newItem: VariantCategory): Boolean {
        return oldItem.identifier == newItem.identifier
    }

    override fun areContentsTheSame(oldItem: VariantCategory, newItem: VariantCategory): Boolean {
        return oldItem == newItem
    }
}