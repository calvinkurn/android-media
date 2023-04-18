package com.tokopedia.catalog.viewholder.components

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogEntryBannerDataModel
import com.tokopedia.catalog.model.raw.ComparisonNewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class EntryBannerViewHolder(private val view : View,
                                       private val catalogDetailListener: CatalogDetailListener): AbstractViewHolder<CatalogEntryBannerDataModel>(view) {

    private val categoryName : Typography by lazy {
        view.findViewById(R.id.catalog_lib_category_tv)
    }

    private val productCountText : Typography by lazy {
        view.findViewById(R.id.catalog_lib_product_count_tv)
    }

    private val imageLeft : ImageUnify by lazy {
        view.findViewById(R.id.catalog_image_left)
    }

    private val imageRight : ImageUnify by lazy {
        view.findViewById(R.id.catalog_image_right)
    }

    companion object {
        val LAYOUT = R.layout.item_view_catalog_entry_point
    }

    override fun bind(element: CatalogEntryBannerDataModel) {
        renderText(element.categoryProductCount,element.categoryName)
        renderImages(element.catalogs)
    }

    private fun renderText(categoryProductCount : String? , category : String?) {
        productCountText.text = "Temukan ${categoryProductCount} Katalog lainnya"
        categoryName.text = "di ${category}"
    }

    private fun renderImages(catalogs: ArrayList<ComparisonNewModel>?) {
        val leftImageUrl = catalogs?.firstOrNull()?.imageUrl ?: ""
        imageLeft.loadImage(leftImageUrl)
        val rightImageUrl = catalogs?.get(1)?.imageUrl ?: ""
        imageRight.loadImage(rightImageUrl)
    }
}
