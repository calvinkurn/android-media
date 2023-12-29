package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.ComparisonNewModel
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.CardUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ComparisonDetailNewViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(featureLeftData: ComparisonNewModel?, featureRightData: ComparisonNewModel?, catalogDetailListener: CatalogDetailListener?) {
        featureLeftData?.run {
            view.findViewById<LinearLayout>(R.id.base_catalog_card)?.apply {
                findViewById<Typography>(R.id.catalog_comparison_product_brand).displayTextOrHide(brand ?: "")
                findViewById<Typography>(R.id.catalog_comparison_product_name).displayTextOrHide(name ?: "")
                findViewById<Typography>(R.id.catalog_comparison_product_price).displayTextOrHide(price ?: "")
                imageUrl?.let { imageUrl ->
                    findViewById<ImageUnify>(R.id.catalog_comparison_image).loadImageWithoutPlaceholder(imageUrl)
                }
                findViewById<UnifyButton>(R.id.catalog_comparison_bandingkan_button).invisible()
                findViewById<CardUnify>(R.id.catalog_card).apply {
                    cardType = CardUnify.TYPE_BORDER
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT)
                    params.setMargins(0, 0, 0,
                        view.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2).toPx().toInt())
                    layoutParams = params
                }
            }

        }
        featureRightData?.run {
            view.findViewById<LinearLayout>(R.id.comparision_card)?.apply {
                findViewById<Typography>(R.id.catalog_comparison_product_brand).displayTextOrHide(brand ?: "")
                findViewById<Typography>(R.id.catalog_comparison_product_name).displayTextOrHide(name ?: "")
                findViewById<Typography>(R.id.catalog_comparison_product_price).displayTextOrHide(price ?: "")
                imageUrl?.let { imageUrl ->
                    findViewById<ImageUnify>(R.id.catalog_comparison_image).loadImageWithoutPlaceholder(imageUrl)
                }
                findViewById<UnifyButton>(R.id.catalog_comparison_bandingkan_button)?.apply {
                    text = view.context.getString(R.string.catalog_ganti_perbandingan)
                    show()
                    setOnClickListener {
                        catalogDetailListener?.openComparisonNewBottomSheet(featureRightData)
                    }
                }
            }
        }

        view.findViewById<LinearLayout>(R.id.comparision_card).setOnClickListener {
            featureRightData?.id?.let { catalogId ->
                if(catalogId.isNotEmpty()){
                    catalogDetailListener?.comparisonNewCatalogClicked(catalogId)
                }
            }
        }
    }
}
