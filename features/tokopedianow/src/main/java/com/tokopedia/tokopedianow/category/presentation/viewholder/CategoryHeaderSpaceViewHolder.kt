package com.tokopedia.tokopedianow.category.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.common.util.ViewUtil.safeParseColor
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowCategoryHeaderSpaceBinding
import com.tokopedia.utils.resources.isDarkMode
import com.tokopedia.utils.view.binding.viewBinding

class CategoryHeaderSpaceViewHolder(
    itemView: View
) : AbstractViewHolder<CategoryHeaderSpaceUiModel>(itemView) {
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_category_header_space
    }

    private var binding: ItemTokopedianowCategoryHeaderSpaceBinding? by viewBinding()

    override fun bind(data: CategoryHeaderSpaceUiModel) {
        binding?.root?.apply {
            layoutParams.height = data.space
            setBackgroundColor(
                safeParseColor(
                    color = if (context.isDarkMode()) data.backgroundDarkColor else data.backgroundLightColor,
                    defaultColor = ContextCompat.getColor(
                        itemView.context,
                        R.color.tokopedianow_card_dms_color
                    )
                )
            )
        }
    }
}
