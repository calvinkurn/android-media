package com.tokopedia.product.detail.view.viewholder.product_variant_thumbail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.view.AtcVariantListener
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by Yovi.Putra on 10/01/23
 */
class ProductThumbnailVariantAdapter(
    private val atcListener: AtcVariantListener,
    private val pdpListener: DynamicProductDetailListener
) : ListAdapter<VariantOptionWithAttribute, ThumbnailVariantViewHolder>(DIFF_ITEM) {

    override fun onBindViewHolder(holder: ThumbnailVariantViewHolder, position: Int) {
        holder.bind(element = getItem(position))
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ThumbnailVariantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(ThumbnailVariantViewHolder.LAYOUT, parent, false)
        return ThumbnailVariantViewHolder(view, atcListener, pdpListener)
    }

    companion object {
        private val DIFF_ITEM = object : DiffUtil.ItemCallback<VariantOptionWithAttribute>() {
            override fun areItemsTheSame(
                oldItem: VariantOptionWithAttribute,
                newItem: VariantOptionWithAttribute
            ): Boolean = oldItem.variantId == newItem.variantId

            override fun areContentsTheSame(
                oldItem: VariantOptionWithAttribute,
                newItem: VariantOptionWithAttribute
            ): Boolean = oldItem.variantCategoryKey == newItem.variantCategoryKey &&
                oldItem.currentState == newItem.currentState &&
                oldItem.flashSale == newItem.flashSale
        }
    }
}
