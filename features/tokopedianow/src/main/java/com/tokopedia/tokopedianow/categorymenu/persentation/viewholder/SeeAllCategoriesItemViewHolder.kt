package com.tokopedia.tokopedianow.categorymenu.persentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.categorymenu.persentation.uimodel.SeeAllCategoriesItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryMenuItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class SeeAllCategoriesItemViewHolder(
    itemView: View
): AbstractViewHolder<SeeAllCategoriesItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_see_all_categories
    }

    private var binding: ItemTokopedianowCategoryMenuItemBinding? by viewBinding()

    override fun bind(data: SeeAllCategoriesItemUiModel) {
        binding?.apply {
            tpCategoryTitle.text = data.title
            ivCategoryImage.loadImage(data.imageUrl)
            root.setOnClickListener {
//                setLayoutClicked(data)
            }
        }
    }

//    private fun setLayoutClicked(data: TokoNowCategoryMenuItemUiModel) {
//        RouteManager.route(itemView.context, data.appLink)
//        listener?.onCategoryClicked(
//            position = layoutPosition,
//            categoryId = data.id,
//            headerName = data.headerName,
//            categoryName = data.title
//        )
//    }

    interface TokoNowCategoryMenuItemListener {
        fun onCategoryClicked(
            position: Int,
            categoryId: String,
            headerName: String,
            categoryName: String
        )
    }
}
