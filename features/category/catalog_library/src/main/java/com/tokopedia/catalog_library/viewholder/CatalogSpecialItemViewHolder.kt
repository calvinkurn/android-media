package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogSpecialDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogSpecialItemViewHolder(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : AbstractViewHolder<CatalogSpecialDataModel>(view) {

    private val specialImage: ImageUnify by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.special_icon)
    }

    private val specialTitle: Typography by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.special_title)
    }

    private val specialLayout: View by lazy(LazyThreadSafetyMode.NONE) {
        itemView.findViewById(R.id.view_background)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_special
    }

    override fun bind(element: CatalogSpecialDataModel?) {
        val specialDataListItem = element?.specialDataListItem
        specialDataListItem?.iconUrl?.let { iconUrl ->
            specialImage.loadImage(iconUrl)
        }
        specialLayout.background = view.context.getDrawable(R.drawable.squircle)
        specialTitle.text = specialDataListItem?.name ?: ""
        specialTitle.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(specialDataListItem?.categoryIdentifier)
        }
        specialLayout.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(specialDataListItem?.categoryIdentifier)
        }
    }
}
