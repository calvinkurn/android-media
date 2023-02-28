package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatItemDataModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlin.LazyThreadSafetyMode.NONE

class CatalogLihatItemViewHolder(
    val view: View,
    private val catalogLibraryListener:
        CatalogLibraryListener
) : AbstractViewHolder<CatalogLihatItemDataModel>(view) {
    var dataModel: CatalogLihatItemDataModel? = null

    private val lihatItemIcon: ImageUnify? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_icon)
    }

    private val lihatItemTitle: Typography? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_title)
    }

    private val lihatExpandedItemLayout: View? by lazy(NONE) {
        itemView.findViewById(R.id.view_background)
    }

    companion object {
        val LAYOUT = R.layout.item_lihat_grid
    }

    override fun bind(element: CatalogLihatItemDataModel?) {
        dataModel = element
        val childDataItem = element?.catalogLibraryChildDataListItem
        childDataItem?.categoryIconUrl?.let {
            lihatItemIcon?.loadImage(it)
        }
        lihatItemTitle?.text = childDataItem?.categoryName ?: ""
        lihatItemTitle?.setOnClickListener {
            it.setOnClickListener {
                catalogLibraryListener.onCategoryItemClicked(
                    childDataItem?.categoryId ?: ""
                )
            }
        }
        lihatExpandedItemLayout?.background = view.context.getDrawable(R.drawable.squircle)
        lihatExpandedItemLayout?.setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(
                childDataItem?.categoryId ?: ""
            )
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.let {
            catalogLibraryListener.categoryListImpression(
                it.rootCategoryName,
                it.rootCategoryId,
                it.catalogLibraryChildDataListItem.categoryName ?: "",
                it.catalogLibraryChildDataListItem.categoryId ?: "",
                it.isGrid,
                it.isAsc,
                layoutPosition + 1,
                UserSession(itemView.context).userId
            )
        }
    }
}
