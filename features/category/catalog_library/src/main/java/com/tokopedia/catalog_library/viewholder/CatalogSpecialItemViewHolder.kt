package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogSpecialDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogSpecialItemViewHolder(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : AbstractViewHolder<CatalogSpecialDataModel>(view) {

    private val specialImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.special_icon)
    }

    private val specialTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.special_title)
    }

    private val specialLayout: CardUnify2 by lazy(LazyThreadSafetyMode.NONE) {
        view.findViewById(R.id.img_card)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_special
    }

    override fun bind(element: CatalogSpecialDataModel?) {
        val specialDataListItem = element?.specialDataListItem
        specialDataListItem?.iconUrl?.let { iconUrl ->
            specialImage.loadImage(iconUrl)
        }
        specialTitle.text = specialDataListItem?.name ?: ""
        specialTitle.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(specialDataListItem?.categoryIdentifier)
        }
        specialLayout.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(specialDataListItem?.categoryIdentifier)
        }
    }
}
