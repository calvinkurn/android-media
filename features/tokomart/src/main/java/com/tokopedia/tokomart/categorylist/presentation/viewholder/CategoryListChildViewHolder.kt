package com.tokopedia.tokomart.categorylist.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokomart.R
import com.tokopedia.tokomart.categorylist.presentation.uimodel.CategoryListChildUiModel
import kotlinx.android.synthetic.main.item_tokomart_category_child.view.*

class CategoryListChildViewHolder(itemView: View): AbstractViewHolder<CategoryListChildUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_category_child
    }

    override fun bind(data: CategoryListChildUiModel) {
        itemView.run {
            textTitle.text = data.title
            data.iconUrl?.let {
                imageCategory.loadImage(it)
                imageCategory.show()
            }
        }
    }

    fun hideDivider() {
        itemView.categoryDivider.hide()
    }
}
