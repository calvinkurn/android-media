package com.tokopedia.play.ui.variantsheet.adapter

import android.os.Bundle
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.variantsheet.adapter.delegate.VariantCategoryAdapterDelegate
import com.tokopedia.play.ui.variantsheet.adapter.delegate.VariantPlaceholderAdapterDelegate
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder.Companion.VARIANT_OPTION_CHANGED

/**
 * Created by mzennis on 2020-03-10.
 */
class VariantAdapter(
        listener: AtcVariantListener
) : BaseDiffUtilAdapter<Any>(), AtcVariantListener by listener {

    init {
        delegatesManager
                .addDelegate(VariantCategoryAdapterDelegate(listener))
                .addDelegate(VariantPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is VariantCategory && newItem is VariantCategory) oldItem.identifier == newItem.identifier
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Any, newItem: Any): Bundle? {
        return if (oldItem is VariantCategory && newItem is VariantCategory) {
            val bundle = Bundle()
            bundle.putBoolean(VARIANT_OPTION_CHANGED, oldItem.variantOptions != newItem.variantOptions)
            bundle
        } else null
    }
}