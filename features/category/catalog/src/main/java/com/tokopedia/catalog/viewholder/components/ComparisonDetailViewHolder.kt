package com.tokopedia.catalog.viewholder.components

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.raw.ComparisionModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton

class ComparisonDetailViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    fun bind(baseCatalog: ComparisionModel?, comparisonCatalog: ComparisionModel?, catalogDetailListener: CatalogDetailListener?) {
        baseCatalog?.run {
            itemView.findViewById<LinearLayout>(R.id.base_catalog_card)?.apply {
                findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_brand).displayTextOrHide(brand ?: "")
                findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_name).displayTextOrHide(name ?: "")
                findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_price).displayTextOrHide(price ?: "")
                url?.let {imageUrl ->
                    findViewById<ImageUnify>(R.id.catalog_comparison_image).loadImageWithoutPlaceholder(imageUrl)
                }
                findViewById<UnifyButton>(R.id.catalog_comparison_bandingkan_button).invisible()
                findViewById<CardUnify>(R.id.catalog_card).apply {
                    cardType = CardUnify.TYPE_BORDER
                    val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    params.setMargins(0, 0, 0,
                        itemView.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt())
                    layoutParams = params
                }
            }

        }
        comparisonCatalog?.run {
            itemView.findViewById<LinearLayout>(R.id.comparision_card)?.apply {
                findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_brand).displayTextOrHide(brand ?: "")
                findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_name).displayTextOrHide(name ?: "")
                findViewById<com.tokopedia.unifyprinciples.Typography>(R.id.catalog_comparison_product_price).displayTextOrHide(price ?: "")
                url?.let {imageUrl ->
                    findViewById<ImageUnify>(R.id.catalog_comparison_image).loadImageWithoutPlaceholder(imageUrl)
                }
                findViewById<UnifyButton>(R.id.catalog_comparison_bandingkan_button)?.apply {
                    text = itemView.context.getString(com.tokopedia.catalog.R.string.catalog_ganti_perbandingan)
                    show()
                    setOnClickListener {
                        catalogDetailListener?.openComparisonBottomSheet(comparisonCatalog)
                    }
                }
            }
        }

        itemView.findViewById<LinearLayout>(R.id.comparision_card).setOnClickListener {
            comparisonCatalog?.id?.let { catalogId ->
                if(catalogId.isNotEmpty()){
                    catalogDetailListener?.comparisonCatalogClicked(catalogId)
                }
            }
        }
    }
}
