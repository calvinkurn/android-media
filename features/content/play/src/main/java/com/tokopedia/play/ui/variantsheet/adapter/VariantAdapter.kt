package com.tokopedia.play.ui.variantsheet.adapter

import android.os.Bundle
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.variantsheet.adapter.delegate.VariantAdapterDelegate
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder.Companion.VARIANT_OPTION_CHANGED

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

    override fun getChangePayload(oldItem: VariantCategory, newItem: VariantCategory): Bundle {
        val bundle = Bundle()
        bundle.putBoolean(VARIANT_OPTION_CHANGED, oldItem.variantOptions != newItem.variantOptions)
        return bundle
    }
}