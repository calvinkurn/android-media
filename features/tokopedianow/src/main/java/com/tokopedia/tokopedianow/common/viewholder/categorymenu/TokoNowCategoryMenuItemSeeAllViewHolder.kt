package com.tokopedia.tokopedianow.common.viewholder.categorymenu

import com.tokopedia.imageassets.ImageUrl

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.categorymenu.TokoNowCategoryMenuItemSeeAllUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryMenuItemSeeAllBinding
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class TokoNowCategoryMenuItemSeeAllViewHolder(
    itemView: View,
    private val listener: TokoNowCategoryMenuItemSeeAllListener? = null,
): AbstractViewHolder<TokoNowCategoryMenuItemSeeAllUiModel>(itemView) {

    companion object {
        const val IMG_DARK_MODE = ImageUrl.IMG_DARK_MODE
        const val IMG_LIGHT_MODE = ImageUrl.IMG_LIGHT_MODE

        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_menu_item_see_all
    }

    private var binding: ItemTokopedianowCategoryMenuItemSeeAllBinding? by viewBinding()

    override fun bind(element: TokoNowCategoryMenuItemSeeAllUiModel) {
        binding?.apply {
            root.setOnClickListener {
                listener?.onSeeAllClicked(element.appLink)
            }
            ivBackground.loadImage(if (root.context.isDarkMode()) IMG_DARK_MODE else IMG_LIGHT_MODE)
        }
    }

    interface TokoNowCategoryMenuItemSeeAllListener {
        fun onSeeAllClicked(appLink: String)
    }
}
