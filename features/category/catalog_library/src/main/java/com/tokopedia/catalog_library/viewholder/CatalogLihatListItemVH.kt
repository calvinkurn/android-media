package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogLihatListItemDM
import com.tokopedia.catalog_library.util.ActionKeys
import com.tokopedia.catalog_library.util.CatalogAnalyticsLihatSemuaPage
import com.tokopedia.catalog_library.util.CategoryKeys
import com.tokopedia.catalog_library.util.TrackerId
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import kotlin.LazyThreadSafetyMode.NONE

class CatalogLihatListItemVH(
    val view: View,
    private val catalogLibraryListener:
        CatalogLibraryListener
) : CatalogLibraryAbstractViewHolder<CatalogLihatListItemDM>(view) {
    var dataModel: CatalogLihatListItemDM? = null

    private val lihatItemIcon: ImageUnify? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_icon)
    }

    private val lihatItemIconCheck: IconUnify? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_icon_checked)
    }

    private val lihatItemTitle: Typography? by lazy(NONE) {
        itemView.findViewById(R.id.lihat_item_title)
    }

    private val lihatExpandedItemLayout: View? by lazy(NONE) {
        itemView.findViewById(R.id.main_item_layout)
    }

    companion object {
        val LAYOUT = R.layout.item_catalog_library_lihat_list
    }

    override fun bind(element: CatalogLihatListItemDM?) {
        dataModel = element
        val childDataItem = element?.catalogLibraryChildDataListItem
        childDataItem?.categoryIconUrl?.let {
            lihatItemIcon?.loadImage(it)
        }
        if (dataModel?.activeCategoryId == dataModel?.catalogLibraryChildDataListItem?.categoryId) {
            lihatItemIconCheck?.show()
        } else {
            lihatItemIconCheck?.hide()
        }
        lihatItemTitle?.text = childDataItem?.categoryName ?: ""
        lihatExpandedItemLayout?.setOnClickListener {
            CatalogAnalyticsLihatSemuaPage.sendClickCategoryOnCategoryListEvent(
                element?.rootCategoryName ?: "",
                element?.rootCategoryId ?: "",
                element?.catalogLibraryChildDataListItem?.categoryName ?: "",
                element?.catalogLibraryChildDataListItem?.categoryId ?: "",
                element?.isAsc ?: true,
                UserSession(itemView.context).userId
            )
            catalogLibraryListener.onCategoryItemClicked(
                childDataItem?.categoryId ?: "",
                childDataItem?.categoryName ?: ""
            )
        }
    }

    override fun onViewAttachedToWindow() {
        dataModel?.let {
            catalogLibraryListener.categoryListImpression(
                TrackerId.IMPRESSION_ON_CATEGORY_LIST_BRAND_LANDING,
                CategoryKeys.CATALOG_LIBRARY_POPULAR_BRAND_LANDING_PAGE,
                ActionKeys.IMPRESSION_ON_CATEGORY_LIST_BS,
                "",
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
