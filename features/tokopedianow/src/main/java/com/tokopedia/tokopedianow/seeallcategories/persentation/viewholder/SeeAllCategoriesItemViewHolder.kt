package com.tokopedia.tokopedianow.seeallcategories.persentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.seeallcategories.persentation.uimodel.SeeAllCategoriesItemUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryMenuItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class SeeAllCategoriesItemViewHolder(
    itemView: View,
    private val listener: SeeAllCategoriesListener? = null
): AbstractViewHolder<SeeAllCategoriesItemUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_see_all_categories
    }

    private var binding: ItemTokopedianowCategoryMenuItemBinding? by viewBinding()

    override fun bind(data: SeeAllCategoriesItemUiModel) {
        binding?.apply {
            tpCategoryTitle.text = data.name
            ivCategoryImage.loadImage(data.imageUrl)
            root.setOnClickListener {
                listener?.onCategoryClicked(data)
            }
        }
    }

    interface SeeAllCategoriesListener {
        fun onCategoryClicked(data: SeeAllCategoriesItemUiModel)
    }
}
