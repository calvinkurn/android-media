package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryTitleUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryTitleBinding
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class CategoryTitleViewHolder(
    itemView: View,
    private val listener: CategoryTitleListener? = null
): AbstractViewHolder<CategoryTitleUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_title
    }

    private var binding: ItemTokopedianowCategoryTitleBinding? by viewBinding()

    override fun bind(data: CategoryTitleUiModel) {
        binding?.apply {
            tpTitle.text = data.title
            root.setBackgroundColor(
                safeParseColor(
                    color = if (root.context.isDarkMode()) data.backgroundDarkColor else data.backgroundLightColor,
                    defaultColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.tokopedianow_card_dms_color
                    )
                )
            )

            tpAnotherCategory.setOnClickListener {
                listener?.onClickMoreCategories()
            }
        }
    }

    interface CategoryTitleListener {
        fun onClickMoreCategories()
    }
}
