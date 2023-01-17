package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import kotlin.LazyThreadSafetyMode.NONE

class CatalogLihatItemViewHolder(
    val view: View,
    private val catalogLibraryListener:
        CatalogLibraryListener
) : AbstractViewHolder<CatalogLihatItemDataModel>(view) {

    private val lihatItemIcon: ImageUnify? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_icon)
    }

    private val lihatItemTitle: Typography? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_title)
    }

    private val lihatExpandedItemLayout: CardUnify2? by lazy(NONE) {
        itemView.findViewById(R.id.img_card)
    }

    companion object {
        val LAYOUT = R.layout.item_lihat_grid
    }

    override fun bind(element: CatalogLihatItemDataModel?) {
        val childDataItem = element?.catalogLibraryChildDataListItem
        childDataItem?.categoryIconUrl?.let {
            lihatItemIcon?.loadImage(it)
        }
        lihatItemTitle?.text = childDataItem?.categoryName ?: ""
        lihatItemTitle?.setOnClickListener {
            it.setOnClickListener {
                catalogLibraryListener.onCategoryItemClicked(
                    childDataItem?.categoryIdentifier ?: ""
                )
            }
        }
        lihatExpandedItemLayout?.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(
                childDataItem?.categoryIdentifier ?: ""
            )
        }
    }
}
