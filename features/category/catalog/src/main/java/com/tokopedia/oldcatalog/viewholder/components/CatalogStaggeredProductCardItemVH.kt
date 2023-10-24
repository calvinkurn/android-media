package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogStaggeredProductModel
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class CatalogStaggeredProductCardItemVH(itemView: View, val catalogDetailListener: CatalogDetailListener?)
    : AbstractViewHolder<CatalogStaggeredProductModel>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        var LAYOUT = R.layout.catalog_staggered_product_card_item_layout

    }

    override fun bind(element: CatalogStaggeredProductModel?) {
        itemView.findViewById<Typography>(R.id.catalog_comparison_product_brand).displayTextOrHide("${element?.comparisonItem?.brand}")
        itemView.findViewById<Typography>(R.id.catalog_comparison_product_name).apply {
            displayTextOrHide(element?.comparisonItem?.name ?: "")
            setWeight(Typography.BOLD)
        }
        itemView.findViewById<Typography>(R.id.catalog_comparison_product_price).apply {
            displayTextOrHide("${element?.comparisonItem?.marketPrice?.firstOrNull()?.minFmt} - ${element?.comparisonItem?.marketPrice?.firstOrNull()?.maxFmt}")
            setType(Typography.BODY_3)
            setWeight(Typography.REGULAR)
        }
        element?.comparisonItem?.catalogImage?.firstOrNull()?.let { image ->
            itemView.findViewById<ImageUnify>(R.id.catalog_comparison_image).loadImageWithoutPlaceholder(image.imageUrl ?: "")
        }
        itemView.findViewById<UnifyButton>(R.id.catalog_comparison_bandingkan_button)?.apply {
            show()
            if(element?.comparisonItem?.isActive == false){
                isEnabled = false
                text = getString(R.string.catalog_selected)
                setOnClickListener(null)
            }else {
                isEnabled = true
                text = getString(R.string.catalog_bandingkan)
                setOnClickListener {
                    catalogDetailListener?.changeComparison(element?.comparisonItem?.id ?: "")
                }
            }

        }
    }
}
