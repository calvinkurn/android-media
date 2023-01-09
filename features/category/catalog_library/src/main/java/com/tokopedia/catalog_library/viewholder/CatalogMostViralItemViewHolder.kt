package com.tokopedia.catalog_library.viewholder

import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogMostViralDataModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogMostViralItemViewHolder(val view: View, private val catalogLibraryListener: CatalogLibraryListener): AbstractViewHolder<CatalogMostViralDataModel>(view) {

    private val mostViralImage = view.findViewById<ImageUnify>(R.id.catalog_product_viral_image)
    private val mostViralTitle = view.findViewById<Typography>(R.id.catalog_product_viral_title)
    private val mostViralIcon = view.findViewById<IconUnify>(R.id.catalog_most_viral_icon)
    private val mostViralLayout = view.findViewById<LinearLayout>(R.id.catalog_most_viral_item_layout)

    companion object {
        val LAYOUT = R.layout.item_catalog_most_viral
    }

    override fun bind(element: CatalogMostViralDataModel?) {
        element?.catalogMostViralData?.imageUrl?.let { iconUrl ->
            mostViralImage.loadImageWithoutPlaceholder(iconUrl)
        }
        mostViralTitle.text = element?.catalogMostViralData?.name
        mostViralIcon.apply {
            setImage(
                newLightEnable = MethodChecker.getColor(itemView.context,com.tokopedia.unifyprinciples.R.color.Unify_Static_White)
            )
        }
        mostViralLayout.setOnClickListener {
            catalogLibraryListener.onProductCardClicked(element?.catalogMostViralData?.applink)
        }
    }
}
