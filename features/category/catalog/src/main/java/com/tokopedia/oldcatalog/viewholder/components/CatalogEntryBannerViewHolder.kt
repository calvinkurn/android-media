package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.datamodel.CatalogEntryBannerDataModel
import com.tokopedia.oldcatalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogEntryBannerViewHolder(
    private val view: View,
    private val catalogDetailListener: CatalogDetailListener
) : AbstractViewHolder<CatalogEntryBannerDataModel>(view) {

    private val parentLayout: ConstraintLayout by lazy {
        view.findViewById(R.id.entry_point_parent)
    }

    private val categoryName: Typography by lazy {
        view.findViewById(R.id.catalog_lib_category_tv)
    }

    private val productCountText: Typography by lazy {
        view.findViewById(R.id.catalog_lib_product_count_tv)
    }

    private val imageLeft: ImageUnify by lazy {
        view.findViewById(R.id.catalog_image_left)
    }

    private val imageRight: ImageUnify by lazy {
        view.findViewById(R.id.catalog_image_right)
    }

    companion object {
        val LAYOUT = R.layout.item_view_catalog_entry_point
    }

    override fun bind(element: CatalogEntryBannerDataModel) {
        renderText(element.categoryProductCount, element.categoryName)
        renderImages(element.catalogs)
        parentLayout.setOnClickListener {
            catalogDetailListener.entryPointBannerClicked(element.categoryName ?: "")
        }
    }

    private fun renderText(categoryProductCount: String?, category: String?) {
        productCountText.text = view.context.getString(R.string.catalog_entry_banner_product_count, categoryProductCount)
        categoryName.text = view.context.getString(R.string.catalog_entry_banner_title, category)
    }

    private fun renderImages(catalogs: ArrayList<CatalogImage>?) {
        val leftImageUrl = catalogs?.firstOrNull()?.imageURL ?: ""
        renderImage(imageLeft, catalogs?.firstOrNull()?.appLink ?: "", leftImageUrl)
        if ((catalogs?.size ?: 0) > 1) {
            val rightImageUrl = catalogs?.get(1)?.imageURL ?: ""
            renderImage(imageRight, catalogs?.get(1)?.appLink ?: "", rightImageUrl)
        } else {
            imageRight.hide()
        }
    }

    private fun renderImage(image: ImageUnify, appLink: String, imageUrl: String) {
        if (imageUrl.isNotBlank()) {
            image.apply {
                show()
                setOnClickListener {
                    catalogDetailListener.entryPointBannerImageClicked(appLink)
                }
                loadImage(imageUrl)
            }
        } else {
            image.hide()
        }
    }
}
