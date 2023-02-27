package com.tokopedia.catalog_library.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.datamodel.CatalogSpecialDataModel
import com.tokopedia.catalog_library.model.raw.CatalogSpecialResponse
import com.tokopedia.catalog_library.util.AnalyticsHomePage
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession

class CatalogSpecialItemViewHolder(
    val view: View,
    private val catalogLibraryListener: CatalogLibraryListener
) : AbstractViewHolder<CatalogSpecialDataModel>(view) {

    private var dataModel: CatalogSpecialDataModel? = null

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

        AnalyticsHomePage.sendClickCategoryOnSpecialCategoriesEvent(
            specialDataListItem?.name ?: "",
            specialDataListItem?.id.toString(),
            UserSession(itemView.context).userId
        )
    }

    override fun onViewAttachedToWindow() {
        if (dataModel?.impressHolder?.isInvoke != true) {
            dataModel?.specialDataListItem?.let {
                AnalyticsHomePage.sendImpressionOnSpecialCategoriesEvent(
                    layoutPosition + 1,
                    dataModel?.specialDataListItem?.id.toString(),
                    dataModel?.specialDataListItem?.name ?: "",
                    UserSession(itemView.context).userId
                )

                dataModel?.impressHolder?.invoke()
            }
        }
    }
}
