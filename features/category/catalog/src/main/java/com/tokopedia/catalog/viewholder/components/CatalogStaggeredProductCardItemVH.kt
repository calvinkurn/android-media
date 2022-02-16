package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogStaggeredProductModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

class CatalogStaggeredProductCardItemVH(itemView: View, val catalogDetailListener: CatalogDetailListener?)
    : AbstractViewHolder<CatalogStaggeredProductModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.catalog_staggered_product_card_item_layout

    }

    override fun bind(element: CatalogStaggeredProductModel?) {
        itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_brand).displayTextOrHide("${element?.comparisonItem?.brand} pos : $adapterPosition" ?: "")
        itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_name).displayTextOrHide(element?.comparisonItem?.name ?: "")
        itemView.findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_price).
        displayTextOrHide("${element?.comparisonItem?.marketPrice?.firstOrNull()?.minFmt} - ${element?.comparisonItem?.marketPrice?.firstOrNull()?.maxFmt}")
        element?.comparisonItem?.catalogImage?.firstOrNull()?.let { image ->
            itemView.findViewById<ImageUnify>(R.id.catalog_comparison_image).loadImageWithoutPlaceholder(image.imageUrl ?: "")
        }
        itemView.findViewById<UnifyButton>(R.id.catalog_comparison_bandingkan_button)?.apply {
            show()
            if(element?.comparisonItem?.isActive == false){
                isEnabled = false
                setOnClickListener(null)
            }else {
                isEnabled = true
                setOnClickListener {
                    catalogDetailListener?.changeComparison(element?.comparisonItem?.id ?: "")
                }
            }

        }
    }
}
