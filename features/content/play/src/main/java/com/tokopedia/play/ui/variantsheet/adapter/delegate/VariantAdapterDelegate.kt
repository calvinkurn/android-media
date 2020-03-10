package com.tokopedia.play.ui.variantsheet.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.variant_common.model.VariantCategory
import com.tokopedia.variant_common.view.ProductVariantListener
import com.tokopedia.variant_common.view.holder.VariantContainerViewHolder


/**
 * Created by mzennis on 2020-03-10.
 */
class VariantAdapterDelegate(
        listener: ProductVariantListener
) : TypedAdapterDelegate<VariantCategory, VariantCategory, VariantContainerViewHolder>(R.layout.item_variant_container_view_holder), ProductVariantListener by listener {

    override fun onBindViewHolder(item: VariantCategory, holder: VariantContainerViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): VariantContainerViewHolder {
        return VariantContainerViewHolder(basicView, this)
    }
}