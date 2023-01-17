package com.tokopedia.tokopedianow.common.viewholder.categorymenu

import android.view.View
import androidx.annotation.LayoutRes
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_menu_item_see_all
    }

    private var binding: ItemTokopedianowCategoryMenuItemSeeAllBinding? by viewBinding()

    override fun bind(element: TokoNowCategoryMenuItemSeeAllUiModel) {
        binding?.apply {
            root.setOnClickListener {
                listener?.onSeeAllClicked(element.appLink)
            }
            val drawable = VectorDrawableCompat.create(
                root.context.resources,
                if (root.context.isDarkMode()) R.drawable.tokopedianow_bg_see_all_categories_dark else R.drawable.tokopedianow_bg_see_all_categories_light,
                itemView.context.theme
            )
            ivBackground.setImageDrawable(drawable)
        }
    }

    interface TokoNowCategoryMenuItemSeeAllListener {
        fun onSeeAllClicked(appLink: String)
    }
}
