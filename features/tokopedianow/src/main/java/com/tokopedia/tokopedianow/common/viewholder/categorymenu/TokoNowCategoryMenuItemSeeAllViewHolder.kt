package com.tokopedia.tokopedianow.common.viewholder.categorymenu

import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
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
        const val IMG_DARK_MODE = "https://images.tokopedia.net/img/tokopedianow/see-all-category-dark.png"
        const val IMG_LIGHT_MODE = "https://images.tokopedia.net/img/tokopedianow/see-all-category-light.png"


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
