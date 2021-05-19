package com.tokopedia.play.ui.variantsheet.adapter.delegate

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder

/**
 * Created by mzennis on 2020-03-10.
 */
class VariantCategoryAdapterDelegate(
        listener: ProductVariantListener
) : TypedAdapterDelegate<VariantCategory, Any, VariantContainerViewHolder>(com.tokopedia.variant_common.R.layout.item_variant_container_view_holder), ProductVariantListener by listener {

    override fun onBindViewHolder(item: VariantCategory, holder: VariantContainerViewHolder) {
        holder.bind(item)
    }

    override fun onBindViewHolderWithPayloads(item: VariantCategory, holder: VariantContainerViewHolder, payloads: Bundle) {
        val isOptionChanged = payloads.getBoolean(VariantContainerViewHolder.VARIANT_OPTION_CHANGED)
        holder.bind(item, isOptionChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): VariantContainerViewHolder {
        return VariantContainerViewHolder(basicView, this)
    }
}