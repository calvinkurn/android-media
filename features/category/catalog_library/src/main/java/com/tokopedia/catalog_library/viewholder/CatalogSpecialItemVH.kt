package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogSpecialDM
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.util.ActionKeys
import com.tokopedia.catalog_library.util.CatalogAnalyticsHomePage
import com.tokopedia.catalog_library.util.EventKeys
import com.tokopedia.catalog_library.util.TrackerId
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogSpecialItemVH(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : CatalogLibraryAbstractViewHolder<CatalogSpecialDM>(view) {

    private var dataModel: CatalogSpecialDM? = null

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

    override fun bind(element: CatalogSpecialDM?) {
        dataModel = element
        val specialDataListItem = element?.specialDataListItem
        specialDataListItem?.iconUrl?.let { iconUrl ->
            specialImage.loadImage(iconUrl)
        }
        specialLayout.background = view.context.getDrawable(R.drawable.squircle)
        specialTitle.text = specialDataListItem?.name ?: ""
        specialTitle.setOnClickListener {
            specialLayoutClicked(specialDataListItem)
        }
        specialLayout.setOnClickListener {
            specialLayoutClicked(specialDataListItem)
        }
    }

    private fun specialLayoutClicked(specialDataListItem: CatalogSpecialResponse.CatalogCategorySpecial.CatalogSpecialData?) {
        catalogLibraryListener.onCategoryItemClicked((specialDataListItem?.id.toString()))

        CatalogAnalyticsHomePage.sendClickCategoryOnSpecialCategoriesEvent(
            specialDataListItem?.name ?: "",
            specialDataListItem?.id.toString(),
            UserSession(itemView.context).userId
        )
    }

    override fun onViewAttachedToWindow() {
        dataModel?.specialDataListItem?.let {
            catalogLibraryListener.categoryHorizontalCarouselImpression(
                EventKeys.CREATIVE_NAME_SPECIAL_VALUE,
                layoutPosition + 1,
                dataModel?.specialDataListItem?.id.toString(),
                dataModel?.specialDataListItem?.name ?: "",
                UserSession(itemView.context).userId,
                TrackerId.IMPRESSION_ON_SPECIAL_CATEGORIES,
                ActionKeys.IMPRESSION_ON_SPECIAL_CATEGORIES
            )
        }
    }
}
