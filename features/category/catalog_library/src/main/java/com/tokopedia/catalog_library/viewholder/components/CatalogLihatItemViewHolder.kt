package com.tokopedia.catalog_library.viewholder.components

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.catalog_library.R
import com.tokopedia.catalog_library.listener.CatalogLibraryListener
import com.tokopedia.catalog_library.model.raw.CatalogLibraryResponse
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class CatalogLihatItemViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    fun bind(childCategoryData: CatalogLibraryResponse.CategoryListLibraryPage.CategoryData.ChildCategoryList, catalogLibraryListener: CatalogLibraryListener){
        childCategoryData.categoryIconUrl?.let { iconUrl ->
            view.findViewById<ImageUnify>(R.id.lihat_item_icon)
                ?.loadImageWithoutPlaceholder(iconUrl)

        }
        view.findViewById<Typography>(R.id.lihat_item_title)?.text =
            childCategoryData.categoryName
        view.findViewById<ConstraintLayout>(R.id.lihat_expanded_item_layout).setOnClickListener {
            catalogLibraryListener.onCategoryItemClicked(childCategoryData.categoryName)
        }
    }
}
