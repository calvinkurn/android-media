package com.tokopedia.tokomart.categorylist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel.CategoryType
import kotlinx.android.synthetic.main.item_tokomart_category_list.view.*

class CategoryListItemViewHolder(
    itemView: View,
    private val listener: CategoryListListener
): AbstractViewHolder<CategoryListChildUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_list
    }

    override fun bind(category: CategoryListChildUiModel) {
        itemView.run {
            textTitle.text = if(category.type == CategoryType.ALL_CATEGORY_TEXT) {
                getString(R.string.tokomart_all_category_text_format, category.name)
            } else {
                category.name
            }
            textTitle.setWeight(category.textWeight)
            textTitle.setTextColor(ContextCompat.getColor(context, category.textColorId))

            category.imageUrl?.let {
                imageCategory.loadImage(it)
                imageCategory.show()
            }

            setOnClickListener {
                RouteManager.route(context, category.appLink)
                listener.onClickCategory()
            }
        }
    }

    interface CategoryListListener {
        fun onClickCategory()
    }
}
