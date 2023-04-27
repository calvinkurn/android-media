package com.tokopedia.catalog.viewholder.components

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalog.R
import com.tokopedia.catalog.listener.CatalogDetailListener
import com.tokopedia.catalog.model.datamodel.CatalogEntryBannerDataModel
import com.tokopedia.catalog.model.raw.CatalogImage
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
        if (leftImageUrl.isNotBlank()) {
            imageLeft.show()
            imageLeft.loadImage(leftImageUrl)
        } else {
            imageLeft.hide()
        }
        if ((catalogs?.size ?: 0) > 1) {
            val rightImageUrl = catalogs?.get(1)?.imageURL ?: ""
            if (rightImageUrl.isNotBlank()) {
                imageRight.show()
                imageRight.loadImage(rightImageUrl)
            } else {
                imageRight.hide()
            }
        } else {
            imageRight.hide()
        }
    }
}
